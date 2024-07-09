package com.telemedicicne.telemedicicne.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.telemedicicne.telemedicicne.Entity.Doctor;
import com.telemedicicne.telemedicicne.Entity.Document;
import com.telemedicicne.telemedicicne.Entity.HealthOfficer;
import com.telemedicicne.telemedicicne.Entity.Patient;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "userId")
@Entity

public class PatientHealthMetrics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate localDate;
    private Integer respirationRate;
    private String bloodGroup;
    private Double spO2;
    private Double heartRate;
    private Integer diastolicBP;
    private Integer systolicBP;
    private Integer pulseRate;
    private Double temperature;
    private Double hemoglobin;
    private String comorbidity;
    private String complaints;

    private Double bloodSugar;

    private String allergy;

    private String otherIllness;

    private Long patientIdd;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    @JsonIgnore
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_hs_id")
    private HealthOfficer healthOfficer;

    @OneToMany(mappedBy = "patientHealthMetrics", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Document> documents = new ArrayList<>();
}
