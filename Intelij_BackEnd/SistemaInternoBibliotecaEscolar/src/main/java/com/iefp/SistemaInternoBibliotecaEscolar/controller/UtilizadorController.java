package com.iefp.SistemaInternoBibliotecaEscolar.controller;

import com.iefp.SistemaInternoBibliotecaEscolar.model.Utilizador;
import com.iefp.SistemaInternoBibliotecaEscolar.service.UtilizadorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController //Este Controller não devolve ecrãs ou páginas HTML; devolve apenas dados puros//
@RequestMapping("/api/utilizadores") //Esta anotação define o endereço base (a rota) para aceder a este controlador na internet.
public class UtilizadorController {
    private final UtilizadorService utilizadorService;

    public UtilizadorController(UtilizadorService utilizadorService) {
        this.utilizadorService = utilizadorService;
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
    public ResponseEntity<?> login(@RequestParam String nome, @RequestParam String email, @RequestParam String senha) {
        try {
            Utilizador utilizadorAutenticado = utilizadorService.autenticar(nome, email, senha);
            return ResponseEntity.ok(utilizadorAutenticado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

