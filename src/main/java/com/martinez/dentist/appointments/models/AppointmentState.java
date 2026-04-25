package com.martinez.dentist.appointments.models;

import lombok.Getter;

@Getter
public enum AppointmentState {
    PENDIENTE("Pendiente"),
    ATENDIDO("Atendido"),
    AUSENTE_CON_AVISO("Ausente con aviso"),
    AUSENTE_SIN_AVISO("Ausente sin aviso"),
    CANCELADO("Cancelado"),
    CONFIRMADO("Confirmado"),
    NINGUNO("Ninguno");

    private final String descripcion;

    AppointmentState(String descripcion) {
        this.descripcion = descripcion;
    }

}