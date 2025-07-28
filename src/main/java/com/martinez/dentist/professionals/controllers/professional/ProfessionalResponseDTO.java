package com.martinez.dentist.professionals.controllers.professional;

import com.martinez.dentist.professionals.controllers.schedule.ScheduleResponseDTO;
import java.util.List;

public class ProfessionalResponseDTO {
    private Long id;
    private String fullName;
    private String documentType;
    private String documentNumber;
    private String phone;
    private List<ScheduleResponseDTO> schedules;
    private List<String> specialties;
    private String professionalState;
    private Boolean available;

    public ProfessionalResponseDTO(Long id, String fullName, String documentType,
                                   String documentNumber, String phone,
                                   List<ScheduleResponseDTO> schedules,
                                   List<String> specialties,
                                   String professionalState,
                                   Boolean available) {
        this.id = id;
        this.fullName = fullName;
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.phone = phone;
        this.schedules = schedules;
        this.specialties = specialties;
        this.professionalState = professionalState;
        this.available = available;
    }

    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getDocumentType() { return documentType; }
    public String getDocumentNumber() { return documentNumber; }
    public String getPhone() { return phone; }
    public List<ScheduleResponseDTO> getSchedules() { return schedules; }
    public List<String> getSpecialties() { return specialties; }
    public String getProfessionalState() { return professionalState; }
    public Boolean getAvailable() { return available; }
}
