package com.iefp.SistemaInternoBibliotecaEscolar.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration /*O que faz:
Diz ao Spring Boot: "Atenção, esta classe não é uma tabela nem uma API comum. Isto é um Manual de Instruções ou um Painel de Controlo do sistema."
Por trás dos panos: Quando o teu programa arranca, o Spring Boot procura por esta etiqueta e lê tudo o que está lá dentro primeiro para saber como o teu projeto
 se deve comportar (neste caso, como deve gerir a segurança).*/
@EnableWebSecurity /*O que faz:
Liga o disjuntor geral da segurança web do Spring.
Por trás dos panos: Por padrão, o Spring Boot deixa as portas abertas. Ao colocares esta etiqueta, estás a ativar os "seguranças" (filtros) do Spring à porta de todas as rotas da tua aplicação.
A partir desse momento, nenhum pedido entra sem passar pela fiscalização do Java.*/
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);

        builder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());

        return builder.build();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

                .csrf(csrf -> csrf.disable())


                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/utilizadores/login").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/emprestimos/solicitar").hasAnyRole("ALUNO", "ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/livros/**").hasAnyRole("ALUNO", "BIBLIOTECARIO", "ADMIN")

                        .requestMatchers("/api/emprestimos/solicitacoes").hasAnyRole("BIBLIOTECARIOS", "ADMIN")
                        .requestMatchers("/api/emprestimos/*/aprovar").hasAnyRole("BIBLIOTECARIOS", "ADMIN")
                        .requestMatchers("/api/devolucoes/**").hasAnyRole("BIBLIOTECARIOS", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/livros/*/abastecer").hasAnyRole("BIBLIOTECARIOS", "ADMIN")

                        .requestMatchers("/api/alunos/registoAluno", "/api/bibliotecarios/registoBibliotecario").hasAnyRole("ADMIN")
                        .requestMatchers("/api/utilizadores/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )

                .httpBasic(httpBasic -> {});

        return http.build();
    }
}
