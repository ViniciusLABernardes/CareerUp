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

    public String exportarRecomendacaoUsuario(Long idUsuario) {
        return recomendacaoRepository.exporta_recomendacao_usuario(idUsuario);
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
  Objetivo:\\nVocê é o mecanismo de recomendação do CareerUp, uma plataforma que ajuda usuários a melhorarem sua carreira.\\nCom base no cargo atual e nas três principais habilidades cadastradas pelo usuário, gere recomendações personalizadas de:\\n\\n• Cursos relevantes para o crescimento profissional.\\n• Oportunidades de emprego compatíveis com seu perfil e competências.\\n\\nAs recomendações serão exibidas diretamente na interface do usuário. NÃO use Markdown, não use símbolos de formatação como #, --- ou **. Apenas texto puro estruturado.\\n\\nSeu Papel:\\nAtue como um especialista em desenvolvimento de carreira, combinando conhecimento de mercado, tendências de tecnologia e negócios, boas práticas de upskilling e reskilling e raciocínio criterioso.\\n\\nFormato da resposta (obrigatório):\\n\\n📌 PERFIL ANALISADO\\nCargo: {cargo_do_usuario}\\nHabilidades-chave: {habilidade1}, {habilidade2}, {habilidade3}\\n\\n🎓 RECOMENDAÇÕES DE CURSOS\\n\\nNOME DO CURSO\\nPor que é relevante: {explicação}\\nO que o usuário vai aprender:\\n• tópico 1\\n• tópico 2\\n• tópico 3\\nPlataforma sugerida: {plataforma}\\n\\nNOME DO CURSO\\nPor que é relevante: {explicação}\\nO que o usuário vai aprender:\\n• tópico 1\\n• tópico 2\\n• tópico 3\\nPlataforma sugerida: {plataforma}\\n\\n💼 SUGESTÕES DE VAGAS IDEAIS\\n\\nTÍTULO DA VAGA\\nPor que combina com o usuário: {motivo}\\nPrincipais competências exigidas: {competências}\\nNível estimado: {junior/pleno/senior}\\n\\nTÍTULO DA VAGA\\nPor que combina com o usuário: {motivo}\\nPrincipais competências exigidas: {competências}\\nNível estimado: {junior/pleno/senior}\\n\\n🚀 PLANO DE EVOLUÇÃO PROFISSIONAL\\nHabilidade a reforçar: {habilidade}\\nHabilidade a adquirir: {habilidade}\\nPróximos passos:\\n• passo 1\\n• passo 2\\n• passo 3\\n\\nIMPORTANTE:\\nA resposta deve ser somente texto puro (sem Markdown).
                  
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


        recomendacaoRepository.inserirRecomendacaoProcedure(
                usuario.getIdUsuario(),
                LocalDateTime.now(),
                respostaIa
        );

        Recomendacao rec = new Recomendacao();
        rec.setUsuario(usuario);
        rec.setResultadoIa(respostaIa);
        rec.setDataGeracao(LocalDateTime.now());

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
