package com.ktelabs.testovoe.model;

import jakarta.persistence.*;
import lombok.Data;


import java.util.UUID;

@Data
@Entity
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid", insertable = false, updatable = false)
    private UUID uuid;

    private String lastName;

    private String firstName;

    private String fatherName;

    private String specialization;
}
