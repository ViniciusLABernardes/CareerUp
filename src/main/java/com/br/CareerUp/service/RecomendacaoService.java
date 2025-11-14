package com.br.CareerUp.service;

import com.br.CareerUp.exceptions.IdNaoEncontradoException;
import com.br.CareerUp.model.Recomendacao;
import com.br.CareerUp.model.Usuario;
import com.br.CareerUp.repository.RecomendacaoRepository;
import com.br.CareerUp.repository.UsuarioRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecomendacaoService {


    @Autowired
    private RecomendacaoRepository recomendacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private AiService aiService;

    public RecomendacaoService(AiService aiService) {
        this.aiService = aiService;
    }

    public Recomendacao buscarPorId(Long id) {
        return recomendacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recomendação não encontrada"));
    }

    @Cacheable(value = "recomendacaoCache", key = "#idUsuario")
    public Recomendacao gerarRecomendacao(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));


        String cargo = usuario.getCargo();
        String h1 = usuario.getHabilidades().getHabilidadePrimaria();
        String h2 = usuario.getHabilidades().getHabilidadeSecundaria();
        String h3 = usuario.getHabilidades().getHabilidadeTerciaria();

        String promptUsuario =
                "Cargo do usuário: " + cargo + "\n" +
                        "Habilidades principais: " + h1 + ", " + h2 + ", " + h3 + "\n\n";

        String prompt = """
       Objetivo:
Você é o mecanismo de recomendação do CareerUp, uma plataforma que ajuda usuários a melhorarem sua carreira.
Com base no cargo atual e nas três principais habilidades cadastradas pelo usuário, gere recomendações personalizadas de:

• Cursos relevantes para o crescimento profissional.
• Oportunidades de emprego compatíveis com seu perfil e competências.

As recomendações serão exibidas diretamente na interface do usuário. NÃO use Markdown, não use símbolos de formatação como #, --- ou **. Apenas texto puro estruturado.

Seu Papel:
Atue como um especialista em desenvolvimento de carreira, combinando conhecimento de mercado, tendências de tecnologia e negócios, boas práticas de upskilling e reskilling e raciocínio criterioso.

Formato da resposta (obrigatório):

📌 PERFIL ANALISADO
Cargo: {cargo_do_usuario}
Habilidades-chave: {habilidade1}, {habilidade2}, {habilidade3}

🎓 RECOMENDAÇÕES DE CURSOS

NOME DO CURSO
Por que é relevante: {explicação}
O que o usuário vai aprender:
• tópico 1
• tópico 2
• tópico 3
Plataforma sugerida: {plataforma}

NOME DO CURSO
Por que é relevante: {explicação}
O que o usuário vai aprender:
• tópico 1
• tópico 2
• tópico 3
Plataforma sugerida: {plataforma}

💼 SUGESTÕES DE VAGAS IDEAIS

TÍTULO DA VAGA
Por que combina com o usuário: {motivo}
Principais competências exigidas: {competências}
Nível estimado: {junior/pleno/senior}

TÍTULO DA VAGA
Por que combina com o usuário: {motivo}
Principais competências exigidas: {competências}
Nível estimado: {junior/pleno/senior}

🚀 PLANO DE EVOLUÇÃO PROFISSIONAL
Habilidade a reforçar: {habilidade}
Habilidade a adquirir: {habilidade}
Próximos passos:
• passo 1
• passo 2
• passo 3

IMPORTANTE:
A resposta deve ser somente texto puro (sem Markdown).
""";

        String promptCompleto = prompt + "\n\n" + promptUsuario;

        System.out.println(">>> Chamando IA uma única vez...");
        System.out.println("id user: " + idUsuario);
        System.out.println("usuario nome: " + usuario.getNomeUsuario());
        System.out.println("usuario cargo: " + usuario.getCargo());
        System.out.println("habilidades: " + usuario.getHabilidades().getHabilidadePrimaria());

        String respostaIa = aiService.chat(promptCompleto);
        System.out.println("prompt: " + respostaIa);
        System.out.println(">>> Resposta recebida da IA");

        Recomendacao rec = new Recomendacao();
        rec.setUsuario(usuario);
        rec.setResultadoIa(respostaIa);
        rec.setDataGeracao(LocalDateTime.now());

        recomendacaoRepository.save(rec);

        return rec;
    }

    @CacheEvict(value = "recomendacaoCache", key = "#idUsuario")
    public void invalidarCachePorUsuario(Long idUsuario) {

    }

    public Page<Recomendacao> listarRecomendacoesPaginadas(String login, Pageable pageable) {
        return recomendacaoRepository.findByUsuario_LoginUsuario_Login(login, pageable);
    }

    public void deletarRecomendacao(Long idRecomendacao) throws IdNaoEncontradoException {
        Recomendacao recomendacao = recomendacaoRepository.findById(idRecomendacao)
                .orElseThrow(()-> new IdNaoEncontradoException("Recomendação com id: " + idRecomendacao + " não encontrada!"));
        recomendacaoRepository.delete(recomendacao);
    }
}
