package com.binarfinalproject.rajawali.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.binarfinalproject.rajawali.dto.payment.request.CreatePaymentDto;
import com.binarfinalproject.rajawali.dto.payment.response.ResPaymentDto;
import com.binarfinalproject.rajawali.entity.Notification;
import com.binarfinalproject.rajawali.entity.Passenger;
import com.binarfinalproject.rajawali.entity.Notification.NotificationType;
import com.binarfinalproject.rajawali.entity.Payment;
import com.binarfinalproject.rajawali.entity.Reservation;
import com.binarfinalproject.rajawali.entity.ReservationDetails;
import com.binarfinalproject.rajawali.entity.User;
import com.binarfinalproject.rajawali.exception.ApiException;
import com.binarfinalproject.rajawali.repository.NotificationRepository;
import com.binarfinalproject.rajawali.repository.PassengerRepository;
import com.binarfinalproject.rajawali.repository.PaymentRepository;
import com.binarfinalproject.rajawali.repository.ReservationDetailsRepository;
import com.binarfinalproject.rajawali.repository.ReservationRepository;
import com.binarfinalproject.rajawali.repository.UserRepository;
import com.binarfinalproject.rajawali.service.PaymentService;
import com.binarfinalproject.rajawali.util.EmailSender;
import com.binarfinalproject.rajawali.util.EmailTemplate;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReservationDetailsRepository reservationDetailsRepository;

    @Autowired
    PassengerRepository passengerRepository;

    @Autowired
    EmailSender emailSender;

    @Autowired
    EmailTemplate emailTemplate;

    public void createPaymentNotification(User user, NotificationType notificationType) {
        String dateFormatted = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now());
        String message = "";
        if (notificationType == NotificationType.CREATE_PAYMENT)
            message = "Payment created on " + dateFormatted
                    + ", check your reservation history to finish your payment!";
        else if (notificationType == NotificationType.FINISH_PAYMENT)
            message = "Payment is successful on " + dateFormatted
                    + ", system will verify your payment.";
        else if (notificationType == NotificationType.APPROVE_PAYMENT)
            message = "Payment is approved on " + dateFormatted
                    + ", enjoy your flight.";
        else if (notificationType == NotificationType.REJECT_PAYMENT)
            message = "Payment is rejected on " + dateFormatted
                    + ", because payment is invalid. Please create new reservation.";

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setNotificationType(notificationType);
        notification.setDescription(message);
        notificationRepository.save(notification);

        user.setNotificationIsSeen(false);
        userRepository.save(user);
    }

    @Transactional(rollbackFor = { ApiException.class, Exception.class })
    @Override
    public ResPaymentDto createPayment(CreatePaymentDto request) throws ApiException {
        Optional<Reservation> reservationOnDb = reservationRepository
                .findById(UUID.fromString(request.getReservationId()));
        if (reservationOnDb.isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "Reservation with id " + request.getReservationId() + " is not found.");

        String receiverNumber = "";
        for (int i = 0; i < 15; i++) {
            receiverNumber += "" + i % 10;
        }
        Payment payment = new Payment();
        payment.setReservation(reservationOnDb.get());
        payment.setMethod(request.getMethod());
        payment.setReceiverNumber(receiverNumber);
        Payment updatedPayment = paymentRepository.saveAndFlush(payment);

        if (updatedPayment.getReservation().getUser() != null)
            createPaymentNotification(updatedPayment.getReservation().getUser(), NotificationType.CREATE_PAYMENT);

        ResPaymentDto resPaymentDto = modelMapper.map(updatedPayment, ResPaymentDto.class);
        resPaymentDto.setPaymentStatus("Wait for Payment");

        return resPaymentDto;
    }

    @Transactional(rollbackFor = { ApiException.class, Exception.class })
    @Override
    public ResPaymentDto finishPayment(UUID paymentId) throws ApiException {
        Optional<Payment> paymentOnDb = paymentRepository.findById(paymentId);
        if (paymentOnDb.isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "Payment with id " + paymentId + " is not found.");

        if (paymentOnDb.get().getIsPaid())
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "Payment with id " + paymentOnDb.get().getId() + " is already paid!");

        if (paymentOnDb.get().getReservation().getExpiredAt().isBefore(LocalDateTime.now()))
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "Payment with id " + paymentOnDb.get().getId() + " is already expired");

        Payment payment = paymentOnDb.get();
        payment.setIsPaid(true);
        payment.setPaidAt(LocalDateTime.now());
        Payment updatedPayment = paymentRepository.saveAndFlush(payment);

        if (updatedPayment.getReservation().getUser() != null)
            createPaymentNotification(updatedPayment.getReservation().getUser(), NotificationType.FINISH_PAYMENT);

        ResPaymentDto resPaymentDto = modelMapper.map(updatedPayment, ResPaymentDto.class);
        resPaymentDto.setPaymentStatus("Purchase Pending");

        // send email
        String templateEmail = emailTemplate.notifyPaymentIsPaid(
                payment.getReservation().getEmail(),
                payment.getReservation().getId().toString(),
                "/transaction/" + payment.getReservation().getId());
        emailSender.sendEmail(payment.getReservation().getEmail(), "Payment is success", templateEmail);

        return resPaymentDto;
    }

    @Override
    public ResPaymentDto approvePayment(UUID paymentId) throws ApiException {
        Optional<Payment> paymentOnDb = paymentRepository.findById(paymentId);
        if (paymentOnDb.isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "Payment with id " + paymentId + " is not found.");

        if (!paymentOnDb.get().getIsPaid())
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "Payment with id " + paymentOnDb.get().getId() + " is not paid yet!");

        Payment payment = paymentOnDb.get();
        payment.setIsApproved(true);
        payment.setVerifiedAt(LocalDateTime.now());
        Payment updatedPayment = paymentRepository.saveAndFlush(payment);

        if (updatedPayment.getReservation().getUser() != null)
            createPaymentNotification(updatedPayment.getReservation().getUser(), NotificationType.APPROVE_PAYMENT);

        ResPaymentDto resPaymentDto = modelMapper.map(updatedPayment, ResPaymentDto.class);
        resPaymentDto.setPaymentStatus("Purchase Successful");

        // send email
        String templateEmail = emailTemplate.notifyPaymentIsPaid(
                payment.getReservation().getEmail(),
                payment.getReservation().getId().toString(),
                "/transaction/" + payment.getReservation().getId());
        emailSender.sendEmail(payment.getReservation().getEmail(), "Payment is approved", templateEmail);

        return resPaymentDto;
    }

    @Transactional(rollbackFor = { ApiException.class, Exception.class })
    @Override
    public ResPaymentDto rejectPayment(UUID paymentId) throws ApiException {
        Optional<Payment> paymentOnDb = paymentRepository.findById(paymentId);
        if (paymentOnDb.isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "Payment with id " + paymentId + " is not found.");

        if (!paymentOnDb.get().getIsPaid())
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "Payment with id " + paymentOnDb.get().getId() + " is not paid yet!");

        Payment payment = paymentOnDb.get();
        payment.setIsApproved(false);
        payment.setVerifiedAt(LocalDateTime.now());
        Payment updatedPayment = paymentRepository.saveAndFlush(payment);

        if (updatedPayment.getReservation().getUser() != null)
            createPaymentNotification(updatedPayment.getReservation().getUser(), NotificationType.REJECT_PAYMENT);

        // delete all the data that connects to the reservation
        for (ReservationDetails rd : updatedPayment.getReservation().getReservationDetails()) {
            for (Passenger p : rd.getPassengers()) {
                p.setDeletedAt(LocalDateTime.now());
                Passenger deletedPassenger = passengerRepository.saveAndFlush(p);
                passengerRepository.delete(deletedPassenger);
            }
            rd.setDeletedAt(LocalDateTime.now());
            ReservationDetails deletedReservationDetails = reservationDetailsRepository
                    .saveAndFlush(rd);
            reservationDetailsRepository.delete(deletedReservationDetails);
        }

        ResPaymentDto resPaymentDto = modelMapper.map(updatedPayment, ResPaymentDto.class);
        resPaymentDto.setPaymentStatus("Payment Not Valid");

        // send email
        String templateEmail = emailTemplate.notifyPaymentIsPaid(
                payment.getReservation().getEmail(),
                payment.getReservation().getId().toString(),
                "/transaction/" + payment.getReservation().getId());
        emailSender.sendEmail(payment.getReservation().getEmail(), "Payment is rejected", templateEmail);

        return resPaymentDto;
    }
}
