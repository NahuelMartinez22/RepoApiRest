package com.martinez.dentist.professionals.controllers.specialty;

import lombok.Getter;

@Getter
public class SpecialtyResponseDTO {
    private Long id;
    private String name;

    public SpecialtyResponseDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}