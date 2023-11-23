package com.example.demoOTP.Controller;

import com.example.demoOTP.Service.EmailService;
import com.example.demoOTP.Service.EmailTemplate;
import com.example.demoOTP.Service.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {
    @Autowired
    public OTPService otpService;

    @Autowired
    public EmailService emailService;

    @GetMapping("/generateOtp")
    public ResponseEntity<String> generateOTP(@RequestParam("email") String email) throws MessagingException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        int otp = otpService.generateOTP(username);

        // Truyền nội dung template trực tiếp (ví dụ: "<p>Hi {{user}}, your OTP is {{otpnum}}</p>")
        EmailTemplate template = new EmailTemplate("<p>Hi {{user}}, your OTP is {{otpnum}}</p>");

        Map<String, String> replacements = new HashMap<>();
        replacements.put("user", username);
        replacements.put("otpnum", String.valueOf(otp));
        String message = template.getTemplate(replacements);

        emailService.sendOtpMessage(email, "OTP -SpringBoot", message);

        return new ResponseEntity<>("OTP generated and sent successfully", HttpStatus.OK);
    }

    @GetMapping("/validateOtp")
    public ResponseEntity<String> validateOtp(@RequestParam("otpnum") int otpnum) {

        final String SUCCESS = "Success";
        final String FAIL = "Fail";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        // Validate the Otp
        if (otpnum >= 0) {

            int serverOtp = otpService.getOtp(username);
            if (serverOtp > 0) {
                if (otpnum == serverOtp) {
                    otpService.clearOTP(username);

                    return new ResponseEntity<>(SUCCESS, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(FAIL, HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>(FAIL, HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(FAIL, HttpStatus.BAD_REQUEST);
        }
    }
}
