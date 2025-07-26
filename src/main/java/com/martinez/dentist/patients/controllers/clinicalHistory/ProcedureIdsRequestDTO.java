package com.martinez.dentist.patients.controllers.clinicalHistory;


import java.util.List;

public class ProcedureIdsRequestDTO {

    private List<Long> procedureIds;

    public ProcedureIdsRequestDTO() {}

    public ProcedureIdsRequestDTO(List<Long> procedureIds) {
        this.procedureIds = procedureIds;
    }

    public List<Long> getProcedureIds() {
        return procedureIds;
    }

    public void setProcedureIds(List<Long> procedureIds) {
        this.procedureIds = procedureIds;
    }
}
