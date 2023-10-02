package com.ktelabs.testovoe.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "uuid", insertable = false, updatable = false)
    private UUID uuid;

    private String lastName;

    private String firstName;

    private String fatherName;

    public Patient(String firstName, String lastName, String fatherName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.fatherName = fatherName;
    }


}
