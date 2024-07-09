package com.telemedicicne.telemedicicne.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AllToggle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean notificationOn=false;
    private boolean registration_term_condition;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id")
    private Hospital user;

    public AllToggle(boolean notificationOn, boolean registration_term_condition, Hospital user) {
        this.notificationOn = notificationOn;
        this.registration_term_condition = registration_term_condition;
        this.user = user;
    }

    public AllToggle(boolean notificationOn, Hospital user) {
        this.notificationOn = notificationOn;
        this.user = user;
    }
}
