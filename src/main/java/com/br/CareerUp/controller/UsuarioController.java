package com.br.CareerUp.controller;


import com.br.CareerUp.dto.UsuarioRequestDto;
import com.br.CareerUp.exceptions.IdNaoEncontradoException;
import com.br.CareerUp.model.Usuario;
import com.br.CareerUp.repository.UsuarioRepository;
import com.br.CareerUp.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/listar")
    public String listarUsuarios(Model model){
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        model.addAttribute("usuario", usuarios);
        return "usuarios";

    }


    @GetMapping("/cadastro")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("usuario", new UsuarioRequestDto());
        return "cadastro";
    }

    @PostMapping("/salvar")
    public String salvarUsuario(@ModelAttribute("usuario") UsuarioRequestDto usuarioRequestDto, RedirectAttributes redirectAttributes) throws Exception{
        usuarioService.salvarUsuario(usuarioRequestDto);
        redirectAttributes.addFlashAttribute("cadastroSucesso", true);
        return "redirect:/login";
    }

    @PatchMapping("/alterar-cargo")
    public String alterarCargo(@RequestParam("novoCargo") String novoCargo,
                               Authentication authentication) throws IdNaoEncontradoException {

        String login = authentication.getName();
        usuarioService.editarCargoUsuarioPorLogin(login, novoCargo);

        return "redirect:/usuario/perfil";
    }

}
