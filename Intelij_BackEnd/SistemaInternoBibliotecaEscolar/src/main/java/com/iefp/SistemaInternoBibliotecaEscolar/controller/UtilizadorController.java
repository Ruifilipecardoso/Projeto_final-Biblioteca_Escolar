package com.iefp.SistemaInternoBibliotecaEscolar.controller;

import com.iefp.SistemaInternoBibliotecaEscolar.dto.LoginRequest;
import com.iefp.SistemaInternoBibliotecaEscolar.model.Utilizador;
import com.iefp.SistemaInternoBibliotecaEscolar.security.JwtService;
import com.iefp.SistemaInternoBibliotecaEscolar.service.UtilizadorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(originPatterns = "*", allowCredentials = "true")
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
            //Passa apenas o email e a senha vindos do DTO
            Utilizador utilizador = utilizadorService.autenticar(loginData.getEmail(), loginData.getSenha());

            //Gerar Token
            String token = jwtService.gerarToken(utilizador.getEmail(), utilizador.getPerfil());

            //Criar Cookie
            jakarta.servlet.http.Cookie jwtCookie = new jakarta.servlet.http.Cookie("access_token", token);
            jwtCookie.setHttpOnly(true); //Bloqueia a leitura por JavaScript malicioso
            jwtCookie.setSecure(false); //Fica false para conseguirmos testar no nosso computador (HTTP)
            jwtCookie.setPath("/"); //O cookie fica disponível para todas as páginas e rotas do sistema
            jwtCookie.setMaxAge(86400); //Define que o login expira automaticamente em 24 horas (em segundos)

            //Enviar Cookie
            response.addCookie(jwtCookie);

            //RESPOSTA LIMPA: Devolvemos um JSON apenas com dados públicos úteis para o Frontend construir o menu
            return ResponseEntity.ok(java.util.Map.of(
                    "email", utilizador.getEmail(),
                    "perfil", utilizador.getPerfil(),
                    "imagemPerfil", utilizador.getImagemPerfil() != null ? utilizador.getImagemPerfil() : ""
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("erro", e.getMessage()));
        }
    }
}

