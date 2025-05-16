package com.airportapp.model;

import jakarta.persistence.*;

@Entity
public class Aircraft {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String model, manufacturer;
    private int capacity;
    // getters/setters...
}