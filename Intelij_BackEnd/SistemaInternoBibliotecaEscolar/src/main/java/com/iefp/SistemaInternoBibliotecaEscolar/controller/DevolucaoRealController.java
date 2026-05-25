package com.iefp.SistemaInternoBibliotecaEscolar.controller;

import com.iefp.SistemaInternoBibliotecaEscolar.dto.DevolucaoRequest;
import com.iefp.SistemaInternoBibliotecaEscolar.model.DevolucaoReal;
import com.iefp.SistemaInternoBibliotecaEscolar.service.DevolucaoRealService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/devolucoes")
public class DevolucaoRealController {
    private final DevolucaoRealService devolucaoRealService;

    public DevolucaoRealController(DevolucaoRealService devolucaoRealService) {
        this.devolucaoRealService = devolucaoRealService;
    }

    @PutMapping("/emprestimo/{idEmprestimo}/devolver")
    public ResponseEntity<?> processarDevolucao(@PathVariable Integer idEmprestimo,
                                                @RequestBody DevolucaoRequest request) {

        try {
            DevolucaoReal registo = devolucaoRealService.processarDevolucao(
                    idEmprestimo,
                    request.getIdLivro(),
                    request.getQualidadeEntrega(),
                    request.getStatusSugeridoBibliotecário()
            );

            return ResponseEntity.ok(registo);

        } catch (RuntimeException e) {

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
