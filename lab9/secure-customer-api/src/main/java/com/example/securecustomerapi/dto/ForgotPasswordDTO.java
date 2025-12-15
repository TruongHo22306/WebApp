package com.example.securecustomerapi.dto;

import jakarta.validation.constraints.NotBlank;

public class ForgotPasswordDTO {

    @NotBlank
    private String identifier;

    public ForgotPasswordDTO() {
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
