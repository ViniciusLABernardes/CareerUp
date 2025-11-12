package com.br.CareerUp.security;

import com.br.CareerUp.service.UsuarioDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    @Autowired
    private final UsuarioDetailsService usuarioUserDetailsService;

    @Bean
    public UserDetailsService userDetailsService() {
        return usuarioUserDetailsService;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(usuarioUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)

                .formLogin(form -> form
                        .loginPage("/login").permitAll()
                        .defaultSuccessUrl("/index", true)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/cadastro").permitAll()
                        .requestMatchers("/usuario/salvar").permitAll()
                        .requestMatchers("/recomendacao/**").hasAnyRole("USUARIO","GERENTE")
                        .requestMatchers("/usuario/cadastro").hasAnyRole("USUARIO","GERENTE")
                        .requestMatchers("/usuario/listar").hasAnyRole("USUARIO","GERENTE")
                        .requestMatchers("/usuario/alterar-cargo").hasRole("GERENTE")
                        .requestMatchers("/dashboard-gerente/**").hasRole("GERENTE")

                        .anyRequest().authenticated()
                )
                .build();
    }
}
