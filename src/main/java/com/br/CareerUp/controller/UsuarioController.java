package com.br.CareerUp.controller;

import com.br.CareerUp.dto.UsuarioRequestDto;
import com.br.CareerUp.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/salvar")
    public String salvarUsuario(@ModelAttribute("usuario") UsuarioRequestDto usuarioRequestDto) throws Exception{
        usuarioService.salvarUsuario(usuarioRequestDto);
        return "redirect:/recomendacao/listar";
    }

}
