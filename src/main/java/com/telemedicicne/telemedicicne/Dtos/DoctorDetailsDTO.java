package com.telemedicicne.telemedicicne.Dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDetailsDTO {

    private Long id;
    private String name;
    private String email;
    private String mobileNo;
    private String specialist;
    private String type;
    private List<String> roles;
}

