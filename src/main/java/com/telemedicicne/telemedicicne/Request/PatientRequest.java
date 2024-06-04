package com.telemedicicne.telemedicicne.Request;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientRequest {
    private String patientName;
    private String addharNo;
    private Double height;
    private Double weight;
    private Double age;
    private String gender;
    private String email;
    private String password;
    private String mobileNo;
    private String address;
}
