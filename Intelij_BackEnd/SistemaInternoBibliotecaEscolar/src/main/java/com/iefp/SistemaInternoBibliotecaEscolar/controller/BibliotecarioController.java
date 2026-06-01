package com.iefp.SistemaInternoBibliotecaEscolar.controller;

import com.iefp.SistemaInternoBibliotecaEscolar.dto.BibliotecarioResponse;
import com.iefp.SistemaInternoBibliotecaEscolar.dto.UtilizadorMinimo;
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
    public ResponseEntity<List<BibliotecarioResponse>> listarTodosBibliotecarios() {
        List<Bibliotecario> bibliotecarios = bibliotecarioService.listarTodosBibliotecarios();

        List<BibliotecarioResponse> resposta = bibliotecarios.stream().map(b -> {
            UtilizadorMinimo utilizadorMinimo = null;
            if (b.getUtilizador() != null) {
                utilizadorMinimo = new UtilizadorMinimo(
                        b.getUtilizador().getIdUtilizador(),
                        b.getUtilizador().getImagemPerfil()
                );
            }

            return new BibliotecarioResponse(
                    b.getIdBibliotecario(),
                    b.getNome(),
                    b.getNumeroEscolar(),
                    utilizadorMinimo
            );
        }).toList();

        return ResponseEntity.ok(resposta);
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
