package com.br.CareerUp.repository;

import com.br.CareerUp.model.LoginUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface LoginRepository extends JpaRepository<LoginUsuario, Long> {

    @Procedure(procedureName = "pkg_usuario.inserir_login")
    void inserirLoginProcedure(
            @Param("p_id_usuario") Long idUsuario,
            @Param("p_login") String login,
            @Param("p_senha") String senha
    );
}
