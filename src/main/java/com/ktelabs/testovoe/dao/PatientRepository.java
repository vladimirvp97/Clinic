package com.ktelabs.testovoe.dao;

import com.ktelabs.testovoe.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {
    Optional<Patient> findFirstByFirstNameAndLastNameAndFatherName(String FirstName, String LastName, String FatherName);
}
