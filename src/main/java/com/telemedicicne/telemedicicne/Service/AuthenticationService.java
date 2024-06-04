package com.telemedicicne.telemedicicne.Service;


import com.telemedicicne.telemedicicne.Entity.Patient.Patient;
import com.telemedicicne.telemedicicne.Entity.Patient.PatientRepository;
import com.telemedicicne.telemedicicne.Repository.DocHSRepository;
import com.telemedicicne.telemedicicne.Entity.DocHs;
import com.telemedicicne.telemedicicne.Entity.User;
import com.telemedicicne.telemedicicne.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DocHSRepository docHSRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PatientRepository patientRepository;

//    public UserDetails authenticate(String email, String password) throws AuthenticationException {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(email, password)
//        );
//
//        Optional<User> userOptional = userRepository.findByEmail(email);
//        if (userOptional.isPresent()) {
//            return userDetailsService.loadUserByUsername(email);
//        }
//
//        Optional<DocHs> docHsOptional = docHSRepository.findByEmail(email);
//        if (docHsOptional.isPresent()) {
//            return userDetailsService.loadUserByUsername(email);
//        }
//
//        throw new RuntimeException("User not found");
//    }
public UserDetails authenticate(String email, String password) throws AuthenticationException {
    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(email, password)
    );

    Optional<User> userOptional = userRepository.findByEmail(email);
    if (userOptional.isPresent()) {
        return userDetailsService.loadUserByUsername(email);
    }

    Optional<DocHs> docHsOptional = docHSRepository.findByEmail(email);
    if (docHsOptional.isPresent()) {
        return userDetailsService.loadUserByUsername(email);
    }

    Optional<Patient> patientOptional = patientRepository.findByMobileNo(email);
    if (patientOptional.isPresent()) {
        return userDetailsService.loadUserByUsername(email);
    }

    throw new RuntimeException("User not found");
}
}
