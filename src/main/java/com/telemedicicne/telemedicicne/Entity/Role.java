package com.telemedicicne.telemedicicne.Entity;



import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Role {
    @Id
    public int id;
    public String name;
}
