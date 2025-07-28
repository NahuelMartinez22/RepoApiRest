package com.martinez.dentist.patients.controllers.clinicalHistory;

public class ClinicalFileDTO {

    private Long id;
    private String fileName;
    private String fileType;
    private Long size;

    public ClinicalFileDTO() {}

    public ClinicalFileDTO(Long id, String fileName, String fileType, Long size) {
        this.id = id;
        this.fileName = fileName;
        this.fileType = fileType;
        this.size = size;
    }

    public Long getId() { return id; }
    public String getFileName() { return fileName; }
    public String getFileType() { return fileType; }
    public Long getSize() { return size; }

    public void setId(Long id) { this.id = id; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public void setFileType(String fileType) { this.fileType = fileType; }
    public void setSize(Long size) { this.size = size; }
}
