package com.martinez.dentist.patients.models;

public enum PatientState {
    ACTIVE("Active"),
    DEACTIVATED("Deactivated");

    private final String displayName;

    PatientState(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
