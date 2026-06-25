package com.iefp.SistemaInternoBibliotecaEscolar.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                //CONFIGURAÇÃO DE CORS: Permite que o Frontend (VS Code) comunique com a API
                .cors(cors -> cors.configurationSource(request -> {
                    org.springframework.web.cors.CorsConfiguration config = new org.springframework.web.cors.CorsConfiguration();
                    config.setAllowedOriginPatterns(java.util.List.of("*"));
                    config.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(java.util.List.of("*"));
                    config.setAllowCredentials(true);
                    return config;
                }))

                //DESATIVAR CSRF: Como usamos uma API REST protegida por Cookies HttpOnly e sem estado, o CSRF é desativado
                .csrf(csrf -> csrf.disable())

                //SESSÃO STATELESS: O servidor não guarda dados na memória; cada requisição valida o Token
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                //CONTROLAR O ACESSO ÀS ROTAS (REQUEST MATCHERS)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/Utilizadores/login").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/emprestimos/solicitar").hasAnyRole("ALUNO", "ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/livros/**").hasAnyRole("ALUNO", "BIBLIOTECARIO", "ADMIN")

                        .requestMatchers("/api/emprestimos/solicitacoes").hasAnyRole("BIBLIOTECARIO", "ADMIN")
                        .requestMatchers("/api/emprestimos//aprovar").hasAnyRole("BIBLIOTECARIO", "ADMIN")
                        .requestMatchers("/api/devolucoes/**").hasAnyRole("BIBLIOTECARIO", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/livros//abastecer").hasAnyRole("BIBLIOTECARIO", "ADMIN")

                        .requestMatchers("/api/alunos/registoAluno", "/api/bibliotecarios/registoBibliotecario").hasAnyRole("ADMIN")
                        .requestMatchers("/api/utilizadores/**").hasRole("ADMIN")

                        .anyRequest().authenticated()

                )

                //INJETAR O NOSSO FILTRO PERSONALIZADO: Executa o nosso leitor de Cookies antes do filtro padrão do Spring
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    /*private final UserDetailsService userDetailsService;

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

        //System.out.println(new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode("bibliotecarioRuben1234"));

        builder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());

        return builder.build();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(request -> {
                    var config = new org.springframework.web.cors.CorsConfiguration();
                    config.setAllowedOrigins(java.util.List.of("*"));
                    config.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(java.util.List.of("*"));
                    return config;
                }))

                .csrf(csrf -> csrf.disable())


                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST,"/api/utilizadores/login").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/alunos", "/api/bibliotecarios", "/api/emprestimos/**", "/error").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/utilizadores").permitAll()


                        .requestMatchers(HttpMethod.POST, "/api/emprestimos/solicitar").hasAnyRole("ALUNO", "ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/livros/**").hasAnyRole("ALUNO", "BIBLIOTECARIO", "ADMIN")

                        .requestMatchers("/api/emprestimos/solicitacoes").hasAnyRole("BIBLIOTECARIO", "ADMIN")
                        .requestMatchers("/api/emprestimos//aprovar").hasAnyRole("BIBLIOTECARIO", "ADMIN")
                        .requestMatchers("/api/devolucoes/**").hasAnyRole("BIBLIOTECARIO", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/livros//abastecer").hasAnyRole("BIBLIOTECARIO", "ADMIN")

                        .requestMatchers("/api/alunos/registoAluno", "/api/bibliotecarios/registoBibliotecario").hasAnyRole("ADMIN")
                        .requestMatchers("/api/utilizadores/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )

                .httpBasic(httpBasic -> {});

        return http.build();
    }*/
}
