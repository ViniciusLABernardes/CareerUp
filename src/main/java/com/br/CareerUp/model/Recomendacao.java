package com.br.CareerUp.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_recomendacao")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(name = "recomendacao_seq", sequenceName = "tb_recomendacao_seq",allocationSize = 1)
public class Recomendacao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recomendacao_seq")
    @Column(name = "id_recomendacao")
    private Long idRecomendacao;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "data_geracao", nullable = false)
    private LocalDateTime dataGeracao;

    @Lob
    @Column(name = "resultado_ia", nullable = false)
    private String resultadoIa;

}
