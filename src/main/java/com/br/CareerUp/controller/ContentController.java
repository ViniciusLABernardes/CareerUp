package com.br.CareerUp.controller;

import com.br.CareerUp.model.Habilidade;
import com.br.CareerUp.model.LoginUsuario;
import com.br.CareerUp.model.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class ContentController {
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/cadastro")
    public String exibirFormulario(Model model) {
        Usuario usuario = new Usuario();
        usuario.setLoginUsuario(new LoginUsuario());
        usuario.setHabilidades(new Habilidade());
        model.addAttribute("usuario", usuario);
        return "cadastro";
    }
    @GetMapping("/index")
    public String index(){
        return "index";
    }

}
