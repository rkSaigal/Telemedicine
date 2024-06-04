package com.telemedicicne.telemedicicne.Repository;




import com.telemedicicne.telemedicicne.Entity.DocHs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocHSRepository extends JpaRepository<DocHs, Long> {

//    public Optional<DocHs> findByEmail(String email);

    public Optional<DocHs> findByEmail(String email);
}
