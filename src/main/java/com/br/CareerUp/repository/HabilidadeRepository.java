package com.br.CareerUp.repository;

import com.br.CareerUp.model.Habilidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface HabilidadeRepository extends JpaRepository<Habilidade, Long> {
}
