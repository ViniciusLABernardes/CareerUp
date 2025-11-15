package com.br.CareerUp.controller;

import com.br.CareerUp.exceptions.IdNaoEncontradoException;
import com.br.CareerUp.model.Recomendacao;
import com.br.CareerUp.model.Usuario;
import com.br.CareerUp.service.RecomendacaoService;
import com.br.CareerUp.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/recomendacao")
public class RecomendacaoController {

    @Autowired
    private RecomendacaoService recomendacaoService;
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/listar")
    public String listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model,
            Authentication authentication) {

        String login = authentication.getName();
        Pageable pageable = PageRequest.of(page, size, Sort.by("idRecomendacao").descending());

        Page<Recomendacao> recomendacoesPage = recomendacaoService.listarRecomendacoesPaginadas(login, pageable);

        model.addAttribute("recomendacoesPage", recomendacoesPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", recomendacoesPage.getTotalPages());

        Usuario usuario = usuarioService.buscarPorLogin(login);
        model.addAttribute("usuario", usuario);

        return "recomendacoes";
    }

    @GetMapping("/exportar")
    public String mostrarPaginaExportar() {
        return "exportar-recomendacao";

    }

    @PostMapping("/exportar")
    public String exportarRecomendacao(
            @RequestParam("idUsuario") Long idUsuario,
            Model model) {

        try {
            String json = recomendacaoService.exportarRecomendacaoUsuario(idUsuario);

            model.addAttribute("jsonResultado", json);
            model.addAttribute("idUsuario", idUsuario);

            return "exportar-recomendacao";

        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao exportar recomendação: " + e.getMessage());
            return "exportar-recomendacao";
        }
    }

    @PostMapping("/{idUsuario}/gerar")
    public String gerarRecomendacao(@PathVariable Long idUsuario, Model model) {
        try {
            Recomendacao recomendacao = recomendacaoService.gerarRecomendacao(idUsuario);
            model.addAttribute("recomendacao", recomendacao);
            model.addAttribute("mensagemSucesso", "Recomendação gerada com sucesso!");
        } catch (RuntimeException e) {
            model.addAttribute("erro", e.getMessage());
        }


        return "detalhe-recomendacao";

    }

    @GetMapping("/detalhe/{id}")
    public String detalharRecomendacao(@PathVariable Long id, Model model) {
        Recomendacao recomendacao = recomendacaoService.buscarPorId(id);
        model.addAttribute("recomendacao", recomendacao);
        return "detalhe-recomendacao";
    }

    @PostMapping("{idRecomendacao}/excluir")
    public String deletarRecomendacao(@PathVariable Long idRecomendacao, Model model) {
        try {
            recomendacaoService.deletarRecomendacao(idRecomendacao);
        } catch (IdNaoEncontradoException e) {
            model.addAttribute("erro", e.getMessage());
        }
        return "redirect:/recomendacao/listar";
    }
}
