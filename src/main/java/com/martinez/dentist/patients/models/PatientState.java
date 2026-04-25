package com.martinez.dentist.patients.models;

import lombok.Getter;

@Getter
public enum PatientState {
    ACTIVE("Active"),
    DEACTIVATED("Deactivated");

    private final String displayName;

    PatientState(String displayName) {
        this.displayName = displayName;
    }

}
