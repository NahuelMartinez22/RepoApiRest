package com.martinez.dentist.patients.models;

import jakarta.persistence.*;

@Entity
@Table(name = "clinical_files")
public class ClinicalFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String fileType;

    @Column(name = "data", columnDefinition = "LONGBLOB", nullable = false)
    private byte[] data;

    @ManyToOne(optional = false)
    @JoinColumn(name = "clinical_history_id")
    private ClinicalHistory clinicalHistory;

    public ClinicalFile() {}

    public ClinicalFile(String fileName, String fileType, byte[] data, ClinicalHistory clinicalHistory) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
        this.clinicalHistory = clinicalHistory;
    }

    public Long getId() { return id; }
    public String getFileName() { return fileName; }
    public String getFileType() { return fileType; }
    public byte[] getData() { return data; }
    public ClinicalHistory getClinicalHistory() { return clinicalHistory; }

    public void setId(Long id) { this.id = id; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public void setFileType(String fileType) { this.fileType = fileType; }
    public void setData(byte[] data) { this.data = data; }
    public void setClinicalHistory(ClinicalHistory clinicalHistory) { this.clinicalHistory = clinicalHistory; }
}