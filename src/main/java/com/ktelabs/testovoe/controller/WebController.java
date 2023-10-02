package com.ktelabs.testovoe.controller;


import com.ktelabs.testovoe.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
public class WebController {

    @Autowired
    private TicketService ticketService;

    @GetMapping("/")
    public String showForm() {
        return "index";
    }

    @GetMapping("/free-tickets")
    @ResponseBody
    // tickets?startDate=2023-09-23&endDate=2023-09-28&specialization=Hirurg
    public String getFreeTickets(@RequestParam String startDate, @RequestParam String endDate, @RequestParam String specialization) {

        LocalDateTime start = LocalDate.parse(startDate).atStartOfDay();
        LocalDateTime end = LocalDate.parse(endDate).atStartOfDay();

        return ticketService.getFreeTickets(start, end, specialization) + addHomeButton();
    }

    @PostMapping("/take-ticket")
    @ResponseBody
    public String takeTicket(@RequestParam Integer idOfTicket, @RequestParam String firstname,
                             @RequestParam String lastname, @RequestParam String fathername){

        return ticketService.takeTicket(idOfTicket, firstname, lastname, fathername) + addHomeButton();
    }

    @GetMapping("/tickets-by-patient")
    @ResponseBody
    public String getAllTicketsByOnePatient(@RequestParam String firstname,
                                            String lastname, String fathername) {

        return ticketService.getAllTicketsByOnePatient(firstname, lastname, fathername) + addHomeButton();
    }

    private String addHomeButton() {
        return "<br><a href=\"/\" class=\"btn\">Go to Home</a>";
    }
}
