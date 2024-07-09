package com.telemedicicne.telemedicicne.Dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class PatientCaseHistoryDto {
    private Long patientId;
    private String patientName;
    private String doctorName;
    private String complaint;
    private LocalDate localDate;
    private boolean status;


    private List<PrescriptionDTO> prescriptions;
//    private List<PatientHealthMetricsDTO> healthMetrics;
//    private List<PatientHealthMetricsDTO> healthMetrics;

//    private LocalDate date;
//    private String patientComplaint;
//    private String prescription;
}