package com.br.CareerUp.repository;

import com.br.CareerUp.model.Habilidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface HabilidadeRepository extends JpaRepository<Habilidade, Long> {

    @Procedure(procedureName = "pkg_usuario.inserir_habilidades")
    void inserirHabilidadesProcedure(
            @Param("p_id_usuario") Long idUsuario,
            @Param("p_hab_primaria") String primaria,
            @Param("p_hab_secundaria") String secundaria,
            @Param("p_hab_terciaria") String terciaria
    );
}
