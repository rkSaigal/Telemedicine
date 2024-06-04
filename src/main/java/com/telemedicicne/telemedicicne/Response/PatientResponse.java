package com.telemedicicne.telemedicicne.Response;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientResponse {
    private Long patientId;
    private String patientName;
    private String addharNo;
    private Double height;
    private Double weight;
    private Double age;
    private String gender;
    private String email;
    private String mobileNo;
    private String address;
    private LocalDate localDate;
    private LocalDateTime registrationTimestamp;
}
