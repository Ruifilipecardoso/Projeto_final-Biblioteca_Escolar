package com.iefp.SistemaInternoBibliotecaEscolar.security;


import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration /*O que faz:
Diz ao Spring Boot: "Atenção, esta classe não é uma tabela nem uma API comum. Isto é um Manual de Instruções ou um Painel de Controlo do sistema."
Por trás dos panos: Quando o teu programa arranca, o Spring Boot procura por esta etiqueta e lê tudo o que está lá dentro primeiro para saber como o teu projeto
 se deve comportar (neste caso, como deve gerir a segurança).*/
@EnableWebSecurity /*O que faz:
Liga o disjuntor geral da segurança web do Spring.
Por trás dos panos: Por padrão, o Spring Boot deixa as portas abertas. Ao colocares esta etiqueta, estás a ativar os "seguranças" (filtros) do Spring à porta de todas as rotas da tua aplicação.
A partir desse momento, nenhum pedido entra sem passar pela fiscalização do Java.*/
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    //CONFIGURAÇÃO DE CORS: Permite que o Frontend (VS Code) comunique com a API
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain publicFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/utilizadores/login", "/api/utilizadores/logout", "/error")
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                //DESATIVAR CSRF: Como usamos uma API REST protegida por Cookies HttpOnly e sem estado, o CSRF é desativado
                .csrf(csrf -> csrf.disable())

                //SESSÃO STATELESS: O servidor não guarda dados na memória; cada requisição valida o Token
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                //CONTROLAR O ACESSO ÀS ROTAS (REQUEST MATCHERS)
                .authorizeHttpRequests(auth -> auth

                        //Rotas públicas
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .dispatcherTypeMatchers(DispatcherType.ERROR, DispatcherType.FORWARD).permitAll()

                        //Rota para o utilizador receber os seus dados através do Cookie JWT
                        .requestMatchers(HttpMethod.GET, "/api/utilizadores/me").authenticated()

                        //Rotas de Aluno
                        .requestMatchers(HttpMethod.POST, "/api/emprestimos/solicitar").hasAnyRole("ALUNO", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/emprestimos/meus").hasRole("ALUNO")

                        //Consulta de livros
                        .requestMatchers(HttpMethod.GET, "/api/livros/**").hasAnyRole("ALUNO", "BIBLIOTECARIO", "ADMIN")

                        //Rotas de Biblioteário
                        .requestMatchers(HttpMethod.GET, "/api/emprestimos/todos").hasAnyRole("BIBLIOTECARIO", "ADMIN")
                        .requestMatchers("/api/emprestimos/solicitacoes").hasAnyRole("BIBLIOTECARIO", "ADMIN")
                        .requestMatchers("/api/emprestimos/*/aprovar").hasAnyRole("BIBLIOTECARIO", "ADMIN")
                        .requestMatchers("/api/devolucoes/**").hasAnyRole("BIBLIOTECARIO", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/livros/*/abastecer").hasAnyRole("BIBLIOTECARIO", "ADMIN")

                        //Rotas de Admin
                        .requestMatchers("/api/alunos/registarAluno", "/api/bibliotecarios/registarBibliotecario").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/utilizadores").hasRole("ADMIN")

                        .anyRequest().authenticated()

                )

                //INJETAR O NOSSO FILTRO PERSONALIZADO: Executa o nosso leitor de Cookies antes do filtro padrão do Spring
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
