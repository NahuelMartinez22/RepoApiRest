package com.martinez.dentist.controllers;

public class ClientDTO {
    private String name;
    private String email;
    private int age;

    // Constructor sin argumentos
    public ClientDTO() {
    }

    // Constructor con argumentos
    public ClientDTO(String name, String email, int age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

    // Getters y setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    // Método toString (opcional)
    @Override
    public String toString() {
        return "ClientDTO{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                '}';
    }
}
