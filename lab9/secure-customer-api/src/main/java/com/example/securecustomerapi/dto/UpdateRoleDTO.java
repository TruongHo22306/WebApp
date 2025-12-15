package com.example.securecustomerapi.dto;

import com.example.securecustomerapi.entity.Role;
import jakarta.validation.constraints.NotNull;

public class UpdateRoleDTO {

    @NotNull
    private Role role;

    public UpdateRoleDTO() {
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
