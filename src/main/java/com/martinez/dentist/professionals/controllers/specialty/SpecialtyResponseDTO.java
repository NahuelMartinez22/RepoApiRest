package com.martinez.dentist.professionals.controllers.specialty;

public class SpecialtyResponseDTO {
    private Long id;
    private String name;

    public SpecialtyResponseDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}