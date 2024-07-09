package com.telemedicicne.telemedicicne.Repository;


import com.telemedicicne.telemedicicne.Entity.AllToggle;
import com.telemedicicne.telemedicicne.Entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AllToggleRepository extends JpaRepository<AllToggle, Long> {
    AllToggle findByUser(Hospital user);

    Optional<AllToggle> findByUserEmail(String email);
}
