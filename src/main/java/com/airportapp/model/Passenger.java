package com.airportapp.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Passenger {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String firstName, lastName, passportNumber;
    private LocalDate dateOfBirth;
    // getters/setters...
}