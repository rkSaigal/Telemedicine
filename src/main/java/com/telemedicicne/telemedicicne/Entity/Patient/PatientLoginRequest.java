package com.telemedicicne.telemedicicne.Entity.Patient;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PatientLoginRequest {

    private String mobileNo;
    private String password;
}
