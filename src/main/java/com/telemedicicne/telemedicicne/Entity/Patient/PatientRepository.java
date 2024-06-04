package com.telemedicicne.telemedicicne.Entity.Patient;




import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByEmail(String email);
    Optional<Patient> findByAddharNo(String addharNo);

    Optional<Patient> findByMobileNo(String email);
}
