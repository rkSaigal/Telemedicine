package com.telemedicicne.telemedicicne.Controller;

import com.telemedicicne.telemedicicne.Entity.Patient.Patient;
import com.telemedicicne.telemedicicne.Entity.Patient.PatientRepository;
import com.telemedicicne.telemedicicne.Repository.DocHSRepository;
import com.telemedicicne.telemedicicne.Entity.DocHs;
import com.telemedicicne.telemedicicne.Entity.RefreshToken;
import com.telemedicicne.telemedicicne.Entity.User;
import com.telemedicicne.telemedicicne.Model.JwtRequest;
import com.telemedicicne.telemedicicne.Model.JwtResponse;
import com.telemedicicne.telemedicicne.Repository.UserRepository;
import com.telemedicicne.telemedicicne.Request.RegistrationRequest;
import com.telemedicicne.telemedicicne.Security.JwtHelper;
import com.telemedicicne.telemedicicne.Service.AuthenticationService;
import com.telemedicicne.telemedicicne.Service.RefreshTokenService;
import com.telemedicicne.telemedicicne.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private RefreshTokenService refreshTokenService;


    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private UserRepository userRepository;
//    @Autowired
//    private UserService userService;
    private final UserService userService;
    private final ApplicationEventPublisher publisher;
    Logger logger = LoggerFactory.getLogger(UserController.class);



    //for registering the user n application
    @PostMapping("/register")
    public String registerUser(@RequestBody RegistrationRequest registrationRequest, final HttpServletRequest request) {
        User user = userService.registerUser(registrationRequest);
//        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
//        return "Success!  Please, check your email for to complete your registration";
        return "Success!  Your Registration is Complete";

    }


    // for user's login
//    @PostMapping("/login")
//    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request)
//    {
//        this.doAuthenticate(request.getEmail(), request.getPassword());
//
//        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
//        String token = this.helper.generateToken(userDetails);
//
//        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());
//
//        Optional<User> user = userRepository.findByEmail(request.getEmail());
//
//        User usr = user.get();
//
//        JwtResponse response = JwtResponse.builder()
//                .jwtToken(token)
//                .refreshToken(refreshToken.getRefreshToken())
//                .userId(usr.getUserId().toString())
//                .username(userDetails.getUsername()).build();
//        return new ResponseEntity<>(response, HttpStatus.OK);
//
//    }

    @Autowired
    private DocHSRepository docHSRepository;
//    @PostMapping("/login")
//    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
//        // Authenticate the user with provided credentials
//        authenticate(request.getEmail(), request.getPassword());
//
//        // Load UserDetails for the authenticated user
//        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
//
//        // Check if the user exists in the User table
//        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
//        if (userOptional.isPresent()) {
//            User user = userOptional.get();
//
//            // Generate JWT token
//            String token = jwtHelper.generateToken(userDetails);
//
//            // Create refresh token
//            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());
//
//            // Prepare JwtResponse
//            JwtResponse response = JwtResponse.builder()
//                    .jwtToken(token)
//                    .refreshToken(refreshToken.getRefreshToken())
//                    .userId(user.getUserId().toString())
//                    .username(userDetails.getUsername())
//                    .build();
//
//            return ResponseEntity.ok(response);
//        }
//
//        // Check if the user exists in the DocHs table
//        Optional<DocHs> docHsOptional = docHSRepository.findByEmail(request.getEmail());
//        if (docHsOptional.isPresent()) {
//            DocHs docHs = docHsOptional.get();
//
//            // Generate JWT token
//            String token = jwtHelper.generateToken(userDetails);
//
//            // Create refresh token
//            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());
//
//            // Prepare JwtResponse
//            JwtResponse response = JwtResponse.builder()
//                    .jwtToken(token)
//                    .refreshToken(refreshToken.getRefreshToken())
//                    .userId(docHs.getDocHsId().toString())
//                    .username(userDetails.getUsername())
//                    .build();
//
//            return ResponseEntity.ok(response);
//        }
//
//        // If user not found in either table, handle accordingly (throw exception or return error response)
//        throw new UsernameNotFoundException("User not found with email: " + request.getEmail());
//    }


//    @PostMapping("/login")
//    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
//        // Authenticate the user with provided credentials
//        authenticate(request.getEmail(), request.getPassword());
//
//        // Load UserDetails for the authenticated user
//        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
//
//        // Check if the user exists in the User table
//        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
//        if (userOptional.isPresent()) {
//            User user = userOptional.get();
//
//            // Generate JWT token
//            String token = jwtHelper.generateToken(userDetails);
//
//            // Create refresh token
//            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());
//
//            // Prepare JwtResponse
//            JwtResponse response = JwtResponse.builder()
//                    .jwtToken(token)
//                    .refreshToken(refreshToken.getRefreshToken())
//                    .userId(user.getUserId().toString())
//                    .username(userDetails.getUsername())
//                    .build();
//
//            return ResponseEntity.ok(response);
//        }
//
//        // Check if the user exists in the DocHs table
//        Optional<DocHs> docHsOptional = docHSRepository.findByEmail(request.getEmail());
//        if (docHsOptional.isPresent()) {
//            DocHs docHs = docHsOptional.get();
//
//            // Generate JWT token
//            String token = jwtHelper.generateToken(userDetails);
//
//            // Create refresh token
//            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());
//
//            // Prepare JwtResponse
//            JwtResponse response = JwtResponse.builder()
//                    .jwtToken(token)
//                    .refreshToken(refreshToken.getRefreshToken())
//                    .userId(docHs.getDocHsId().toString())
//                    .username(userDetails.getUsername())
//                    .build();
//
//            return ResponseEntity.ok(response);
//        }
//
//        // If user not found in either table, handle accordingly (throw exception or return error response)
//        throw new UsernameNotFoundException("User not found with email: " + request.getEmail());
//    }

    private void authenticate(String email, String password) {
        manager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }

    // do authentication of the user
    private void doAuthenticate(String email, String password) {

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        try {
            manager.authenticate(authentication);
        }catch (BadCredentialsException e){
            throw new BadCredentialsException("Invalid Username or Password !!");
        }
    }

    @Autowired
    private AuthenticationService authenticationService;
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
        UserDetails userDetails = authenticationService.authenticate(request.getEmail(), request.getPassword());

        // Check if the user exists in the User table
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Generate JWT token
            String token = jwtHelper.generateToken(userDetails);

            // Create refresh token
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());

            // Prepare JwtResponse
            JwtResponse response = JwtResponse.builder()
                    .jwtToken(token)
                    .refreshToken(refreshToken.getRefreshToken())
                    .userId(user.getUserId().toString())
                    .username(userDetails.getUsername())
                    .role(user.getRoles().toString())
                    .build();

            return ResponseEntity.ok(response);
        }

        // Check if the user exists in the DocHs table
        Optional<DocHs> docHsOptional = docHSRepository.findByEmail(request.getEmail());
        if (docHsOptional.isPresent()) {
            DocHs docHs = docHsOptional.get();

            // Generate JWT token
            String token = jwtHelper.generateToken(userDetails);

            // Create refresh token
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());

            // Prepare JwtResponse
            JwtResponse response = JwtResponse.builder()
                    .jwtToken(token)
                    .refreshToken(refreshToken.getRefreshToken())
                    .userId(docHs.getDocHsId().toString())
                    .username(userDetails.getUsername())
                    .role(docHs.getRoles().toString())
                    .build();

            return ResponseEntity.ok(response);
        }

//
//        // Check if the user exists in the Patient table
//        Optional<Patient> patientOptional = patientRepository.findByMobileNo(request.getEmail());
//        if (patientOptional.isPresent()) {
//            Patient patient = patientOptional.get();
//
//            // Generate JWT token
//            String token = jwtHelper.generateToken(userDetails);
//
//            // Create refresh token
//            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());
//
//            // Prepare JwtResponse
//            JwtResponse response = JwtResponse.builder()
//                    .jwtToken(token)
//                    .refreshToken(refreshToken.getRefreshToken())
//                    .userId(patient.getPatientId().toString())
//                    .username(userDetails.getUsername())
//                    .role(patient.getRoles().toString())
//                    .build();
//
//            return ResponseEntity.ok(response);
//        }

        // If user not found in either table, handle accordingly (throw exception or return error response)
        throw new RuntimeException("User not found with email: " + request.getEmail());
    }
}