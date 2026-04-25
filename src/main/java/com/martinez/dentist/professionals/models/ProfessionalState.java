package com.martinez.dentist.professionals.models;

import lombok.Getter;

@Getter
public enum ProfessionalState {
    ACTIVE("Active"),
    DEACTIVATED("Deactivated");

    private final String displayName;

    ProfessionalState(String displayName) {
        this.displayName = displayName;
    }

}
