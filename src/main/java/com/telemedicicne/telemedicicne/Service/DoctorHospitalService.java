package com.telemedicicne.telemedicicne.Service;

// DocHSService.java

import com.telemedicicne.telemedicicne.Entity.Doctor;
import com.telemedicicne.telemedicicne.Entity.HealthOfficer;
import com.telemedicicne.telemedicicne.Entity.Role;
import com.telemedicicne.telemedicicne.Entity.Hospital;
import com.telemedicicne.telemedicicne.Repository.DoctorRepository;
import com.telemedicicne.telemedicicne.Repository.HealthOfficerRepository;
import com.telemedicicne.telemedicicne.Repository.RoleRepo;
import com.telemedicicne.telemedicicne.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class DoctorHospitalService {

    @Autowired
    private HealthOfficerRepository healthOfficerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private RoleRepo roleRepository; // Assuming you have a RoleRepository to fetch roles

//old
//public void registerDocHS(HealthOfficer docHS) {
//    // Determine the role name based on the type
//    String roleName = docHS.getType().equals("DOCTOR") ? "DOCTOR" : "HEALTH_OFFICER";
//    Role role = roleRepository.findByName(roleName);
//
//    if (role != null) {
//        Set<Role> roles = new HashSet<>();
//        roles.add(role);
//        docHS.setRoles(roles);
//    } else {
//        throw new IllegalArgumentException("Role not found: " + roleName);
//    }
//
//    docHSRepository.save(docHS);
//}
//public void registerDocHS(HealthOfficer docHS) {
//    // Check if a user with the same email already exists
//    Optional<User> existingUser = userRepository.findByEmail(docHS.getEmail());
//    if (existingUser.isPresent()) {
//        throw new IllegalArgumentException("User with email " + docHS.getEmail() + " already exists");
//    }
//
////    newUser.setPassword(passwordEncoder.encode(request.password()));
//
//    // Encode the password
//    String encodedPassword = passwordEncoder.encode(docHS.getPassword());
//    docHS.setPassword(encodedPassword); // Set the encoded password to HealthOfficer entity
//
//    docHS.setType(docHS.getType());
//    // Determine the role name based on the type
//    String roleName = docHS.getType().equals("DOCTOR") ? "DOCTOR" : "HEALTH_OFFICER";
//    Role role = roleRepository.findByName(roleName);
//
//    if (role != null) {
//        Set<Role> roles = new HashSet<>();
//        roles.add(role);
//        docHS.setRoles(roles);
//    } else {
//        throw new IllegalArgumentException("Role not found: " + roleName);
//    }
//
//    // Save the new HealthOfficer entity
//    docHSRepository.save(docHS);
//}


    public void save(HealthOfficer docHs) {
        healthOfficerRepository.save(docHs);
    }
    public void registerDocHS(HealthOfficer healthOfficer, String userType, Hospital hospital) {
    // Check if a user with the same email already exists
    Optional<Hospital> existingUser = userRepository.findByEmail(healthOfficer.getEmail());
    if (existingUser.isPresent()) {
        throw new IllegalArgumentException("User with email " + healthOfficer.getEmail() + " already exists");
    }

    // Encode the password
    String encodedPassword = passwordEncoder.encode(healthOfficer.getPassword());
    healthOfficer.setPassword(encodedPassword);

    // Set the user type to the provided value
    healthOfficer.setType(userType);
        // Set the relationship
        healthOfficer.setHospital(hospital);
    // Determine the role name based on the userType
    String roleName = userType.equals("Doctor") ? "DOCTOR" : "HEALTH_OFFICER";
    Role role = roleRepository.findByName(roleName);

    if (role != null) {
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        healthOfficer.setRoles(roles);
    } else {
        throw new IllegalArgumentException("Role not found: " + roleName);
    }

    // Save the new HealthOfficer entity
    healthOfficerRepository.save(healthOfficer);
}


    public void registerUser(Object user, String userType, Hospital hospital) {
        if (userType.equals("Doctor")) {
            Doctor doctor = (Doctor) user;
            registerDoctor(doctor, hospital);
        } else if (userType.equals("Health Officer")) {
            HealthOfficer healthOfficer = (HealthOfficer) user;
            registerHealthOfficer(healthOfficer, hospital);
        } else {
            throw new IllegalArgumentException("Invalid user type: " + userType);
        }
    }

    private void registerDoctor(Doctor doctor, Hospital hospital) {
        if (doctorRepository.findByEmail(doctor.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User with email " + doctor.getEmail() + " already exists");
        }

        doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
        doctor.setType("Doctor");
        doctor.setHospital(hospital);

        Role role = roleRepository.findByName("DOCTOR");
        if (role != null) {
            doctor.setRoles(Set.of(role));
        } else {
            throw new IllegalArgumentException("Role not found: DOCTOR");
        }

        doctorRepository.save(doctor);
    }

    private void registerHealthOfficer(HealthOfficer healthOfficer, Hospital hospital) {
        if (healthOfficerRepository.findByEmail(healthOfficer.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User with email " + healthOfficer.getEmail() + " already exists");
        }

        healthOfficer.setPassword(passwordEncoder.encode(healthOfficer.getPassword()));
        healthOfficer.setType("Health Officer");
        healthOfficer.setHospital(hospital);

        Role role = roleRepository.findByName("HEALTH_OFFICER");
        if (role != null) {
            healthOfficer.setRoles(Set.of(role));
        } else {
            throw new IllegalArgumentException("Role not found: HEALTH_OFFICER");
        }

        healthOfficerRepository.save(healthOfficer);
    }

    public HealthOfficer findByUsername(String username) {
        Optional<HealthOfficer> userOptional = healthOfficerRepository.findByEmail(username);
        return userOptional.orElse(null); // Return null if not found, or handle differently if needed
    }

    public Doctor findByUsernameDoctor(String username) {
        Optional<Doctor> userOptional = doctorRepository.findByEmail(username);
        return userOptional.orElse(null); // Return null if not found, or handle differently if needed
    }

    public List<Doctor> findAllDoctors() {
        // Implement logic to fetch all users with the DOCTOR role
        return doctorRepository.findAll();
    }

    public HealthOfficer findByRefId(Long refId) {
        return healthOfficerRepository.findByDocHsId(refId);
    }
}