package br.edu.ifmg.produto.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class RequestTokenDTO {

    @NotBlank(message = "Campo requerido")
    @Email(message = "E-mail inválido")
    private String email;

    public RequestTokenDTO(String email) {
        this.email = email;
    }

    public RequestTokenDTO() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}