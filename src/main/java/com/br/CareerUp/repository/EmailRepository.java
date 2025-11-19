package com.br.CareerUp.repository;


import com.br.CareerUp.model.EmailModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.UUID;

public interface EmailRepository extends JpaRepository<EmailModel, UUID> {
    @Procedure(procedureName = "pkg_usuario.inserir_email")
    void inserir_email(
            @Param("p_owner_ref") String ownerRef,
            @Param("p_email_from") String emailFrom,
            @Param("p_email_to") String emailTo,
            @Param("p_subject") String subject,
            @Param("p_text") String text,
            @Param("p_data_envio") LocalDateTime dataEnvio,
            @Param("p_status") String status

    );
}
