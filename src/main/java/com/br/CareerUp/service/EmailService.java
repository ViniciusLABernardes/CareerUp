package com.br.CareerUp.service;

import com.br.CareerUp.model.EmailModel;
import com.br.CareerUp.model.StatusEmail;
import com.br.CareerUp.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import java.util.Optional;
import java.util.UUID;

@Service
public class EmailService {

    @Autowired
    EmailRepository emailRepository;

    @Autowired
    private JavaMailSender emailSender;

    @Transactional
    public EmailModel enviarEmail(EmailModel emailModel) {
        emailModel.setSendDateEmail(LocalDateTime.now());
        //Montando o e-mail e enviando e colocando o status SENT ou ERROR no BD
        //Os status são do enum StatusEmail criado
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(emailModel.getEmailFrom());
            message.setTo(emailModel.getEmailTo());
            message.setSubject(emailModel.getSubject());
            message.setText(emailModel.getText());
            emailSender.send(message);

            emailModel.setStatusEmail(StatusEmail.ENVIADO);
        } catch (MailException e){
            emailModel.setStatusEmail(StatusEmail.ERRO);
            e.printStackTrace();
        } finally {
            return emailRepository.save(emailModel);
        }
    }
    public Page<EmailModel> findAll(Pageable pageable) {
        return  emailRepository.findAll(pageable);
    }

    public Optional<EmailModel> findById(UUID emailId) {
        return emailRepository.findById(emailId);
    }

}
