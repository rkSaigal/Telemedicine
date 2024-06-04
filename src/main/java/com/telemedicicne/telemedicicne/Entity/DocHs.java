package com.telemedicicne.telemedicicne.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "userId")
@Entity

public class DocHs implements UserDetails {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long docHsId;

    private String name;
    @NaturalId(mutable = true)
    @Column(unique = true) // Ensure uniqueness

    private String email;
    private String mobileNo;
    private String specialist;
    private String password;
    private String type;//HS or DOC


    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();
//    @ManyToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
//    private Set<Role> roles = new HashSet<>();
//@ManyToMany
//@JoinTable(
//        name = "doc_hs_roles",
//        joinColumns = @JoinColumn(name = "doc_hs_id"),
//        inverseJoinColumns = @JoinColumn(name = "role_id")
//)
//private Set<Role> roles = new HashSet<>();

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return List.of();
//    }
@Override
public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles.stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toList());
}

//    @Override
//    public String getUsername() {
//        return this.email;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//    @Override
//    public boolean isEnabled() {
//        return false;
//    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Implement logic for account expiration if needed
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Implement logic for account locking if needed
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Implement logic for credentials expiration if needed
    }

    @Override
    public boolean isEnabled() {
        return true; // Implement logic for enabled/disabled accounts
    }

    @OneToOne(mappedBy = "docHs", cascade = CascadeType.ALL)
    private RefreshToken refreshToken;
}
