package com.martinez.dentist.patients.controllers;

import jakarta.validation.constraints.NotBlank;

public class HealthInsuranceRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    private String contactEmail;
    private String phone;
    private String note;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
