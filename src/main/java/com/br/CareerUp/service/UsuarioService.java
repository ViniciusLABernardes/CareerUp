package com.br.CareerUp.service;

import com.br.CareerUp.dto.UsuarioRequestDto;
import com.br.CareerUp.exceptions.IdNaoEncontradoException;
import com.br.CareerUp.model.Habilidade;
import com.br.CareerUp.model.LoginUsuario;
import com.br.CareerUp.model.Usuario;
import com.br.CareerUp.repository.HabilidadeRepository;
import com.br.CareerUp.repository.LoginRepository;
import com.br.CareerUp.repository.UsuarioRepository;
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
    public Usuario salvarUsuario(UsuarioRequestDto usuarioRequestDto){
        Usuario user = Usuario.builder()
                .nomeUsuario(usuarioRequestDto.getNomeUsuario())
                .cpf(usuarioRequestDto.getCpf())
                .cargo(usuarioRequestDto.getCargo())
                .papel(usuarioRequestDto.getPapel())
                .build();

        usuarioRepository.save(user);

        LoginUsuario loginUsuario = new LoginUsuario();
        loginUsuario.setUsuario(user);
        loginUsuario.setLogin(usuarioRequestDto.getLoginUsuario().getLogin());
        loginUsuario.setSenha(passwordEncoder.encode(usuarioRequestDto.getLoginUsuario().getSenha()));

        loginRepository.save(loginUsuario);

        Habilidade habilidade = new Habilidade();
        habilidade.setUsuario(user);
        habilidade.setHabilidadePrimaria(usuarioRequestDto.getHabilidades().getHabilidadePrimaria());
        habilidade.setHabilidadeSecundaria(usuarioRequestDto.getHabilidades().getHabilidadeSecundaria());
        habilidade.setHabilidadeTerciaria(usuarioRequestDto.getHabilidades().getHabilidadeTerciaria());

        habilidadeRepository.save(habilidade);

        return user;

    }


    @Transactional
    public Usuario editarCargoUsuarioPorLogin(String login, String novoCargo) throws IdNaoEncontradoException {
        Usuario usuario = usuarioRepository.findByLoginUsuario_Login(login)
                .orElseThrow(() -> new IdNaoEncontradoException("Usuário não encontrado com o login: " + login));

        usuario.setCargo(novoCargo);
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarUsuarios(){
        return usuarioRepository.findAll();
    }

}
