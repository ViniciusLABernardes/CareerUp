package com.br.CareerUp.repository;



import com.br.CareerUp.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    Optional<Usuario> findByLoginUsuario_Login(String login);
    Optional<Usuario> findByCpf(String cpf);
    @Procedure(procedureName = "pkg_usuario.inserir_usuario")
    void inserirUsuarioProcedure(
            @Param("p_nome_usuario") String nomeUsuario,
            @Param("p_cpf") String cpf,
            @Param("p_email") String email,
            @Param("p_cargo") String cargo,
            @Param("p_papel") String papel
    );
}
