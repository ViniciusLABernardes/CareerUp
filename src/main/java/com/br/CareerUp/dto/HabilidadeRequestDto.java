package com.br.CareerUp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HabilidadeRequestDto {
    @NotBlank
    private String habilidadePrimaria;
    @NotBlank
    private String habilidadeSecundaria;
    @NotBlank
    private String habilidadeTerciaria;
}
