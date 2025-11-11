package com.br.CareerUp.service;



import com.br.CareerUp.model.Usuario;
import com.br.CareerUp.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByLogin_Login(login)
                .orElseThrow(() -> new UsernameNotFoundException("Login não encontrado: " + login));



        return User.builder()
                .username(usuario.getLogin().getLogin())
                .password(usuario.getLogin().getSenha())
                .roles(usuario.getCargo().toUpperCase())
                .build();

    }
}
