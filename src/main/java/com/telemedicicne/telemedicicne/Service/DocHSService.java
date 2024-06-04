package com.telemedicicne.telemedicicne.Service;

// DocHSService.java




import com.telemedicicne.telemedicicne.Entity.DocHs;
import com.telemedicicne.telemedicicne.Entity.Role;
import com.telemedicicne.telemedicicne.Entity.User;
import com.telemedicicne.telemedicicne.Repository.DocHSRepository;
import com.telemedicicne.telemedicicne.Repository.RoleRepo;
import com.telemedicicne.telemedicicne.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class DocHSService {

    @Autowired
    private DocHSRepository docHSRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private RoleRepo roleRepository; // Assuming you have a RoleRepository to fetch roles

//old
//public void registerDocHS(DocHs docHS) {
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
public void registerDocHS(DocHs docHS) {
    // Check if a user with the same email already exists
    Optional<User> existingUser = userRepository.findByEmail(docHS.getEmail());
    if (existingUser.isPresent()) {
        throw new IllegalArgumentException("User with email " + docHS.getEmail() + " already exists");
    }

//    newUser.setPassword(passwordEncoder.encode(request.password()));

    // Encode the password
    String encodedPassword = passwordEncoder.encode(docHS.getPassword());
    docHS.setPassword(encodedPassword); // Set the encoded password to DocHs entity

    docHS.setType(docHS.getType());
    // Determine the role name based on the type
    String roleName = docHS.getType().equals("DOCTOR") ? "DOCTOR" : "HEALTH_OFFICER";
    Role role = roleRepository.findByName(roleName);

    if (role != null) {
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        docHS.setRoles(roles);
    } else {
        throw new IllegalArgumentException("Role not found: " + roleName);
    }

    // Save the new DocHs entity
    docHSRepository.save(docHS);
}

    public DocHs findByUsername(String username) {
        Optional<DocHs> userOptional = docHSRepository.findByEmail(username);
        return userOptional.orElse(null); // Return null if not found, or handle differently if needed
    }
}
