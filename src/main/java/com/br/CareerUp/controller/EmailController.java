package com.br.CareerUp.controller;


import com.br.CareerUp.dto.EmailDto;
import com.br.CareerUp.exceptions.IdNaoEncontradoException;
import com.br.CareerUp.model.EmailModel;
import com.br.CareerUp.model.Usuario;
import com.br.CareerUp.service.EmailService;
import com.br.CareerUp.service.UsuarioService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/email")
public class EmailController {

    @Autowired
    EmailService emailService;
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/feedbacks")
    public String listarFeedbacks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("sendDateEmail").descending());
        Page<EmailModel> emailsPage = emailService.findAll(pageable);

        model.addAttribute("emailsPage", emailsPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", emailsPage.getTotalPages());

        return "feedbacks";
    }


    @GetMapping("/cadastrar-feedback")
    public String cadastrarFeedback(Model model) {
        model.addAttribute("email", new EmailDto());
        return "cadastrar-feedback";
    }


    @PostMapping("/enviar-email")
    public String enviarEmail(@Valid @ModelAttribute EmailDto emailDto,

                              RedirectAttributes redirectAttributes) {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginUsuario = authentication.getName();


        Usuario usuario = usuarioService.buscarPorLogin(loginUsuario);


        emailDto.setEmailFrom(usuario.getEmail());
        emailDto.setOwnerRef(usuario.getNomeUsuario());


        if (emailDto.getSubject() == null || emailDto.getSubject().isBlank() ||
                emailDto.getText() == null || emailDto.getText().isBlank()) {

            redirectAttributes.addFlashAttribute("mensagemErro", "Preencha todos os campos obrigatórios.");
            return "redirect:/email/cadastrar-feedback";
        }

        EmailModel emailModel = new EmailModel();
        BeanUtils.copyProperties(emailDto, emailModel);
        emailService.enviarEmail(emailModel);

        // Adiciona uma mensagem flash (disponível após o redirect)
        redirectAttributes.addFlashAttribute("mensagemSucesso", "E-mail enviado com sucesso!");


        return "redirect:/recomendacao/listar";
    }



}
