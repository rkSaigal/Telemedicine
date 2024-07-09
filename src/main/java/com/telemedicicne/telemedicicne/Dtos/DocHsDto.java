package com.telemedicicne.telemedicicne.Dtos;



import com.telemedicicne.telemedicicne.Entity.Hospital;
import com.telemedicicne.telemedicicne.Entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DocHsDto {
    private Long docHsId;
    private String name;
    private String email;
    private String mobileNo;
    private String specialist;
    private String type;
    private String videoToken;
    private Long refId;
    private Set<Role> roles;
    private Set<Hospital> hospitals; // Corrected field definition
    // Setter for hospitals
    public void setHospitals(Set<Hospital> hospitals) {
        this.hospitals = hospitals;
    }
}
