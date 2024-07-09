package com.telemedicicne.telemedicicne.Service;

import com.telemedicicne.telemedicicne.Config.AppConstants;
import com.telemedicicne.telemedicicne.Entity.AllToggle;
import com.telemedicicne.telemedicicne.Entity.Role;
import com.telemedicicne.telemedicicne.Entity.Hospital;
import com.telemedicicne.telemedicicne.Exception.RegistrationException;
import com.telemedicicne.telemedicicne.Exception.UserAlreadyExistsException;
import com.telemedicicne.telemedicicne.Repository.AllToggleRepository;
import com.telemedicicne.telemedicicne.Repository.RoleRepo;
import com.telemedicicne.telemedicicne.Repository.UserRepository;
import com.telemedicicne.telemedicicne.Request.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private AllToggleRepository allToggleRepository;

    // for registering the user in application
    public Hospital registerUser(RegistrationRequest request) {
        // Check if the user with the provided email already exists
        Optional<Hospital> existingUser = userRepository.findByEmail(request.email());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("User with email " + request.email() + " already exists");
        }

        // Check registration term condition
        if (!request.registrationTermCondition()) {
            throw new RegistrationException("Registration terms and conditions must be accepted.");
        }

        // Proceed with user registration
        Hospital newUser = new Hospital();
        newUser.setMobileNo(request.mobileNo());
        newUser.setUserName(request.userName());
        newUser.setEmail(request.email());

        newUser.setPassword(passwordEncoder.encode(request.password()));

        // Assign a role to the user (adjust as needed)
        Role role = roleRepo.findById(AppConstants.SUB_ADMIN).orElseThrow();
        newUser.getRoles().add(role);

        // Save the user
        Hospital savedUser = userRepository.save(newUser);

        // Update AllToggle with registrationTermCondition
        Optional<AllToggle> allToggleOptional = Optional.ofNullable(allToggleRepository.findByUser(savedUser));
        AllToggle allToggle;
        if (allToggleOptional.isPresent()) {
            allToggle = allToggleOptional.get();
        } else {
            allToggle = new AllToggle(true, request.registrationTermCondition(), savedUser);
        }
        allToggle.setRegistration_term_condition(request.registrationTermCondition());
        allToggleRepository.save(allToggle);

        return savedUser;
    }
    public Hospital findByUsername(String username) {
        Optional<Hospital> userOptional = userRepository.findByEmail(username);
        return userOptional.orElse(null); // Return null if not found, or handle differently if needed
    }
}
