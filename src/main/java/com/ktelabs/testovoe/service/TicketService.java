package com.ktelabs.testovoe.service;

import com.ktelabs.testovoe.dao.DoctorRepository;
import com.ktelabs.testovoe.dao.PatientRepository;
import com.ktelabs.testovoe.dao.TicketRepository;
import com.ktelabs.testovoe.model.Doctor;
import com.ktelabs.testovoe.model.Patient;
import com.ktelabs.testovoe.model.Ticket;
import com.ktelabs.testovoe.soap.dto.GetScheduleRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;

@Service
@RequiredArgsConstructor
public class TicketService {

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd HH:mm");

    private final DoctorRepository doctorRepository;

    private final TicketRepository ticketRepository;

    private final PatientRepository patientRepository;

    private String sessionDuration = null;

    public String getFreeTickets(LocalDateTime start, LocalDateTime end, String spec) {
        List<Ticket> tickets = ticketRepository.findFreeTickets(start, end, spec);

        String header = String.format("Free windows to the %s:<br><br>", spec);
        String ticketInfo = tickets.stream()
                .map(ticket -> String.format("ID: %d, Time: %s<br>", ticket.getId(), ticket.getDate().format(dtf)))
                .collect(joining());

        String footer = String.format("<br><br>Time of one session is %s", sessionDuration);

        return header + ticketInfo + footer;
    }
    @Transactional
    public String takeTicket(Integer idOfTicket, String firstName, String lastName, String fatherName) {
        Patient patient = createAndSavePatient(firstName, lastName, fatherName);
        boolean isTicketOccupied = occupyTicket(patient, idOfTicket);

        if (isTicketOccupied) {
            Ticket ticket = ticketRepository.getReferenceById(idOfTicket);
            Doctor doctor = ticket.getDoctorId();
            return String.format(
                    "Successfully registered. We are waiting for you at the clinic. <br>Time: %s, Doctor: %s %s <br>Specialization: %s",
                    ticket.getDate(),
                    doctor.getFirstName(),
                    doctor.getFatherName(),
                    doctor.getSpecialization()
            );
        }
        return "Chosen ticket is no longer available";
    }

    public String getAllTicketsByOnePatient(String firstName, String lastName, String fatherName) {
        return patientRepository.findFirstByFirstNameAndLastNameAndFatherName(firstName, lastName, fatherName)
                .map(patient -> formatTicketInformation(ticketRepository.findByPatientId(patient.getId())))
                .orElse("Patient does not have a registration in that clinic");
    }

    @Transactional
    public void initTickets(GetScheduleRequest getScheduleRequest) {
        LocalDate startDate = LocalDate.parse(getScheduleRequest.getStartSchedule());
        LocalDate endDate = LocalDate.parse(getScheduleRequest.getEndSchedule());

        LocalTime startTime = LocalTime.parse(getScheduleRequest.getStartDay());
        LocalTime endTime = LocalTime.parse(getScheduleRequest.getEndDay());

        int sessionDuration = Integer.parseInt(getScheduleRequest.getAmountMinutesOnSession());

        List<Doctor> doctors = doctorRepository.findAll();
        List<Ticket> ticketsToSave = new ArrayList<>();

        for (Doctor doctor : doctors) {
            generateTicketsForDoctor(doctor, startDate, endDate, startTime, endTime, sessionDuration, ticketsToSave);
        }

        ticketRepository.saveAll(ticketsToSave);
    }

    private Patient createAndSavePatient(String firstName, String lastName, String fatherName) {
        Patient patient = new Patient(firstName, lastName, fatherName);
        patientRepository.save(patient);
        return patient;
    }

    private boolean occupyTicket(Patient patient, Integer idOfTicket) {
        int res = ticketRepository.occupyTicket(patient, idOfTicket);
        return res > 0;
    }

    private String formatTicketInformation(List<Ticket> tickets) {
        StringBuilder sb = new StringBuilder();
        sb.append("All booked tickets for a patient:<br><br>");

        for (Ticket ticket : tickets) {
            Doctor doc = ticket.getDoctorId();
            sb.append("ID: ").append(ticket.getId())
                    .append(", Time: ").append(ticket.getDate().format(dtf))
                    .append(", Doctor: ").append(doc.getFirstName()).append(" ").append(doc.getLastName())
                    .append(", Specialization: ").append(doc.getSpecialization())
                    .append("<br>");
        }

        return sb.toString();
    }


    private void generateTicketsForDoctor(
            Doctor doctor,
            LocalDate startDate,
            LocalDate endDate,
            LocalTime startTime,
            LocalTime endTime,
            int sessionDuration,
            List<Ticket> ticketsToSave
    ) {
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            LocalTime currentTime = startTime;
            while (!currentTime.isAfter(endTime)) {
                LocalDateTime currentDateTime = currentDate.atTime(currentTime);
                Ticket ticket = new Ticket();
                ticket.setDate(currentDateTime);
                ticket.setDoctorId(doctor);
                ticketsToSave.add(ticket);
                currentTime = currentTime.plusMinutes(sessionDuration);
            }
            currentDate = currentDate.plusDays(1);
        }
    }

    // FIXME: save in DB
    public void setSessionDuration(String session) {
        sessionDuration = session;
    }
}

