package com.airportapp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Flight {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String flightNumber;
    private LocalDateTime departureTime, arrivalTime;
    @ManyToOne private Aircraft aircraft;
    @ManyToOne private Airport origin;
    @ManyToOne private Airport destination;
    // getters/setters...
}