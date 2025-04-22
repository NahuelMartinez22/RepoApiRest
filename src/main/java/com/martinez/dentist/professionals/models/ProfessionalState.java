package com.martinez.dentist.professionals.models;

public enum ProfessionalState {
    ACTIVE("Active"),
    DEACTIVATED("Deactivated");

    private final String displayName;

    ProfessionalState(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
