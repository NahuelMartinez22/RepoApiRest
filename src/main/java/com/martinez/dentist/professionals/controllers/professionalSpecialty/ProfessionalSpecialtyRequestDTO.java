package com.martinez.dentist.professionals.controllers.professionalSpecialty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProfessionalSpecialtyRequestDTO {
    private Long professionalId;
    private Long specialtyId;
}
