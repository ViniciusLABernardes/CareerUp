package com.br.CareerUp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDto {
    public LoginRequestDto() {
    }

    public LoginRequestDto(String login, String senha) {
        this.login = login;
        this.senha = senha;
    }

    @NotBlank(message = "O login é obrigatório")
    private String login;

    @NotBlank(message = "A senha é obrigatória")
    private String senha;
}
