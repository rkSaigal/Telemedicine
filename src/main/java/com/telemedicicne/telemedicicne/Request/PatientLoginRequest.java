package com.telemedicicne.telemedicicne.Request;


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
