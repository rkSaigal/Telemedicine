package com.telemedicicne.telemedicicne.Dtos;



import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null fields from JSON

public class PatientDetailsDTO {

    private Long patientId;
    private String patientName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate localDate;
    private String doctor;
    private String mobileNo;
    private String hospital;
    private Double height;
    private Double weight;
    private Double age;
    private String gender;
    private String email;
    private boolean status;
    private Long serialNo;



//    private String address;
//    private LocalDate registrationDate;


    // Constructor without docHsName
    public PatientDetailsDTO(Long patientId, String patientName, Double height, Double weight, Double age, String gender, String email, String mobileNo) {
        this.patientId = patientId;
        this.patientName = patientName;
        this.height = height;
        this.weight = weight;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.mobileNo = mobileNo;
    }
    // Constructor with docHsName
//    public PatientDetailsDTO(Long patientId,
//                             String patientName,
//                            LocalDate localDate,
////                             Double height,
////                             Double weight,
////                             Double age,
////                             String gender,
////                             String email,
//                             String mobileNo,
//                             String docHsName) {
//        this(patientId, patientName,  mobileNo, localDate);
//        this.hospital = docHsName;
//    }
    // Constructor
    // Constructor with all fields
    public PatientDetailsDTO(Long patientId, String patientName, LocalDate localDate, boolean status, String mobileNo, String doctor, String hospital) {
        this.patientId = patientId;
        this.patientName = patientName;
        this.localDate = localDate;
        this.status = status;
        this.mobileNo = mobileNo;
        this.doctor = doctor;
        this.hospital = hospital;
    }


    // Constructor with required fields
    public PatientDetailsDTO(Long patientId, String patientName, String email, Long serialNo) {
        this.patientId = patientId;
        this.patientName = patientName;
        this.email = email;
        this.serialNo = serialNo; // Assigning Long serialNo directly
    }
}



