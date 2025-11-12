package com.br.CareerUp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="tb_usuario")
@Getter
@Builder
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(sequenceName = "tb_usuario_id_usuario_seq", name = "usuario_seq", allocationSize = 1)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_seq")
    private long idUsuario;

    @Column(name= "nome_usuario",nullable = false,length = 255)
    private String nomeUsuario;

    @Column(name="cpf",nullable = false,length = 30, unique = true)
    private String cpf;

    @Column(name = "cargo",nullable = false)
    private String cargo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PapelUsuario papel;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Habilidade habilidades;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private LoginUsuario loginUsuario;


}
