package com.telemedicicne.telemedicicne.Repository;

import com.telemedicicne.telemedicicne.Entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Hospital,Long> {

    public Optional<Hospital> findByEmail(String email);

//    List<User> findByRoles_Name(String roleName);

}
