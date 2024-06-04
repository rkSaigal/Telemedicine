package com.telemedicicne.telemedicicne.Entity.Patient;


import com.telemedicicne.telemedicicne.Entity.Role;
import com.telemedicicne.telemedicicne.Repository.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

//    public void registerPatient(Patient patient) {
//        // Additional logic for patient registration, if needed
//
//        // Example: You might want to validate input fields, check for existing patients, etc.
//        patientRepository.save(patient);
//    }
@Autowired
private RoleRepo roleRepo;

    public void registerPatient(Patient patient) {
        // Assign the PATIENT role to the new patient
        Role patientRole = roleRepo.findByName("PATIENT");
        if (patientRole == null) {
            throw new IllegalArgumentException("Role not found: PATIENT");
        }
        Set<Role> roles = new HashSet<>();
        roles.add(patientRole);
        patient.setRoles(roles);

        // Save the new Patient entity
        patientRepository.save(patient);
    }
}
