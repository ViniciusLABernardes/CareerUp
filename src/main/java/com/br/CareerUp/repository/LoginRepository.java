package com.br.CareerUp.repository;

import com.br.CareerUp.model.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LoginRepository extends JpaRepository<Login, Long> {
}
