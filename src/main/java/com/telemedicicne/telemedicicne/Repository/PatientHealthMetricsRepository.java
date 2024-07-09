package com.telemedicicne.telemedicicne.Repository;

import com.telemedicicne.telemedicicne.Entity.Patient;
import com.telemedicicne.telemedicicne.Entity.PatientHealthMetrics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface PatientHealthMetricsRepository extends JpaRepository<PatientHealthMetrics, Long> {
    // You can add custom query methods if needed
    Optional<PatientHealthMetrics> findByPatientPatientId(Long patientId);
//    List<PatientHealthMetrics> findByPatient_PatientIdAndDocHs_DocHsId(Long patientId, Long docHsId);
    PatientHealthMetrics findTopByOrderByLocalDateDesc();
    PatientHealthMetrics findTopByPatientAndLocalDateOrderByPatientIddDesc(Patient patient, LocalDate localDate);
    PatientHealthMetrics findByPatient(Patient patient);

    Optional<PatientHealthMetrics> findByPatientPatientIdAndHealthOfficerRolesName(Long patientId, String roleName);
    PatientHealthMetrics findTopByPatientOrderByLocalDateDesc(Patient patient);

//    PatientHealthMetrics findByPrescriptions(Prescription prescription);
//List<PatientHealthMetrics> findByPatientPatientIdAndPatientPrescriptions(Long patientId, Prescription prescription);

}
