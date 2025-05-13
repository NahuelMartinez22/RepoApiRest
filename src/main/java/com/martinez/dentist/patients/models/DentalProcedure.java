package com.martinez.dentist.patients.models;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "dental_procedures")
public class DentalProcedure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(name = "base_value")
    private BigDecimal baseValue;

    @Column(name = "is_active")
    private boolean isActive = true;

    public DentalProcedure() {}

    public DentalProcedure(String code, String name, BigDecimal baseValue) {
        this.code = code;
        this.name = name;
        this.baseValue = baseValue;
        this.isActive = true;
    }


    public Long getId() { return id; }
    public String getCode() { return code; }
    public String getName() { return name; }
    public BigDecimal getBaseValue() { return baseValue; }
    public boolean isActive() { return isActive; }


    public void setCode(String code) { this.code = code; }
    public void setName(String name) { this.name = name; }
    public void setBaseValue(BigDecimal baseValue) { this.baseValue = baseValue; }
    public void setActive(boolean active) { isActive = active; }
}
