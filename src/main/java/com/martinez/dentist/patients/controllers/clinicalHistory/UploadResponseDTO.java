package com.martinez.dentist.patients.controllers.clinicalHistory;

import java.util.List;

public class UploadResponseDTO {
    private String message;
    private List<UploadedFileResponseDTO> archivos;

    public UploadResponseDTO(String message, List<UploadedFileResponseDTO> archivos) {
        this.message = message;
        this.archivos = archivos;
    }

    public String getMessage() { return message; }
    public List<UploadedFileResponseDTO> getArchivos() { return archivos; }
}
