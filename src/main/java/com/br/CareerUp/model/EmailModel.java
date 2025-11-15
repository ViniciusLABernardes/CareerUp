package com.br.CareerUp.model;


import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "TB_EMAIL")
public class EmailModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private UUID emailId;

    private String ownerRef;
    @Column(length = 100)
    private String emailFrom;
    @Column(length = 100)
    private String emailTo;

    private String subject;
    @Column(length = 4000)
    private String text;
    private LocalDateTime sendDateEmail;
    @Column(length = 50)
    private StatusEmail statusEmail;
}
