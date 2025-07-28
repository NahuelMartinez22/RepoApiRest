package com.martinez.dentist.patients.controllers.clinicalHistory;

public class UploadedFileResponseDTO {
    private Long id;
    private String fileName;

    public UploadedFileResponseDTO(Long id, String fileName) {
        this.id = id;
        this.fileName = fileName;
    }

    public Long getId() { return id; }
    public String getFileName() { return fileName; }
}
