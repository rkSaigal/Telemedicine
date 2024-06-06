package com.telemedicicne.telemedicicne.Entity.Patient;




import com.telemedicicne.telemedicicne.Entity.DocHs;
import com.telemedicicne.telemedicicne.Entity.RefreshToken;
import com.telemedicicne.telemedicicne.Entity.Role;
import com.telemedicicne.telemedicicne.Entity.User;
import com.telemedicicne.telemedicicne.Model.JwtRequest;
import com.telemedicicne.telemedicicne.Model.JwtResponse;
import com.telemedicicne.telemedicicne.Request.PatientRequest;

import com.telemedicicne.telemedicicne.Security.JwtHelper;
import com.telemedicicne.telemedicicne.Service.AuthenticationService;
import com.telemedicicne.telemedicicne.Service.DocHSService;
import com.telemedicicne.telemedicicne.Service.RefreshTokenService;
import com.telemedicicne.telemedicicne.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private PatientRepository patientRepository;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private DocHSService docHSService;

    @Autowired
    private PasswordEncoder passwordEncoder;

//    @PostMapping("/healthOfficer")
//    public ResponseEntity<String> registerHealthOfficer(@RequestHeader("Auth") String jwtToken, @RequestBody DocHs healthOfficer) {
//        // Extract username from JWT token
//        String token = jwtToken.replace("Bearer ", "");
//        String username = jwtHelper.getUsernameFromToken(token);
//
//        // Fetch user details if needed
//        User user = userService.findByUsername(username);
//
//        // Check if the user's role is SUB_ADMIN
//        if (user.getRoles().stream().anyMatch(role -> role.getName().equals("SUB_ADMIN"))) {
//            // Set type and register health officer
////            healthOfficer.setType("HEALTH_OFFICER");
//            docHSService.registerDocHS(healthOfficer);
//
//            return new ResponseEntity<>("Health Officer registered successfully!", HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>("Unauthorized to register health officers!", HttpStatus.UNAUTHORIZED);
//        }
//    }

    @PostMapping("/register")
//    @PreAuthorize("hasRole('HEALTH_OFFICER')")
    public ResponseEntity<String> registerPatient(@RequestHeader("Auth") String jwtToken, @RequestBody PatientRequest patientRequest) {


        // Extract username from JWT token
        String token = jwtToken.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);

        // Fetch user details if needed
        DocHs docHs = docHSService.findByUsername(username);


        // Check if the user's role is SUB_ADMIN
        if (docHs.getRoles().stream().anyMatch(role -> role.getName().equals("HEALTH_OFFICER"))) {
            // Set type and register health officer
//            healthOfficer.setType("HEALTH_OFFICER");
//            docHSService.registerDocHS(healthOfficer);

//            return new ResponseEntity<>("Health Officer registered successfully!", HttpStatus.OK);

            // Create a new Patient entity from the request
            Patient patient = new Patient();
            patient.setPatientName(patientRequest.getPatientName());
            patient.setAddharNo(patientRequest.getAddharNo());
            patient.setHeight(patientRequest.getHeight());
            patient.setWeight(patientRequest.getWeight());
            patient.setAge(patientRequest.getAge());
            patient.setGender(patientRequest.getGender());
            patient.setEmail(patientRequest.getEmail());

            // Encode the password
//            String encodedPassword = passwordEncoder.encode(patientRequest.getPassword());
//            patient.setPassword(encodedPassword); // Set the encoded password to DocHs entity
//            patient.setPassword(patientRequest.getPassword());
            patient.setMobileNo(patientRequest.getMobileNo());
            patient.setAddress(patientRequest.getAddress());

            // Register patient using patientService
            patientService.registerPatient(patient);

            return new ResponseEntity<>("Patient registered successfully!", HttpStatus.OK);

//            return new ResponseEntity<>("Patient registered successfully!", HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Health Officer registered successfully!", HttpStatus.OK);

        }
    }



    @Autowired
    private AuthenticationService authenticationService;
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

    // Example for sending OTP via Twilio (inside PatientController)
//    @PostMapping("/send-otp")
//    public ResponseEntity<String> sendOtp(@RequestParam("mobileNo") String mobileNo) {
//        // Generate OTP and send via Twilio
//        String otpCode = generateRandomOtp();
//        LocalDateTime otpExpiration = LocalDateTime.now().plusMinutes(5); // Valid for 5 minutes
//        boolean otpSent = twilioService.sendOtp(mobileNo, otpCode);
//
//        // Save OTP to patient entity
//        Optional<Patient> patientOptional = patientRepository.findByMobileNo(mobileNo);
//        if (patientOptional.isPresent()) {
//            Patient patient = patientOptional.get();
//            patient.setOtpCode(otpCode);
//            patient.setOtpExpiration(otpExpiration);
//            patientRepository.save(patient);
//            return ResponseEntity.ok("OTP sent successfully.");
//        }
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found for mobile number: " + mobileNo);
//    }
//
//
//
//
//
//    // Example for verifying OTP (inside PatientController)
//    @PostMapping("/verify-otp")
//    public ResponseEntity<String> verifyOtp(@RequestParam("mobileNo") String mobileNo, @RequestParam("otp") String otp) {
//        Optional<Patient> patientOptional = patientRepository.findByMobileNo(mobileNo);
//        if (patientOptional.isPresent()) {
//            Patient patient = patientOptional.get();
//            if (otp.equals(patient.getOtpCode()) && LocalDateTime.now().isBefore(patient.getOtpExpiration())) {
//                // OTP verified successfully, proceed with JWT token generation and login
//                UserDetails userDetails = authenticationService.authenticate(mobileNo, patient.getPassword());
//                String token = jwtHelper.generateToken(userDetails);
//                RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());
//
//                JwtResponse response = JwtResponse.builder()
//                        .jwtToken(token)
//                        .refreshToken(refreshToken.getRefreshToken())
//                        .userId(patient.getPatientId().toString())
//                        .username(userDetails.getUsername())
//                        .role(patient.getRoles().toString())
//                        .build();
//
//                return ResponseEntity.ok(response);
//            } else {
//                return ResponseEntity.badRequest().body("Invalid OTP or OTP expired.");
//            }
//        }
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found for mobile number: " + mobileNo);
//    }



    @Autowired
    private OtpService otpService;

//    @PostMapping("/send-otp")
//    public ResponseEntity<String> sendOtp(@RequestParam("addharNo") String addharNo) {
//        Optional<Patient> patientOptional = patientRepository.findByAddharNo(addharNo);
//        if (patientOptional.isPresent()) {
//            Patient patient = patientOptional.get();
//            String otpCode = OTPGenerator.generateOTP();
//            boolean otpSent = otpService.sendOtp(patient.getMobileNo(), otpCode);
//
//            // Store OTP and its expiration time in patient entity
//            patient.setOtpCode(otpCode);
//            patient.setOtpExpiration(LocalDateTime.now().plusMinutes(5)); // Valid for 5 minutes
//            patientRepository.save(patient);
//
//            return ResponseEntity.ok("OTP sent successfully to mobile number linked with Aadhaar number.");
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found for Aadhaar number: " + addharNo);
//        }
//    }

    //    @PostMapping("/send-otp")
//    public ResponseEntity<String> sendOtp(@RequestParam("mobileNo") String mobileNo) {
//        String otpCode = OTPGenerator.generateOTP();
//        boolean otpSent = otpService.sendOtp(mobileNo, otpCode); // Use OTPService to send OTP
//
//        if (otpSent) {
//            return ResponseEntity.ok("OTP sent successfully.");
//        } else {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send OTP.");
//        }
//    }
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
//


//    @PostMapping("/verify-otp")
//    public ResponseEntity<?> verifyOtp(
//            @RequestParam("mobileNo") String mobileNo,
//            @RequestParam("otp") String otp) {
//        Optional<Patient> patientOptional = patientRepository.findByMobileNo(mobileNo);
//        if (patientOptional.isPresent()) {
//            Patient patient = patientOptional.get();
//
//            // Verify OTP
//            if (otp.equals(patient.getOtpCode()) && LocalDateTime.now().isBefore(patient.getOtpExpiration())) {
//                // Generate JWT token
//                UserDetails userDetails = authenticationService.authenticate(mobileNo, patient.getPassword());
//                String token = jwtHelper.generateToken(userDetails);
//                RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());
//
//                // Prepare JwtResponse
//                JwtResponse response = JwtResponse.builder()
//                        .jwtToken(token)
//                        .refreshToken(refreshToken.getRefreshToken())
//                        .userId(patient.getPatientId().toString())
//                        .username(userDetails.getUsername())
//                        .role(patient.getRoles().toString())
//                        .build();
//
//                return ResponseEntity.ok(response);
//            } else {
//                return ResponseEntity.badRequest().body("Invalid OTP or OTP expired.");
//            }
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found for the given mobile number.");
//        }
//    }
//@PostMapping("/verify-otp")
//public ResponseEntity<?> verifyOtp(@RequestParam("mobileNo") String mobileNo, @RequestParam("otp") String otp) {
//    Optional<Patient> patientOptional = patientRepository.findByMobileNo(mobileNo);
//    if (patientOptional.isPresent()) {
//        Patient patient = patientOptional.get();
//
//        // Verify OTP
//        if (otp.equals(patient.getOtpCode()) && LocalDateTime.now().isBefore(patient.getOtpExpiration())) {
//            // Generate JWT token
//            UserDetails userDetails = new org.springframework.security.core.userdetails.User(
//                    patient.getUsername(), patient.getPassword(), patient.getAuthorities());
//            String token = jwtHelper.generateToken(userDetails);
//            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());
//
//            // Prepare JwtResponse
//            JwtResponse response = JwtResponse.builder()
//                    .jwtToken(token)
//                    .refreshToken(refreshToken.getRefreshToken())
//                    .userId(patient.getPatientId().toString())
//                    .username(userDetails.getUsername())
//                    .role(patient.getRoles().stream().map(Role::getName).collect(Collectors.joining(", ")))
//                    .build();
//
//            return ResponseEntity.ok(response);
//        } else {
//            return ResponseEntity.badRequest().body("Invalid OTP or OTP expired.");
//        }
//    } else {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found for the given mobile number.");
//    }
//}

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





