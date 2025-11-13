package com.br.CareerUp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;



@Data
public class EmailDto {

    //Declaração dos componentes do e-mail (referência, de quem, para quem, assunto e texto)
    private String ownerRef;

    private String emailFrom;
    @NotBlank
    @Email
    private String emailTo;
    @NotBlank
    private String subject;
    @NotBlank
    private String text;

}
