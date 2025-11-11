package com.br.CareerUp.dto;

import com.br.CareerUp.model.Habilidade;
import com.br.CareerUp.model.Login;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data


public class UsuarioRequestDto {

    @NotBlank
    private String nomeUsuario;
    @NotBlank
    private String cpf;
    @NotBlank
    private String cargo;
    @NotBlank
    private LoginRequestDto login;
    @NotBlank
    private HabilidadeRequestDto habilidades;


}
