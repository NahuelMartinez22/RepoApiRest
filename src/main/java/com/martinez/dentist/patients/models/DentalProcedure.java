package com.martinez.dentist.patients.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "dental_procedures")
public class DentalProcedure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false, unique = true)
    private String code;

    @Setter
    @Column(nullable = false)
    private String name;

    @Setter
    @Column(name = "base_value")
    private BigDecimal baseValue;

    @Setter
    @Column(name = "is_active")
    private boolean isActive = true;

    public DentalProcedure() {}
}
