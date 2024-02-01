package com.binarfinalproject.rajawali.service;

import java.util.UUID;

import com.binarfinalproject.rajawali.dto.payment.request.CreatePaymentDto;
import com.binarfinalproject.rajawali.dto.payment.response.ResPaymentDto;
import com.binarfinalproject.rajawali.exception.ApiException;

public interface PaymentService {
    ResPaymentDto createPayment(CreatePaymentDto request) throws ApiException;

    ResPaymentDto finishPayment(UUID paymentId) throws ApiException;

    ResPaymentDto verifyPayment(UUID paymentId) throws ApiException;
}
