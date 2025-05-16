package com.airportapp.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class Ticket {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String seatNumber;
    private BigDecimal price;
    @ManyToOne private Flight flight;
    @ManyToOne private Passenger passenger;
    // getters/setters...
}