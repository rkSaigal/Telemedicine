package com.telemedicicne.telemedicicne.Dtos;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null fields from JSON

public class PatientHealthMetricsDTO {

private Long id;
    private LocalDate localDate;

    private String bloodGroup;
    private Double spO2;
    private Integer respirationRate;
    private Double heartRate;
    private Integer diastolicBP;
    private Integer systolicBP;
    private Integer pulseRate;
    private Double temperature;
    private Double hemoglobin;
    private Double bloodSugar;

    private String comorbidity;
    private String complaints;


    private String document;
private String doctor;
    private String allergy;
    private String otherIllness;
    private List<DocumentDTO> documents; // Add this line

    private MultipartFile[] files; // Add this line for file uploads


    // Constructor with required fields
    public PatientHealthMetricsDTO(Long id,String bloodGroup, Double spO2, Integer respirationRate, Double heartRate,
                                   Integer diastolicBP, Integer systolicBP, Integer pulseRate, Double temperature,
                                   Double hemoglobin, Double bloodSugar) {
       this.id = id;
        this.bloodGroup = bloodGroup;
        this.spO2 = spO2;
        this.respirationRate = respirationRate;
        this.heartRate = heartRate;
        this.diastolicBP = diastolicBP;
        this.systolicBP = systolicBP;
        this.pulseRate = pulseRate;
        this.temperature = temperature;
        this.hemoglobin = hemoglobin;
        this.bloodSugar = bloodSugar;
    }


    // Constructor with all fields
    public PatientHealthMetricsDTO(String bloodGroup, Double spO2, Integer respirationRate, Double heartRate,
                                         Integer diastolicBP, Integer systolicBP, Integer pulseRate, Double temperature,
                                         Double hemoglobin, Double bloodSugar) {
        this.bloodGroup = bloodGroup;
        this.spO2 = spO2;
        this.respirationRate = respirationRate;
        this.heartRate = heartRate;
        this.diastolicBP = diastolicBP;
        this.systolicBP = systolicBP;
        this.pulseRate = pulseRate;
        this.temperature = temperature;
        this.hemoglobin = hemoglobin;
        this.bloodSugar = bloodSugar;
    }

    public PatientHealthMetricsDTO(Long id, String comorbidity, String complaints, String allergy, String otherIllness) {


        this.id = id;
        this.comorbidity = comorbidity;
        this.complaints = complaints;
        this.allergy = allergy;
        this.otherIllness = otherIllness;


    }
}
