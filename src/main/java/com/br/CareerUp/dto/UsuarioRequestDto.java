package com.br.CareerUp.dto;

import com.br.CareerUp.model.PapelUsuario;
import jakarta.validation.Valid;
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

    private PapelUsuario papel;
    @Valid
    private LoginRequestDto loginUsuario;
    @Valid
    private HabilidadeRequestDto habilidades;


}
