package com.martinez.dentist.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserResponseDTO {
    private long id;

    @JsonProperty("username")
    private String username;

    public UserResponseDTO(long id, String username) {
        this.id = id;
        this.username = username;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
