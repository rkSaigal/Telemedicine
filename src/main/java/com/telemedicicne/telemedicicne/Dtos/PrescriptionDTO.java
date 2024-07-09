package com.telemedicicne.telemedicicne.Dtos;



import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class PrescriptionDTO {
    private Long id;

    private List<MedicationDTO> medications;
    private List<LaboratoryDTO> laboratory;


//    private String medication;
//    private String frequency;
//    private String dosage;
//    private int days;
//    private String laboratory;


    private String chiefComplaints;
    private String symptoms;
    private String advice;
    private String provisionalDiagnosis;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate followUpDate;



    // Additional fields from PatientHealthMetrics
    private String allergy;
    private String comorbidity;
    private String complaints;
    private List<String> otherIllnesses; // Assuming this is a list of strings for other illnesses


    // Constructor with required fields
    // Constructor with required fields
    public PrescriptionDTO(Long id, List<MedicationDTO> medications, String chiefComplaints,
                           String symptoms, String advice, String provisionalDiagnosis,
                           LocalDate followUpDate, String allergy, String comorbidity,
                           String complaints, List<String> otherIllnesses) {
        this.id = id;
        this.medications = medications;
        this.chiefComplaints = chiefComplaints;
        this.symptoms = symptoms;
        this.advice = advice;
        this.provisionalDiagnosis = provisionalDiagnosis;
        this.followUpDate = followUpDate;
        this.allergy = allergy;
        this.comorbidity = comorbidity;
        this.complaints = complaints;
        this.otherIllnesses = otherIllnesses;
    }

    public PrescriptionDTO() {

    }
}
