package com.binarfinalproject.rajawali.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.binarfinalproject.rajawali.dto.payment.request.CreatePaymentDto;
import com.binarfinalproject.rajawali.dto.payment.response.ResPaymentDto;
import com.binarfinalproject.rajawali.exception.ApiException;
import com.binarfinalproject.rajawali.service.PaymentService;
import com.binarfinalproject.rajawali.util.ResponseMapper;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/v1/payments")
public class PaymentController {
    @Autowired
    PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<Object> createPayment(@Valid @RequestBody CreatePaymentDto request) {
        try {
            ResPaymentDto response = paymentService.createPayment(request);
            return ResponseMapper.generateResponseSuccess(HttpStatus.OK, "Payment has successfully created!",
                    response);
        } catch (ApiException e) {
            return ResponseMapper.generateResponseFailed(
                    e.getStatus(), e.getMessage());
        } catch (Exception e) {
            return ResponseMapper.generateResponseFailed(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/{paymentId}/finish")
    public ResponseEntity<Object> finishPayment(@PathVariable UUID paymentId) {
        try {
            ResPaymentDto response = paymentService.finishPayment(paymentId);
            return ResponseMapper.generateResponseSuccess(HttpStatus.OK, "Payment has successfully done!",
                    response);
        } catch (ApiException e) {
            return ResponseMapper.generateResponseFailed(
                    e.getStatus(), e.getMessage());
        } catch (Exception e) {
            return ResponseMapper.generateResponseFailed(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
