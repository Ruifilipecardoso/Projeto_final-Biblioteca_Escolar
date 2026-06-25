package com.iefp.SistemaInternoBibliotecaEscolar.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    private final CustomUserDetailService userDetailService;

    public JwtAuthenticationFilter(JwtService jwtService, CustomUserDetailService userDetailService) {
        this.jwtService = jwtService;
        this.userDetailService = userDetailService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        String token = null;

        //Vai buscar todos os cookies que vieram na requisição do browser
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("access_token".equals(cookie.getName())) {
                    token = cookie.getValue();
                }
            }
        }

        //Se não encontrou nenhum token, deixa o pedido seguir (o Spring barra mais à frente se a rota for protegida)
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String userEmail = jwtService.extrairEmail(token);

        //Se encontrou o email e o utilizador ainda não estiver autenticado nesta requisição
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailService.loadUserByUsername(userEmail);

            if (jwtService.isTokenValido(token, userDetails.getUsername())) {
                //Cria a autorização oficial do Spring Security
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //Salva a autorização no contexto do sistema (O utilizador está oficialmente "logado")
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        //Deixa o pedido continuar para o Controller correspondente
        filterChain.doFilter(request, response);
    }

}
