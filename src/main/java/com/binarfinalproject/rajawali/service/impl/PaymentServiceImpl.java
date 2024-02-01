package com.binarfinalproject.rajawali.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.binarfinalproject.rajawali.dto.payment.request.CreatePaymentDto;
import com.binarfinalproject.rajawali.dto.payment.response.ResPaymentDto;
import com.binarfinalproject.rajawali.entity.Payment;
import com.binarfinalproject.rajawali.entity.Reservation;
import com.binarfinalproject.rajawali.exception.ApiException;
import com.binarfinalproject.rajawali.repository.PaymentRepository;
import com.binarfinalproject.rajawali.repository.ReservationRepository;
import com.binarfinalproject.rajawali.service.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    PaymentRepository paymentRepository;

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

        ResPaymentDto resPaymentDto = modelMapper.map(paymentRepository.save(payment), ResPaymentDto.class);

        return resPaymentDto;
    }

    @Override
    public ResPaymentDto finishPayment(UUID paymentId) throws ApiException {
        Optional<Payment> paymentOnDb = paymentRepository.findById(paymentId);
        if (paymentOnDb.isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "Payment with id " + paymentId + " is not found.");

        if (paymentOnDb.get().getReservation().getExpiredAt().isBefore(LocalDateTime.now()))
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "Payment with id " + paymentOnDb.get().getId() + " is already expired");

        if (paymentOnDb.get().getIsPaid())
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "Payment with id " + paymentOnDb.get().getId() + " is already paid!");

        Payment payment = paymentOnDb.get();
        payment.setIsPaid(true);
        payment.setPaidAt(LocalDateTime.now());
        ResPaymentDto resPaymentDto = modelMapper.map(paymentRepository.save(payment), ResPaymentDto.class);

        return resPaymentDto;
    }

    @Override
    public ResPaymentDto verifyPayment(UUID paymentId) throws ApiException {
        Optional<Payment> paymentOnDb = paymentRepository.findById(paymentId);
        if (paymentOnDb.isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "Payment with id " + paymentId + " is not found.");

        if (!paymentOnDb.get().getIsPaid())
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "Payment with id " + paymentOnDb.get().getId() + " is not paid yet!");

        Payment payment = paymentOnDb.get();
        payment.setIsVerified(true);
        payment.setVerifiedAt(LocalDateTime.now());
        ResPaymentDto resPaymentDto = modelMapper.map(paymentRepository.save(payment), ResPaymentDto.class);

        return resPaymentDto;
    }

}
