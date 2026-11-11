package com.iefp.SistemaInternoBibliotecaEscolar.security;

import com.iefp.SistemaInternoBibliotecaEscolar.model.Utilizador;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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

        try {
            Integer idUtilizador = jwtService.extrairIdUtilizador(token);

            if (idUtilizador != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                Utilizador utilizador = this.userDetailService.loadUserById(idUtilizador);

                if (jwtService.isTokenValido(token, utilizador.getIdUtilizador())) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            utilizador, null, utilizador.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
        }

        //Deixa o pedido continuar para o Controller correspondente
        filterChain.doFilter(request, response);
    }

}
