package com.martinez.dentist.appointments.models;

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

    public String getDescripcion() {
        return descripcion;
    }
}