package com.telemedicicne.telemedicicne.Entity;



import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
//@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    public int id;
    public String name;

    public Role(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return this.name; // assuming 'name' is the field that holds the role name

    }
}
