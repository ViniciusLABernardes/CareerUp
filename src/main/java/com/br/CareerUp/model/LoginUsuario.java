package com.br.CareerUp.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "TB_LOGIN_USUARIO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginUsuario implements Serializable {

    @Id
    @Column(name = "id_usuario")
    private Long idUsuario;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    private Usuario usuario;

    @Column(name = "login", length = 50, nullable = false, unique = true)
    private String login;

    @Column(name = "senha", length = 180, nullable = false)
    private String senha;


}