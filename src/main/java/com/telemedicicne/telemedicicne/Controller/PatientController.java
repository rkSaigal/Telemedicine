package com.telemedicicne.telemedicicne.Controller;




import com.telemedicicne.telemedicicne.Config.OTPGenerator;
import com.telemedicicne.telemedicicne.Entity.*;
import com.telemedicicne.telemedicicne.Model.JwtResponse;
import com.telemedicicne.telemedicicne.Repository.DoctorRepository;
import com.telemedicicne.telemedicicne.Repository.HealthOfficerRepository;
import com.telemedicicne.telemedicicne.Repository.PatientRepository;
import com.telemedicicne.telemedicicne.Request.PatientLoginRequest;
import com.telemedicicne.telemedicicne.Request.PatientRequest;

import com.telemedicicne.telemedicicne.Security.JwtHelper;
import com.telemedicicne.telemedicicne.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private UserService userService;


    @Autowired
    private OtpService otpService;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private DoctorHospitalService docHSService;
    @Autowired
    private AuthenticationService authenticationService;




    @PostMapping("/register")
    public ResponseEntity<String> registerPatient(@RequestHeader("Auth") String jwtToken, @RequestBody PatientRequest patientRequest) {
        // Extract username from JWT token
        String token = jwtToken.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);

        // Fetch HealthOfficer (Health Officer) by username
//        HealthOfficer docHs = docHSRepository.findByEmail(username);

        // Fetch user details if needed
        HealthOfficer docHs = docHSService.findByUsername(username);

        if (docHs != null) {
            // Create a new Patient entity
            Patient patient = new Patient();
            patient.setPatientName(patientRequest.getPatientName());
            patient.setAddharNo(patientRequest.getAddharNo());
            patient.setHeight(patientRequest.getHeight());
            patient.setWeight(patientRequest.getWeight());
            patient.setAge(patientRequest.getAge());
            patient.setGender(patientRequest.getGender());
            patient.setEmail(patientRequest.getEmail());
            patient.setMobileNo(patientRequest.getMobileNo());
            patient.setAddress(patientRequest.getAddress());

            // Associate Patient with HealthOfficer
            patient.setHealthOfficer(docHs);

            // Save patient
            patientRepository.save(patient);

            return new ResponseEntity<>("Patient registered successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Health Officer not found!", HttpStatus.NOT_FOUND);
        }
    }




    @PostMapping(value = "/login",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtResponse> login(@RequestBody PatientLoginRequest request) {
        UserDetails userDetails = authenticationService.authenticate(request.getMobileNo(), request.getPassword());
// Check if the user exists in the Patient table
        Optional<Patient> patientOptional = patientRepository.findByMobileNo(request.getMobileNo());
        if (patientOptional.isPresent()) {
            Patient patient = patientOptional.get();

            // Generate JWT token
            String token = jwtHelper.generateToken(userDetails);

            // Create refresh token
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());

            // Prepare JwtResponse
            JwtResponse response = JwtResponse.builder()
                    .jwtToken(token)
                    .refreshToken(refreshToken.getRefreshToken())
                    .userId(patient.getPatientId().toString())
                    .username(userDetails.getUsername())
                    .role(patient.getRoles().toString())
                    .build();

            return ResponseEntity.ok(response);
        }

        // If user not found in either table, handle accordingly (throw exception or return error response)
        throw new RuntimeException("User not found with email: " + request.getMobileNo());
    }






    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestParam("mobileNo") String mobileNo) {
        Optional<Patient> patientOptional = patientRepository.findByMobileNo(mobileNo);
        if (patientOptional.isPresent()) {
            Patient patient = patientOptional.get();
            String otpCode = OTPGenerator.generateOTP();
            boolean otpSent = otpService.sendOtp(mobileNo, otpCode);

            if (otpSent) {
                // Save OTP and expiration time
                patient.setOtpCode(otpCode);
                patient.setOtpExpiration(LocalDateTime.now().plusMinutes(5)); // OTP valid for 5 minutes
                patientRepository.save(patient);

                return ResponseEntity.ok("OTP sent successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send OTP.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found for the given mobile number.");
        }
    }


    @PostMapping(value = "/verify-otp", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> verifyOtp(@RequestParam("mobileNo") String mobileNo, @RequestParam("otp") String otp) {
        Optional<Patient> patientOptional = patientRepository.findByMobileNo(mobileNo);
        if (patientOptional.isPresent()) {
            Patient patient = patientOptional.get();

            // Verify OTP
            if (otp.equals(patient.getOtpCode()) && LocalDateTime.now().isBefore(patient.getOtpExpiration())) {
//                // Generate JWT token
//                UserDetails userDetails = new org.springframework.security.core.userdetails.User(
//                        patient.getUsername(),  patient.getAuthorities());
                // Generate JWT token
                UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                        patient.getUsername(),
//                        patient.getPassword(),
                        "",
                        patient.getRoles().stream()
                                .map(role -> new SimpleGrantedAuthority(role.getName()))
                                .collect(Collectors.toList())
                );
                String token = jwtHelper.generateToken(userDetails);
                RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());

                // Prepare JwtResponse
                JwtResponse response = JwtResponse.builder()
                        .jwtToken(token)
                        .refreshToken(refreshToken.getRefreshToken())
                        .userId(patient.getPatientId().toString())
                        .username(userDetails.getUsername())
                        .role(patient.getRoles().stream().map(Role::getName).collect(Collectors.joining(", ")))
                        .build();

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body("Invalid OTP or OTP expired.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found for the given mobile number.");
        }
    }

}





