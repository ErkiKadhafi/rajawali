package com.binarfinalproject.rajawali.service.impl;
import java.util.Random;

import com.binarfinalproject.rajawali.service.OtpService;

public class OtpServiceImpl implements OtpService {
    private final int otpLength = 6;
    @Override
    public Integer createRandomOneTimePassword() {
        Random random = new Random();
        StringBuilder oneTimePassword = new StringBuilder();
        for (int i = 0; i < otpLength; i++) {
            int randomNumber = random.nextInt(10);
            oneTimePassword.append(randomNumber);
        }
        return Integer.parseInt(oneTimePassword.toString().trim());
    }
}
