package com.telemedicicne.telemedicicne.Service;

import com.telemedicicne.telemedicicne.Entity.Doctor;
import com.telemedicicne.telemedicicne.Entity.HealthOfficer;
import com.telemedicicne.telemedicicne.Entity.Patient;
import com.telemedicicne.telemedicicne.Repository.PatientRepository;
import com.telemedicicne.telemedicicne.Entity.Hospital;
import com.telemedicicne.telemedicicne.Repository.DoctorRepository;
import com.telemedicicne.telemedicicne.Repository.HealthOfficerRepository;
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
    private HealthOfficerRepository docHsRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private DoctorRepository doctorRepository;


//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        Optional<User> userOptional = userRepository.findByEmail(email);
//        if (userOptional.isPresent()) {
//            return userOptional.get();
//        } else {
//            Optional<HealthOfficer> docHsOptional = docHsRepository.findByEmail(email);
//            if (docHsOptional.isPresent()) {
//                return docHsOptional.get();
//            } else {
//                throw new UsernameNotFoundException("User not found with email: " + email);
//            }
//        }
//    }
//@Override
//public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//    Optional<Hospital> userOptional = userRepository.findByEmail(email);
//    if (userOptional.isPresent()) {
//        return userOptional.get();
//    } else {
//        Optional<HealthOfficer> docHsOptional = docHsRepository.findByEmail(email);
//        if (docHsOptional.isPresent()) {
//            return docHsOptional.get();
//        } else {
//            Optional<Patient> patientOptional = patientRepository.findByMobileNo(email);
//            if (patientOptional.isPresent()) {
//                return patientOptional.get();
//            } else {
//                throw new UsernameNotFoundException("User not found with email: " + email);
//            }
//        }
//    }
//}
@Override
public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Optional<Hospital> userOptional = userRepository.findByEmail(email);
    if (userOptional.isPresent()) {
        return userOptional.get();
    } else {
        Optional<HealthOfficer> docHsOptional = docHsRepository.findByEmail(email);
        if (docHsOptional.isPresent()) {
            return docHsOptional.get();
        } else {
            Optional<Patient> patientOptional = patientRepository.findByMobileNo(email);
            if (patientOptional.isPresent()) {
                return patientOptional.get();
            } else {
                Optional<Doctor> doctorOptional = doctorRepository.findByEmail(email);
                if (doctorOptional.isPresent()) {
                    return doctorOptional.get();
                } else {
                    throw new UsernameNotFoundException("User not found with email: " + email);
                }
            }
        }
    }
}

}
