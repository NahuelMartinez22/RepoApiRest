package com.martinez.dentist.config;

import jakarta.persistence.*;

@Entity
@Table(name = "config")
public class Config {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String clave;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String valor;

    public Config() {}

    public Config(String clave, String valor) {
        this.clave = clave;
        this.valor = valor;
    }

    public Long getId() {
        return id;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
