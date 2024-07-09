package com.telemedicicne.telemedicicne.Repository;

import com.telemedicicne.telemedicicne.Entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

//    public Optional<HealthOfficer> findByEmail(String email);
    Optional<Prescription> findByPatientPatientId(Long id);

}
