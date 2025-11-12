package com.br.CareerUp.repository;

import com.br.CareerUp.model.Recomendacao;
import com.br.CareerUp.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RecomendacaoRepository extends JpaRepository<Recomendacao, Long> {
    List<Recomendacao> findByUsuario_LoginUsuario_Login(String login);


}
