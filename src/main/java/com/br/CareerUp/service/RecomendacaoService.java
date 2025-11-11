package com.br.CareerUp.service;

import com.br.CareerUp.model.Recomendacao;
import com.br.CareerUp.model.Usuario;
import com.br.CareerUp.repository.RecomendacaoRepository;
import com.br.CareerUp.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecomendacaoService {

    @Autowired
    private RecomendacaoRepository recomendacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Recomendacao gerarRecomendacao(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Simulação de resposta de IA (substituir depois pela chamada real)
        String respostaIa = "Com base nas suas habilidades: " + usuario.getHabilidade()
                + " recomendamos o curso 'Spring Boot Avançado' e vaga 'Dev Java Jr'.";

        Recomendacao rec = new Recomendacao();
        rec.setIdUsuario(usuario.getIdUsuario());
        rec.setResultadoIa(respostaIa);
        rec.setDataGeracao(LocalDateTime.now());

        recomendacaoRepository.save(rec);

        return rec;
    }

    public List<Recomendacao> listarRecomendacoes(Long idUsuario) {
        return recomendacaoRepository.findByUsuario(idUsuario);
    }
}
