package com.telemedicicne.telemedicicne.Entity;//package com.telemedicicne.telemedicicne.Entity.Patient;
//
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.util.Date;
//
//
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
////@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "userId")
//@Entity
//public class Prescription {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String chiefComplaints;
//    private String symptoms;
//
//    private String medication;
//    private String frequency;
//    private String dosage;
//    private int days;
//    private String laboratory;
//
//    private String advice;
//    private String provisionalDiagnosis;
//    private Date followUpDate;
//
//
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "patient_id")
//    private Patient patient;
//}





import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String chiefComplaints;
    private String symptoms;
    private String advice;
    private String provisionalDiagnosis;
    private LocalDate followUpDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Medication> medications;

    @OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Laboratory> laboratories;
}
