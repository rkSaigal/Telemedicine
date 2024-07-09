package com.telemedicicne.telemedicicne.Repository;

import com.telemedicicne.telemedicicne.Entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
}
