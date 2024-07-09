package com.telemedicicne.telemedicicne.Config;

import java.util.Random;

public class OTPGenerator {

    private static final String NUMBERS = "0123456789";
    private static final int OTP_LENGTH = 6;

    public static String generateOTP() {
        Random random = new Random();
        StringBuilder otp = new StringBuilder(OTP_LENGTH);
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(NUMBERS.charAt(random.nextInt(NUMBERS.length())));
        }
        return otp.toString();
    }
}
