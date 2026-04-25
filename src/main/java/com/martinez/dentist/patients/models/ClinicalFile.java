package com.martinez.dentist.patients.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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

}