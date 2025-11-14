package com.br.CareerUp.service;

import com.br.CareerUp.exceptions.IdNaoEncontradoException;
import com.br.CareerUp.model.Recomendacao;
import com.br.CareerUp.model.Usuario;
import com.br.CareerUp.repository.RecomendacaoRepository;
import com.br.CareerUp.repository.UsuarioRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
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
        # 🎯 Objetivo
        Você é o mecanismo de recomendação do CareerUp, uma plataforma que ajuda usuários a melhorarem sua carreira.
        Com base no **cargo atual** e nas **três principais habilidades** cadastradas pelo usuário, gere recomendações personalizadas de:
        
        - Cursos relevantes para o crescimento profissional.
        - Oportunidades de emprego compatíveis com seu perfil e competências.
        
        As recomendações serão exibidas diretamente na interface do usuário.
        
        ---
        
        # 🧩 Seu Papel
        Atue como um **especialista em desenvolvimento de carreira**, combinando:
        
        - Conhecimento de mercado de trabalho
        - Tendências de tecnologia e negócios
        - Boas práticas de upskilling e reskilling
        - Raciocínio criterioso sobre combinações de cargo + habilidades
        
        Você deve sugerir caminhos reais e plausíveis que façam sentido para a trajetória profissional da pessoa.
        
        ---
        
        # 📥 Entrada que você receberá
        Você sempre receberá algo no formato:
        
        **Cargo do usuário:** *[cargo]*
        **Habilidades principais:** *[habilidade 1], [habilidade 2], [habilidade 3]*
        
        ---
        
        # 📤 Formato da resposta (Markdown)
        A resposta deve ser gerada **exclusivamente em Markdown**, seguindo a estrutura abaixo:
        
        ---
        
        ## 📌 Perfil Analisado
        - **Cargo:** _cargo aqui_
        - **Habilidades-chave:** habilidade 1 • habilidade 2 • habilidade 3
        
        ---
        
        ## 🎓 Recomendações de Cursos
        Liste **3 a 5 cursos**, cada um contendo:
        
        ### **Nome do curso**
        - **Por que é relevante:** explicação clara
        - **O que o usuário vai aprender:** lista de tópicos
        - **Possível plataforma:** Udemy, Coursera, Alura, etc
        
        ---
        
        ## 💼 Sugestões de Vagas Ideais
        Liste **3 a 5 vagas prováveis**, baseadas no perfil:
        
        ### **Título da vaga**
        - **Por que combina com o usuário**
        - **Principais competências exigidas**
        - **Nível estimado:** júnior/pleno/sênior
        
        ---
        
        ## 🚀 Plano de Evolução Profissional (Opcional, mas recomendado)
        Crie um plano curto com:
        
        1. **Habilidade a reforçar**
        2. **Habilidade a adquirir**
        3. **Próximos passos para crescer no cargo atual**
        
        ---
        
        # 📏 Regras Importantes
        
        1. **Nunca utilize JSON. A saída deve ser exclusivamente Markdown.**
        2. A linguagem deve ser profissional, motivadora e clara.
        3. Não invente tecnologias irreais; mantenha recomendações plausíveis.
        4. Ajuste cursos e vagas com base no cargo informado.
        5. Gere recomendações práticas e aplicáveis, não genéricas.
        6. Seja objetivo, mas detalhado o suficiente para ser útil.
        7. Evite repetir conteúdos entre cursos e vagas.
        
        ---
        
        # 🛠️ Exemplo de Entrada
        Cargo: Desenvolvedor Back-end
        Habilidades: Java, Spring Boot, SQL
        
        ---
        
        # 🛠️ Exemplo de Saída (apenas formato)
        ## 📌 Perfil Analisado
        - **Cargo:** Desenvolvedor Back-end
        - **Habilidades-chave:** Java • Spring Boot • SQL
        
        ---
        
        ## 🎓 Recomendações de Cursos
        ### **Arquitetura de Microsserviços com Spring Cloud**
        - **Por que é relevante:** complementa o domínio atual e prepara para sistemas distribuídos.
        - **Você vai aprender:** Circuit Breaker, Eureka, API Gateway, observabilidade.
        - **Plataforma:** Alura
        
        ### **SQL Avançado para Produtividade**
        - **Por que é relevante:** melhora a performance e otimização de queries do backend.
        - **Você vai aprender:** Indexação, queries complexas, tuning.
        - **Plataforma:** Udemy
        
        ---
        
        ## 💼 Sugestões de Vagas Ideais
        ### **Desenvolvedor Back-end Pleno (Java)**
        - **Por que combina:** exige exatamente as tecnologias dominadas.
        - **Competências:** Java, Spring Boot, APIs REST, SQL.
        - **Nível:** Pleno
        
        ### **Engenheiro de Software – Sistemas Distribuídos**
        - **Por que combina:** foco em backend robusto e escalável.
        - **Competências:** Java, microsserviços, mensageria.
        - **Nível:** Pleno/Sênior
        
        ---
        
        ## 🚀 Plano de Evolução Profissional
        1. **Reforçar:** APIs REST avançadas
        2. **Adquirir:** Docker e Kubernetes
        3. **Próximos passos:** contribuir em arquitetura de serviços internos
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


    public Page<Recomendacao> listarRecomendacoesPaginadas(String login, Pageable pageable) {
        return recomendacaoRepository.findByUsuario_LoginUsuario_Login(login, pageable);
    }

    public void deletarRecomendacao(Long idRecomendacao) throws IdNaoEncontradoException {
        Recomendacao recomendacao = recomendacaoRepository.findById(idRecomendacao)
                .orElseThrow(()-> new IdNaoEncontradoException("Recomendação com id: " + idRecomendacao + " não encontrada!"));
        recomendacaoRepository.delete(recomendacao);
    }
}
