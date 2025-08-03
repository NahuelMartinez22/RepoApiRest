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
    private List<Long> specialtyIds;
    private List<String> specialtyNames;
    private String professionalState;
    private Boolean available;

    public ProfessionalResponseDTO(Long id, String fullName, String documentType,
                                   String documentNumber, String phone,
                                   List<ScheduleResponseDTO> schedules,
                                   List<Long> specialtyIds,
                                   List<String> specialtyNames,
                                   String professionalState,
                                   Boolean available) {
        this.id = id;
        this.fullName = fullName;
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.phone = phone;
        this.schedules = schedules;
        this.specialtyIds = specialtyIds;
        this.specialtyNames = specialtyNames;
        this.professionalState = professionalState;
        this.available = available;
    }

    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getDocumentType() { return documentType; }
    public String getDocumentNumber() { return documentNumber; }
    public String getPhone() { return phone; }
    public List<ScheduleResponseDTO> getSchedules() { return schedules; }
    public List<Long> getSpecialtyIds() { return specialtyIds; }
    public List<String> getSpecialtyNames() { return specialtyNames; }
    public String getProfessionalState() { return professionalState; }
    public Boolean getAvailable() { return available; }
}