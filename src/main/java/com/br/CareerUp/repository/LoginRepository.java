package com.br.CareerUp.repository;

import com.br.CareerUp.model.LoginUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LoginRepository extends JpaRepository<LoginUsuario, Long> {
}
