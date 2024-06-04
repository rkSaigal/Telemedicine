package com.telemedicicne.telemedicicne.Service;

import com.telemedicicne.telemedicicne.Entity.DocHs;
import com.telemedicicne.telemedicicne.Entity.Patient.Patient;
import com.telemedicicne.telemedicicne.Entity.Patient.PatientRepository;
import com.telemedicicne.telemedicicne.Entity.User;
import com.telemedicicne.telemedicicne.Repository.DocHSRepository;
import com.telemedicicne.telemedicicne.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Primary

public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DocHSRepository docHsRepository;
    @Autowired
    private PatientRepository patientRepository;


//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        Optional<User> userOptional = userRepository.findByEmail(email);
//        if (userOptional.isPresent()) {
//            return userOptional.get();
//        } else {
//            Optional<DocHs> docHsOptional = docHsRepository.findByEmail(email);
//            if (docHsOptional.isPresent()) {
//                return docHsOptional.get();
//            } else {
//                throw new UsernameNotFoundException("User not found with email: " + email);
//            }
//        }
//    }
@Override
public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Optional<User> userOptional = userRepository.findByEmail(email);
    if (userOptional.isPresent()) {
        return userOptional.get();
    } else {
        Optional<DocHs> docHsOptional = docHsRepository.findByEmail(email);
        if (docHsOptional.isPresent()) {
            return docHsOptional.get();
        } else {
            Optional<Patient> patientOptional = patientRepository.findByMobileNo(email);
            if (patientOptional.isPresent()) {
                return patientOptional.get();
            } else {
                throw new UsernameNotFoundException("User not found with email: " + email);
            }
        }
    }
}
}
