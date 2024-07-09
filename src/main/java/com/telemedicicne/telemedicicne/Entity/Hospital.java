package com.telemedicicne.telemedicicne.Entity;

import com.fasterxml.jackson.annotation.*;

import jakarta.persistence.*;
import lombok.*;

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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "userId")
@Entity
public class Hospital implements UserDetails {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String userName;

    private String email;
    private String password;
    private String mobileNo;
//    private Double latitude;
//    private Double longitude;
    private String address;

    private LocalDate localDate = LocalDate.now();
    private boolean emailVerified = true;
    private String notificationToken;

    @OneToOne(mappedBy = "user")
    @JsonIgnore
    private RefreshToken refreshToken;

    @Column(name = "registration_timestamp")
    private LocalDateTime registrationTimestamp = LocalDateTime.now();

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<VerificationToken> verificationTokens = new ArrayList<>();
@OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL)
private Set<HealthOfficer> registeredDocHs = new HashSet<>();

    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL)
    private Set<Doctor> doctors = new HashSet<>();



    @ManyToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<SimpleGrantedAuthority> authories = this.roles.stream()
                .map((role)-> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
        return authories;
    }
    public String getUserName() {
        return this.userName;
    }

    @Override
    public String getUsername() {
        return this.email;
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
        return this.emailVerified;
    }



}
