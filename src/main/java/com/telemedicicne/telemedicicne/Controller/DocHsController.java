package com.telemedicicne.telemedicicne.Controller;




import com.telemedicicne.telemedicicne.Repository.DocHSRepository;
import com.telemedicicne.telemedicicne.Service.DocHSService;
import com.telemedicicne.telemedicicne.Entity.DocHs;

import com.telemedicicne.telemedicicne.Entity.RefreshToken;
import com.telemedicicne.telemedicicne.Entity.User;
import com.telemedicicne.telemedicicne.Model.JwtRequest;
import com.telemedicicne.telemedicicne.Model.JwtResponse;
import com.telemedicicne.telemedicicne.Repository.UserRepository;
import com.telemedicicne.telemedicicne.Security.JwtHelper;
import com.telemedicicne.telemedicicne.Service.RefreshTokenService;
import com.telemedicicne.telemedicicne.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/registerDocHs")
public class DocHsController {

    @Autowired
    private final DocHSService docHSService;

    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private DocHSRepository docHSRepository;
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;
//
    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/doctor")
    public ResponseEntity<String> registerDoctor(@RequestHeader("Auth") String jwtToken, @RequestBody DocHs doctor) {
        // Extract username from JWT token
        String token = jwtToken.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);

        // Fetch user details if needed
        User user = userService.findByUsername(username);

        // Check if the user's role is SUB_ADMIN
        if (user.getRoles().stream().anyMatch(role -> role.getName().equals("SUB_ADMIN"))) {
            // Set type and register doctor
//            doctor.setType("DOCTOR");
            docHSService.registerDocHS(doctor);

            return new ResponseEntity<>("Doctor registered successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Unauthorized to register doctors!", HttpStatus.UNAUTHORIZED);
        }
    }



    @PostMapping("/healthOfficer")
    public ResponseEntity<String> registerHealthOfficer(@RequestHeader("Auth") String jwtToken, @RequestBody DocHs healthOfficer) {
        // Extract username from JWT token
        String token = jwtToken.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);

        // Fetch user details if needed
        User user = userService.findByUsername(username);

        // Check if the user's role is SUB_ADMIN
        if (user.getRoles().stream().anyMatch(role -> role.getName().equals("SUB_ADMIN"))) {
            // Set type and register health officer
//            healthOfficer.setType("HEALTH_OFFICER");
            docHSService.registerDocHS(healthOfficer);

            return new ResponseEntity<>("Health Officer registered successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Unauthorized to register health officers!", HttpStatus.UNAUTHORIZED);
        }
    }
//    @PostMapping("/login")
//    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request)
//    {
//        this.doAuthenticate(request.getEmail(), request.getPassword());
//
//        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
//        String token = this.jwtHelper.generateToken(userDetails);
//
//        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());
//
//        Optional<DocHs> docHs = docHSRepository.findByEmail(request.getEmail());
//
//        DocHs docHs1 = docHs.get();
//
//        JwtResponse response = JwtResponse.builder()
//                .jwtToken(token)
//                .refreshToken(refreshToken.getRefreshToken())
//                .userId(docHs1.getDocHsId().toString())
//                .username(userDetails.getUsername()).build();
//        return new ResponseEntity<>(response, HttpStatus.OK);
//
//    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
        // Authenticate user
        this.doAuthenticate(request.getEmail(), request.getPassword());

//        // Try to load as a regular User
//        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
//        Optional<User> user = userRepository.findByEmail(request.getEmail());
//
//        if (user.isPresent()) {
//            User regularUser = user.get();
//            String token = jwtHelper.generateToken(userDetails);
//            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());
//
//            JwtResponse response = JwtResponse.builder()
//                    .jwtToken(token)
//                    .refreshToken(refreshToken.getRefreshToken())
//                    .userId(regularUser.getUserId().toString())
//                    .username(userDetails.getUsername())
//                    .build();
//
//            return new ResponseEntity<>(response, HttpStatus.OK);
//        }

        // If not found as a regular User, try to load as DocHs
        Optional<DocHs> docHs = docHSRepository.findByEmail(request.getEmail());
        if (docHs.isPresent()) {
            DocHs docHs1 = docHs.get();
            String token = jwtHelper.generateToken(docHs1);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(docHs1.getEmail());

            JwtResponse response = JwtResponse.builder()
                    .jwtToken(token)
                    .refreshToken(refreshToken.getRefreshToken())
                    .userId(docHs1.getDocHsId().toString())
                    .username(docHs1.getEmail())
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        // Handle case where user is not found in either table
        throw new IllegalArgumentException("User not found");
    }
//
//    // Helper method for user authentication
//    private void doAuthenticate(String email, String password) {
//        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
//        try {
//            manager.authenticate(authentication);
//        } catch (BadCredentialsException e) {
//            throw new BadCredentialsException("Invalid Username or Password !!");
//        }
//    }

//    @PostMapping("/doctor")
//    public ResponseEntity<JwtResponse> loginDoctor(@RequestBody JwtRequest request) {
//        // Authenticate user
//        this.doAuthenticate(request.getEmail(), request.getPassword());
//
//        // Load user details
//        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
//
//        // Generate JWT token
//        String token = jwtHelper.generateToken(userDetails);
//
//        // Create and return JWT response
//        JwtResponse response = new JwtResponse(token, userDetails.getUsername(), userDetails.getAuthorities());
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
@PostMapping("/login/doctor")
public ResponseEntity<JwtResponse> loginDoctor(@RequestBody JwtRequest request) {
    // Authenticate user
    this.doAuthenticate(request.getEmail(), request.getPassword());

    // Load user details
    UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

    // Generate JWT token
    String token = jwtHelper.generateToken(userDetails);

    // Create and return JWT response
    JwtResponse response = JwtResponse.builder()
            .jwtToken(token)
            .username(userDetails.getUsername())
//            .userId(userDetails.getUserId().toString())
            .refreshToken("")  // Handle refreshToken as per your logic
            .build();

    return new ResponseEntity<>(response, HttpStatus.OK);
}


    //    @PostMapping("/healthOfficer")
//    public ResponseEntity<JwtResponse> loginHealthOfficer(@RequestBody JwtRequest request) {
//        // Authenticate user
//        this.doAuthenticate(request.getEmail(), request.getPassword());
//
//        // Load user details
//        UserDetails userDetails = userService.loadUserByUsername(request.getEmail());
//
//        // Generate JWT token
//        String token = jwtHelper.generateToken(userDetails);
//
//        // Create and return JWT response
//        JwtResponse response = new JwtResponse(token, userDetails.getUsername(), userDetails.getAuthorities());
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//
// do authentication of the user
private void doAuthenticate(String email, String password) {

    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
    try {
        manager.authenticate(authentication);
    }catch (BadCredentialsException e){
        throw new BadCredentialsException("Invalid Username or Password !!");
    }
}
}
