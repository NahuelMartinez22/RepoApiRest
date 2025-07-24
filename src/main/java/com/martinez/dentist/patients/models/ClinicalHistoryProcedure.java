package com.martinez.dentist.patients.models;

import jakarta.persistence.*;

@Entity
public class ClinicalHistoryProcedure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinical_history_id")
    private ClinicalHistory clinicalHistory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "procedure_id")
    private DentalProcedure procedure;

    public ClinicalHistoryProcedure() {}

    public ClinicalHistoryProcedure(ClinicalHistory clinicalHistory, DentalProcedure procedure) {
        this.clinicalHistory = clinicalHistory;
        this.procedure = procedure;
    }

    public Long getId() {
        return id;
    }

    public ClinicalHistory getClinicalHistory() {
        return clinicalHistory;
    }

    public void setClinicalHistory(ClinicalHistory clinicalHistory) {
        this.clinicalHistory = clinicalHistory;
    }

    public DentalProcedure getProcedure() {
        return procedure;
    }

    public void setProcedure(DentalProcedure procedure) {
        this.procedure = procedure;
    }
}
