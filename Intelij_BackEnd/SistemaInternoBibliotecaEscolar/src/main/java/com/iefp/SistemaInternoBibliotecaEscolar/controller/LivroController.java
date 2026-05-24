package com.iefp.SistemaInternoBibliotecaEscolar.controller;

import com.iefp.SistemaInternoBibliotecaEscolar.model.Livro;
import com.iefp.SistemaInternoBibliotecaEscolar.service.LivroService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/livros")
public class LivroController {
    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @GetMapping
    public ResponseEntity<List<Livro>> listarTodosLivros() {
        List<Livro> livros = livroService.listarTodosLivros();
        return ResponseEntity.ok(livros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        return livroService.buscarPorId(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/registoLivro")
    public ResponseEntity<?> registoLivro(@RequestBody Livro livro) {
        try {
            livroService.guardar(livro);
            return ResponseEntity.ok(livro);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/abastecer")
    public ResponseEntity<?> abastecerStock(@PathVariable Integer id, @RequestParam int quantidade) {
        try {
            livroService.abastecerStock(id, quantidade);
            return ResponseEntity.ok("Stock abastecido com sucesso em +" + quantidade + " unidades.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
