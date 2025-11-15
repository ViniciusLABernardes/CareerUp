package com.br.CareerUp.service;

import com.br.CareerUp.dto.HabilidadeRequestDto;
import com.br.CareerUp.dto.UsuarioRequestDto;
import com.br.CareerUp.exceptions.IdNaoEncontradoException;
import com.br.CareerUp.model.Habilidade;
import com.br.CareerUp.model.LoginUsuario;
import com.br.CareerUp.model.Usuario;
import com.br.CareerUp.repository.HabilidadeRepository;
import com.br.CareerUp.repository.LoginRepository;
import com.br.CareerUp.repository.UsuarioRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private HabilidadeRepository habilidadeRepository;

    public Usuario buscarPorLogin(String login) {
        return usuarioRepository.findByLoginUsuario_Login(login)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    @Transactional
    public Usuario salvarUsuario(UsuarioRequestDto dto) {

        usuarioRepository.inserirUsuarioProcedure(
                dto.getNomeUsuario(),
                dto.getCpf(),
                dto.getEmail(),
                dto.getCargo(),
                dto.getPapel().name()
        );

        Usuario user = usuarioRepository
                .findByCpf(dto.getCpf())
                .orElseThrow(() -> new RuntimeException("Erro ao recuperar usuário inserido"));

        loginRepository.inserirLoginProcedure(
                user.getIdUsuario(),
                dto.getLoginUsuario().getLogin(),
                passwordEncoder.encode(dto.getLoginUsuario().getSenha())
        );

        habilidadeRepository.inserirHabilidadesProcedure(
                user.getIdUsuario(),
                dto.getHabilidades().getHabilidadePrimaria(),
                dto.getHabilidades().getHabilidadeSecundaria(),
                dto.getHabilidades().getHabilidadeTerciaria()
        );

        return user;
    }

    @Transactional
    @CacheEvict(value = "recomendacaoCache", key = "#idUsuario")
    public Usuario atualizarHabilidades(Long idUsuario, HabilidadeRequestDto habilidadeRequestDto) throws IdNaoEncontradoException {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IdNaoEncontradoException("Usuário não encontrado com o id: " + idUsuario));

        Habilidade habilidade = usuario.getHabilidades();

        habilidade.setHabilidadePrimaria(habilidadeRequestDto.getHabilidadePrimaria());
        habilidade.setHabilidadeSecundaria(habilidadeRequestDto.getHabilidadeSecundaria());
        habilidade.setHabilidadeTerciaria(habilidadeRequestDto.getHabilidadeTerciaria());

        return usuario;
    }

    @Transactional
    public Usuario editarCargoUsuarioPorLogin(Long idUsuario, String novoCargo) throws IdNaoEncontradoException {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IdNaoEncontradoException("Usuário não encontrado com o id: " + idUsuario));

        usuario.setCargo(novoCargo);
        return usuarioRepository.save(usuario);
    }

    public void deletarUsuario(Long idUsuario) throws IdNaoEncontradoException{
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(()-> new IdNaoEncontradoException("Usuário não encontado com o id: " + idUsuario));
        usuarioRepository.delete(usuario);
    }

    public Page<Usuario> listarUsuarios(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }

    public Usuario visualizarDadosUsuarioEspecifico(Long idUsuario) throws IdNaoEncontradoException{
        return usuarioRepository.findById(idUsuario).orElseThrow(()-> new IdNaoEncontradoException("Usuário não encontrado!"));
    }
}
