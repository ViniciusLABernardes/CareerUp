package com.br.CareerUp.consumers;

import com.br.CareerUp.dto.EmailDto;
import com.br.CareerUp.model.EmailModel;
import com.br.CareerUp.service.EmailService;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

//Bean gerenciado pelo Spring (Component)
@Component
public class EmailConsumer {

    //Ponto de injeção
    @Autowired
    EmailService emailService;

    //Método que escuta a fila definida ("${spring.rabbitmq.queue}")
    //Usando exchange default, pois não foi definido nenhum aqui
    @RabbitListener(queues = "${spring.rabbitmq.queue}")
    public void listen(@Payload EmailDto emailDto) {
        EmailModel emailModel = new EmailModel();
        //Quando recebe o e-mail DTO é necessário converter o DTO para Model
        //para montar a mensagem do e-mail e também fazer a persistência com o BD
        BeanUtils.copyProperties(emailDto, emailModel);
        //Chamando o método sendEmail criado no package services
        emailService.enviarEmail(emailModel);
        System.out.println("Email Status: " + emailModel.getStatusEmail().toString());
    }
}
