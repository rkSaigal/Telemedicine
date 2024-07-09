//package com.telemedicicne.telemedicicne.Entity.Patient;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.telemedicicne.telemedicicne.Entity.Doctor;
//import com.telemedicicne.telemedicicne.Entity.HealthOfficer;
//import com.telemedicicne.telemedicicne.Entity.RefreshToken;
//import com.telemedicicne.telemedicicne.Entity.Role;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import org.hibernate.annotations.NaturalId;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
////@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "userId")
//@Entity
//
//public class Patient implements UserDetails {
//
//
//    @Id
////    @Column(name = "u_id")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long patientId;
//    private String patientName;
////    private String firstName;
////    private String lastName;
//    private String addharNo;
//    private Double height;
//    private Double weight;
//    private Double age;
//    private String gender;
//
//    //bp,spo2/bs/
//    private String email; // not mendentory
//    private String password;
//
//    private String otpCode;
//    private LocalDateTime otpExpiration;
//
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "doc_hs_id")
//    private HealthOfficer healthOfficer;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "doctor_id")
//    private Doctor doctor;
//
//
//    @NaturalId(mutable = true)
//    @Column(unique = true) // Ensure uniqueness
//    private String mobileNo;
//    //    private Double latitude;c
////    private Double longitude;
//    private String address;
//    private boolean status = false;
//    private LocalDate localDate = LocalDate.now();
////    private boolean emailVerified = true;
////    private String notificationToken;
//
//    @OneToOne(mappedBy = "patient")
//    @JsonIgnore
//    private RefreshToken refreshToken;
//
//    @Column(name = "registration_timestamp")
//    private LocalDateTime registrationTimestamp = LocalDateTime.now();
//
////    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
////    private List<VerificationToken> verificationTokens = new ArrayList<>();
//
//
//
//    @ManyToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
//    private Set<Role> roles = new HashSet<>();
//
//
//
//    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<PatientHealthMetrics> healthMetrics = new ArrayList<>();
//
//    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Prescription> prescriptions;
////    @Override
////    public Collection<? extends GrantedAuthority> getAuthorities() {
////        return List.of();
////    }
//
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return roles.stream()
//                .map(role -> new SimpleGrantedAuthority(role.getName()))
//                .collect(Collectors.toList());
//    }
//
//
//    @Override
//    public String getPassword() {
//        return this.password;
//    }
//
//    @Override
//    public String getUsername() {
//        return this.mobileNo;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true; // Implement logic for account expiration if needed
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true; // Implement logic for account locking if needed
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true; // Implement logic for credentials expiration if needed
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true; // Implement logic for enabled/disabled accounts
//    }
//
//
//    // Getter for status
//    public boolean getStatus() {
//        return this.status;
//    }
//}
package com.telemedicicne.telemedicicne.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telemedicicne.telemedicicne.Entity.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Patient implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long patientId;

    private String patientName;
    private String addharNo;
    private Double height;
    private Double weight;
    private Double age;
    private String gender;
    private String email; // not mandatory
    private String password;
    private String otpCode;
    private LocalDateTime otpExpiration;

    private boolean accepted;
    private LocalDateTime requestTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_hs_id")
    private HealthOfficer healthOfficer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @NaturalId(mutable = true)
    @Column(unique = true)
    private String mobileNo;

    private String address;
    private boolean status = false;
    private LocalDate localDate = LocalDate.now();

    @OneToOne(mappedBy = "patient")
    @JsonIgnore
    private RefreshToken refreshToken;

    @Column(name = "registration_timestamp")
    private LocalDateTime registrationTimestamp = LocalDateTime.now();

    @ManyToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PatientHealthMetrics> healthMetrics = new ArrayList<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Prescription> prescriptions;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.mobileNo;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean getStatus() {
        return this.status;
    }
}
