package com.iefp.SistemaInternoBibliotecaEscolar.controller;

import com.iefp.SistemaInternoBibliotecaEscolar.model.Bibliotecario;
import com.iefp.SistemaInternoBibliotecaEscolar.service.BibliotecarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/bibliotecarios")
public class BibliotecarioController {
    private final BibliotecarioService bibliotecarioService;

    public BibliotecarioController(BibliotecarioService bibliotecarioService) {
        this.bibliotecarioService = bibliotecarioService;
    }

    @GetMapping
    public ResponseEntity<List<Bibliotecario>> listarTodosBibliotecarios() {
        List<Bibliotecario> bibliotecarios = bibliotecarioService.listarTodosBibliotecarios();

        return ResponseEntity.ok(bibliotecarios);
    }

    @PostMapping("/reguistoBibliotecario")
    public ResponseEntity<?> registarBibliotecario(@RequestBody Bibliotecario bibliotecario) {
        try {
            Bibliotecario bibliotecarioSalvo = bibliotecarioService.guardar(bibliotecario);
            return ResponseEntity.ok(bibliotecarioSalvo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
