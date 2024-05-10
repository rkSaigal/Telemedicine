package com.telemedicicne.telemedicicne.Controller;

import com.telemedicicne.telemedicicne.Entity.RefreshToken;
import com.telemedicicne.telemedicicne.Entity.User;
import com.telemedicicne.telemedicicne.Model.JwtRequest;
import com.telemedicicne.telemedicicne.Model.JwtResponse;
import com.telemedicicne.telemedicicne.Repository.UserRepository;
import com.telemedicicne.telemedicicne.Request.RegistrationRequest;
import com.telemedicicne.telemedicicne.Security.JwtHelper;
import com.telemedicicne.telemedicicne.Service.RefreshTokenService;
import com.telemedicicne.telemedicicne.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
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
    private JwtHelper helper;
    @Autowired
    private RefreshTokenService refreshTokenService;
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
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request)
    {
        this.doAuthenticate(request.getEmail(), request.getPassword());

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = this.helper.generateToken(userDetails);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());

        Optional<User> user = userRepository.findByEmail(request.getEmail());

        User usr = user.get();

        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .refreshToken(refreshToken.getRefreshToken())
                .userId(usr.getUserId().toString())
                .username(userDetails.getUsername()).build();
        return new ResponseEntity<>(response, HttpStatus.OK);

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

}