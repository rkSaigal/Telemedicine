package com.telemedicicne.telemedicicne.Service;

import lombok.Setter;
//import lombok.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OtpService {
    private final Map<String, String> otpStorage = new HashMap<>(); // Temporary storage for OTPs


    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.fromNumber}")
    private String fromNumber;
//    @Override
    public boolean sendOtp(String mobileNumber, String otp) {
        try {
            Twilio.init(accountSid, authToken);
            Message message = Message.creator(
                            new PhoneNumber("+91" + mobileNumber),  // To
                            new PhoneNumber(fromNumber),
                    "Send from your Telemedicine account - Your OTP is: " + otp)
// From (your Twilio phone number)
//                            "Your OTP is: " + otp)
                    .create();

            // Log success or handle response if needed
            System.out.println("OTP sent successfully to " + mobileNumber);
            return true;
        } catch (Exception e) {
            // Log error or handle exception
            e.printStackTrace();
            return false;
        }
    }

    public boolean verifyOtp(String mobileNumber, String otp) {
        // Retrieve stored OTP for the mobile number
        String storedOtp = otpStorage.get(mobileNumber);

        // Check if OTP matches the stored OTP
        if (storedOtp != null && storedOtp.equals(otp)) {
            // OTP matches, verification successful
            // Optionally, remove the OTP from storage after verification
            otpStorage.remove(mobileNumber);
            return true;
        } else {
            // OTP does not match or is expired
            return false;
        }
    }
}
