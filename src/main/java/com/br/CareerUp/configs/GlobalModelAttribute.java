package com.br.CareerUp.configs;

import com.br.CareerUp.model.Usuario;
import com.br.CareerUp.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@ControllerAdvice
public class GlobalModelAttribute {

    private final UsuarioService usuarioService;

    public GlobalModelAttribute(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @ModelAttribute("usuarioLogado")
    public Usuario adicionarUsuarioLogado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName().equals("anonymousUser")) {
            return null;
        }
        String login = auth.getName();
        return usuarioService.buscarPorLogin(login);
    }
}
