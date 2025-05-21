package com.martinez.dentist.appointments.controllers;

import java.time.LocalDate;

public class BillingAppointmentDTO {
    private LocalDate fecha;
    private String nombreApellido;
    private String dni;
    private String numeroAfiliado;
    private String plan;
    private String codigoProcedimiento;
    private Double valor;
    private String tokenCredencial;

    public BillingAppointmentDTO(LocalDate fecha, String nombreApellido, String dni, String numeroAfiliado,
                                 String plan, String codigoProcedimiento, Double valor, String tokenCredencial) {
        this.fecha = fecha;
        this.nombreApellido = nombreApellido;
        this.dni = dni;
        this.numeroAfiliado = numeroAfiliado;
        this.plan = plan;
        this.codigoProcedimiento = codigoProcedimiento;
        this.valor = valor;
        this.tokenCredencial = tokenCredencial;
    }

    public LocalDate getFecha() {return fecha;}
    public void setFecha(LocalDate fecha) {this.fecha = fecha;}

    public String getNombreApellido() {return nombreApellido;}
    public void setNombreApellido(String nombreApellido) {this.nombreApellido = nombreApellido;}

    public String getDni() {return dni;}
    public void setDni(String dni) {this.dni = dni;}

    public String getNumeroAfiliado() {return numeroAfiliado;}
    public void setNumeroAfiliado(String numeroAfiliado) {this.numeroAfiliado = numeroAfiliado;}

    public String getPlan() {return plan;}
    public void setPlan(String plan) {this.plan = plan;}

    public String getCodigoProcedimiento() {return codigoProcedimiento;}
    public void setCodigoProcedimiento(String codigoProcedimiento) {this.codigoProcedimiento = codigoProcedimiento;}

    public Double getValor() {return valor;}
    public void setValor(Double valor) {this.valor = valor;}

    public String getTokenCredencial() {return tokenCredencial;}
    public void setTokenCredencial(String tokenCredencial) {this.tokenCredencial = tokenCredencial;}
}
