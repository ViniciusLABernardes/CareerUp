package com.br.CareerUp.service;

import com.br.CareerUp.dto.UsuarioRequestDto;
import com.br.CareerUp.model.Habilidade;
import com.br.CareerUp.model.Login;
import com.br.CareerUp.model.Usuario;
import com.br.CareerUp.repository.HabilidadeRepository;
import com.br.CareerUp.repository.LoginRepository;
import com.br.CareerUp.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private HabilidadeRepository habilidadeRepository;

    public Usuario salvarUsuario(UsuarioRequestDto usuarioRequestDto){
        Usuario user = Usuario.builder()
                .nomeUsuario(usuarioRequestDto.getNomeUsuario())
                .cpf(usuarioRequestDto.getCpf())
                .cargo(usuarioRequestDto.getCargo())
                .build();

        usuarioRepository.save(user);

        Login login = new Login();
        login.setUsuario(user);
        login.setLogin(usuarioRequestDto.getLogin().getLogin());
        login.setSenha(usuarioRequestDto.getLogin().getSenha());

        loginRepository.save(login);

        Habilidade habilidade = new Habilidade();
        habilidade.setUsuario(user);
        habilidade.setHabilidadePrimaria(usuarioRequestDto.getHabilidades().getHabilidadePrimaria());
        habilidade.setHabilidadeSecundaria(usuarioRequestDto.getHabilidades().getHabilidadeSecundaria());
        habilidade.setHabilidadeTerciaria(usuarioRequestDto.getHabilidades().getHabilidadeTerciaria());

        habilidadeRepository.save(habilidade);

        return user;

    }


}
