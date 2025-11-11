package com.br.CareerUp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="TB_HABILIDADE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Habilidade {

    @Id
    @Column(name = "id_usuario")
    private Long idUsuario;

    @OneToOne
    @MapsId
    @JoinColumn(referencedColumnName = "id_usuario", name = "id_usuario")
    private Usuario usuario;

    @Column(name = "habilidade_primaria")
    private String habilidadePrimaria;

    @Column(name="habilidade_secundaria")
    private String habilidadeSecundaria;

    @Column(name = "habilidade_terciaria")
    private String habilidadeTerciaria;

}
