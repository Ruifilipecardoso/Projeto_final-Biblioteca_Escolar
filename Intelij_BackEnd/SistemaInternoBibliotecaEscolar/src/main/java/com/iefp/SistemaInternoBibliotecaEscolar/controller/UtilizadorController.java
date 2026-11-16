package com.iefp.SistemaInternoBibliotecaEscolar.controller;

import com.iefp.SistemaInternoBibliotecaEscolar.dto.LoginRequest;
import com.iefp.SistemaInternoBibliotecaEscolar.model.Utilizador;
import com.iefp.SistemaInternoBibliotecaEscolar.security.JwtService;
import com.iefp.SistemaInternoBibliotecaEscolar.service.UtilizadorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController //Este Controller não devolve ecrãs ou páginas HTML; devolve apenas dados puros//
@RequestMapping("/api/utilizadores") //Esta anotação define o endereço base (a rota) para aceder a este controlador na internet.
public class UtilizadorController {
    private final UtilizadorService utilizadorService;
    private final JwtService jwtService;

    public UtilizadorController(UtilizadorService utilizadorService, JwtService jwtService) {
        this.utilizadorService = utilizadorService;
        this.jwtService = jwtService;
    }

    @GetMapping //O @RequestMapping já trata da rota para esta página
    public ResponseEntity<List<Utilizador>> listarTodosUtilizadores() {
        List<Utilizador> utilizadors = utilizadorService.listarTodosUtilizadores();

        return ResponseEntity.ok(utilizadors);
    }
    /*ResponseEntity:
    maginas que a resposta do teu servidor é uma encomenda postal.

    Se devolveres apenas o objeto puro (por exemplo, retornar apenas um Utilizador), estás a enviar o produto solto, sem caixa e sem etiqueta de envio.

    O ResponseEntity é a caixa da encomenda.

    Ele serve para tu juntares duas coisas obrigatórias em qualquer comunicação na internet:

    A etiqueta com o código de estado (Status HTTP): É um número que diz ao ecrã (ou ao Postman) o que aconteceu, antes de ele ler
    os dados.200 OK: "Correu tudo bem, abre a caixa."400 Bad Request: "Houve um erro no pedido, lê a mensagem de erro."404 Not Found: "O que pediste não existe."

    O conteúdo dentro da caixa (O corpo ou Body): É o dado real que queres entregar (a lista de utilizadores, o utilizador autenticado, ou o texto do erro).

    No código, quando fazes:javareturn ResponseEntity.ok(utilizadores);
    Use o código com cuidado.Estás a montar uma caixa que diz: Código 200 (Sucesso) e lá dentro vai a lista de utilizadores.

    Quando fazes no catch:javareturn ResponseEntity.badRequest().body(e.getMessage());
    Use o código com cuidado.Estás a montar uma caixa que diz: Código 400 (Erro) e lá dentro vai o texto do erro.*/
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginData, jakarta.servlet.http.HttpServletResponse response) {
        try {
            //Autentica email e senha
            Utilizador utilizador = utilizadorService.autenticar(loginData.getEmail(), loginData.getSenha());

            //Gerar Token
            String token = jwtService.gerarToken( utilizador.getIdUtilizador(), utilizador.getEmail(), utilizador.getPerfil());

            //Criar Cookie seguro (HttpOnly + SameSite=Lax)
            org.springframework.http.ResponseCookie jwtCookie = org.springframework.http.ResponseCookie.from("access_token", token)
                    .httpOnly(true)    //Bloqueia Leitura por Javascript (anti-XSS)
                    .secure(false)      //False em LocalHost (HTTP); mudar para true em produção (HTTPS)
                    .path("/")          //Válido em todas as lojas do site
                    .maxAge(86400)  //Expira em 24 horas
                    .sameSite("Lax")    //Proteção contra CSRF
                    .build();


            //Envia o Cookie no cabeçalho Set-Cookie
            response.addHeader(org.springframework.http.HttpHeaders.SET_COOKIE, jwtCookie.toString());

            //RESPOSTA LIMPA: Devolvemos um JSON apenas com dados públicos úteis para o Frontend construir o menu
            return ResponseEntity.ok(java.util.Map.of(
                    "idUtilizador", utilizador.getIdUtilizador(),
                    "email", utilizador.getEmail(),
                    "perfil", utilizador.getPerfil(),
                    "imagemPerfil", utilizador.getImagemPerfil() != null ? utilizador.getImagemPerfil() : ""
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("erro", e.getMessage()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> obterUtilizadorAtual(
            @org.springframework.security.core.annotation.AuthenticationPrincipal Utilizador utilizador) {

        if (utilizador == null) {
            return ResponseEntity.status(401).body(java.util.Map.of("erro", "Sessão inválida ou expirada."));
        }

        return ResponseEntity.ok(java.util.Map.of(
                "idUtilizador", utilizador.getIdUtilizador(),
                "email", utilizador.getEmail(),
                "perfil", utilizador.getPerfil(),
                "imagemPerfil", utilizador.getImagemPerfil() != null ? utilizador.getImagemPerfil() : ""
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(jakarta.servlet.http.HttpServletResponse response) {
        org.springframework.http.ResponseCookie cookieLimpo = org.springframework.http.ResponseCookie.from("access_token", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        response.addHeader(org.springframework.http.HttpHeaders.SET_COOKIE, cookieLimpo.toString());
        return ResponseEntity.ok(java.util.Map.of("mensagem", "Sessão terminada com sucesso."));
    }
}

