package com.ktelabs.testovoe.model;

import jakarta.persistence.*;
import lombok.Data;


import java.time.LocalDateTime;

@Data
@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patientId;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctorId;
}
