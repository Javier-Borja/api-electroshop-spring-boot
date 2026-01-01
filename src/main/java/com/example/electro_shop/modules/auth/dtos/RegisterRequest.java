package com.example.electro_shop.modules.auth.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank
    @Email(message = "Debe ser un email válido")
    private String email;

    @NotBlank
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;

    @NotBlank(message = "El nombre es obligatorio")
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio")
    private String lastName;

    public RegisterRequest() {
    }

    public RegisterRequest(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public @NotBlank @Email(message = "Debe ser un email válido") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank @Email(message = "Debe ser un email válido") String email) {
        this.email = email;
    }

    public @NotBlank @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres") String password) {
        this.password = password;
    }

    public @NotBlank(message = "El nombre es obligatorio") String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotBlank(message = "El nombre es obligatorio") String firstName) {
        this.firstName = firstName;
    }

    public @NotBlank(message = "El apellido es obligatorio") String getLastName() {
        return lastName;
    }

    public void setLastName(@NotBlank(message = "El apellido es obligatorio") String lastName) {
        this.lastName = lastName;
    }
}
