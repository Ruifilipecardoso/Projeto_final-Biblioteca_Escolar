package com.iefp.SistemaInternoBibliotecaEscolar.controller;


import com.iefp.SistemaInternoBibliotecaEscolar.model.DevolucaoIdeal;
import com.iefp.SistemaInternoBibliotecaEscolar.service.DevolucaoIdealService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/prazos_ideais")
public class DevolucaoIdealController {
    private final DevolucaoIdealService devolucaoIdealService;

    public DevolucaoIdealController(DevolucaoIdealService devolucaoIdealService) {
        this.devolucaoIdealService = devolucaoIdealService;
    }

    @GetMapping
    public ResponseEntity<List<DevolucaoIdeal>> listarTodasDevolucoesIdeais() {
        List<DevolucaoIdeal> prazos = devolucaoIdealService.listarTodasDevolucoesIdeais();
        return ResponseEntity.ok(prazos);
    }

    @GetMapping("/emprestimo/{idEmprestimo}")
    public ResponseEntity<?> buscarPorEmprestimo(@PathVariable Integer idEmprestimo) {
        return devolucaoIdealService.buscarPorEmprestimo(idEmprestimo)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
