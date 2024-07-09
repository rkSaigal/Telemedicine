package com.telemedicicne.telemedicicne.Repository;




import com.telemedicicne.telemedicicne.Entity.HealthOfficer;
import com.telemedicicne.telemedicicne.Entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByEmail(String email);
//  /
List<Patient> findByHealthOfficer(HealthOfficer docHs);

    List<Patient> findByHealthOfficerAndLocalDate(HealthOfficer docHs, LocalDate localDate);

    Optional<Patient> findByAddharNo(String addharNo);
//    Optional<Patient> findByAddharNo(String addharNo);
    Optional<Patient> findByMobileNo(String email);
}
