package com.br.CareerUp.controller;


import com.br.CareerUp.dto.HabilidadeRequestDto;
import com.br.CareerUp.dto.UsuarioRequestDto;
import com.br.CareerUp.exceptions.IdNaoEncontradoException;
import com.br.CareerUp.model.Usuario;

import com.br.CareerUp.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public String listarUsuarios(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("nomeUsuario").ascending());
        Page<Usuario> usuariosPage = usuarioService.listarUsuarios(pageable);

        model.addAttribute("usuariosPage", usuariosPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", usuariosPage.getTotalPages());

        return "usuarios";
    }

    @PostMapping("{idUsuario}/excluir")
    public String deletarUsuario(@PathVariable Long idUsuario, Model model){
        try {
            usuarioService.deletarUsuario(idUsuario);
        } catch (IdNaoEncontradoException e) {
            model.addAttribute("erro", e.getMessage());
        }
        return "redirect:/usuario/listar";
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

    @PostMapping("{idUsuario}/alterar-cargo")
    public String alterarCargo(@PathVariable Long idUsuario,
                               String novoCargo) throws IdNaoEncontradoException {

        usuarioService.editarCargoUsuarioPorLogin(idUsuario, novoCargo);

        return "redirect:/usuario/listar";
    }

    @GetMapping("{idUsuario}/editar")
    public String editarUsuario(Model model,@PathVariable  Long idUsuario) throws IdNaoEncontradoException {
        Usuario usuario = usuarioService.visualizarDadosUsuarioEspecifico(idUsuario);
        model.addAttribute("usuario", usuario);
        return "detalhe-usuario";

    }

    @GetMapping("{idUsuario}/alterar-habilidades")
    public String alterarHabilidade(Model model,@PathVariable  Long idUsuario) throws IdNaoEncontradoException {
        Usuario usuario = usuarioService.visualizarDadosUsuarioEspecifico(idUsuario);
        model.addAttribute("usuario", usuario);
        return "alterar-habilidades";

    }

    @PostMapping("{idUsuario}/atualizar-habilidades")
    public String atualizarHabilidades(@PathVariable Long idUsuario, Model model, HabilidadeRequestDto dto,
                                       RedirectAttributes redirectAttributes) throws IdNaoEncontradoException{
        usuarioService.atualizarHabilidades(idUsuario,dto);
        Usuario usuario = usuarioService.visualizarDadosUsuarioEspecifico(idUsuario);
        model.addAttribute("usuario",usuario);
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Habilidades atualizadas com sucesso!");
        return "redirect:/recomendacao/listar";
    }
}
