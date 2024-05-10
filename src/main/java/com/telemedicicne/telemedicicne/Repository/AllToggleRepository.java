package com.telemedicicne.telemedicicne.Repository;


import com.telemedicicne.telemedicicne.Entity.AllToggle;
import com.telemedicicne.telemedicicne.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AllToggleRepository extends JpaRepository<AllToggle, Long> {
    AllToggle findByUser(User user);

    Optional<AllToggle> findByUserEmail(String email);
}
