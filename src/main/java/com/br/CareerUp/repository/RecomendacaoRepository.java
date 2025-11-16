package com.br.CareerUp.repository;

import com.br.CareerUp.model.Recomendacao;
import com.br.CareerUp.model.Usuario;
import jakarta.persistence.Lob;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Clob;
import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface RecomendacaoRepository extends JpaRepository<Recomendacao, Long> {
    Page<Recomendacao> findByUsuario_LoginUsuario_Login(String login, Pageable pageable);

    @Procedure(procedureName = "pkg_recomendacao.exporta_recomendacao_usuario")
    String exporta_recomendacao_usuario(@Param("p_id_usuario") Long idUsuario);

    @Procedure(procedureName = "pkg_recomendacao.inserir_recomendacao")
    void inserirRecomendacaoProcedure(
            @Param("p_id_usuario") Long idUsuario,
            @Param("p_data") LocalDateTime data,
            @Param("p_resultado") String resultado
    );
}
