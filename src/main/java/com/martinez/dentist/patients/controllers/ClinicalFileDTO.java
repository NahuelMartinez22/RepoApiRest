package com.martinez.dentist.patients.controllers;

public class ClinicalFileDTO {

    private Long id;
    private String fileName;
    private String fileType;

    public ClinicalFileDTO() {}

    public ClinicalFileDTO(Long id, String fileName, String fileType) {
        this.id = id;
        this.fileName = fileName;
        this.fileType = fileType;
    }

    public Long getId() { return id; }
    public String getFileName() { return fileName; }
    public String getFileType() { return fileType; }

    public void setId(Long id) { this.id = id; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public void setFileType(String fileType) { this.fileType = fileType; }
}
