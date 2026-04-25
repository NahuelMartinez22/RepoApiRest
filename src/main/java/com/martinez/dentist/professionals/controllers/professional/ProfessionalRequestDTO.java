package com.martinez.dentist.professionals.controllers.professional;

import com.martinez.dentist.professionals.controllers.schedule.ScheduleRequestDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ProfessionalRequestDTO {
    private String fullName;
    private String documentType;
    private String documentNumber;
    private String phone;
    private List<ScheduleRequestDTO> schedules;
    private List<Long> specialtyIds;

    public ProfessionalRequestDTO() {}

}
