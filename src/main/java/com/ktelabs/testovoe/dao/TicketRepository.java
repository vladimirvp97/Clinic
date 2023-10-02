package com.ktelabs.testovoe.dao;

import com.ktelabs.testovoe.model.Patient;
import com.ktelabs.testovoe.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    @Query("SELECT t FROM Ticket t WHERE t.date BETWEEN :start_date AND :end_date AND t.doctorId.specialization = :spec AND t.patientId IS NULL")
    List<Ticket> findFreeTickets(
            @Param("start_date") LocalDateTime startDate,
            @Param("end_date") LocalDateTime endDate,
            @Param("spec") String spec
    );


    @Modifying
    @Query("UPDATE Ticket t SET t.patientId = :patient WHERE t.id = :id_of_ticket")
    int occupyTicket(@Param("patient") Patient patient, @Param("id_of_ticket") Integer idOfTicket);

    @Query("SELECT t FROM Ticket t WHERE t.patientId.id = :patientId")
    List<Ticket> findByPatientId(@Param("patientId") Integer patientId);

}