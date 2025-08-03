package com.martinez.dentist.professionals.controllers.professional;

import com.martinez.dentist.professionals.controllers.schedule.ScheduleRequestDTO;
import java.util.List;

public class ProfessionalRequestDTO {
    private String fullName;
    private String documentType;
    private String documentNumber;
    private String phone;
    private List<ScheduleRequestDTO> schedules;
    private List<Long> specialtyIds;

    public ProfessionalRequestDTO() {}

    public String getFullName() { return fullName; }
    public String getDocumentType() { return documentType; }
    public String getDocumentNumber() { return documentNumber; }
    public String getPhone() { return phone; }
    public List<ScheduleRequestDTO> getSchedules() { return schedules; }
    public List<Long> getSpecialtyIds() { return specialtyIds; }

    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setDocumentType(String documentType) { this.documentType = documentType; }
    public void setDocumentNumber(String documentNumber) { this.documentNumber = documentNumber; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setSchedules(List<ScheduleRequestDTO> schedules) { this.schedules = schedules; }
    public void setSpecialtyIds(List<Long> specialtyIds) { this.specialtyIds = specialtyIds; }
}
