package com.iefp.SistemaInternoBibliotecaEscolar.controller;

import com.iefp.SistemaInternoBibliotecaEscolar.model.Aluno;
import com.iefp.SistemaInternoBibliotecaEscolar.service.AlunoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*") // Isto autoriza o VsCode a ligar-se ao Back-End//
@RestController
@RequestMapping("/api/alunos")
public class AlunoController {
    private final AlunoService alunoService;

    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @GetMapping
    public ResponseEntity<List<Aluno>> listarTodosAlunos() {
        List<Aluno> alunos = alunoService.listarTodosAlunos();
        return ResponseEntity.ok(alunos);
    }

    /*@RequestBody: Diz ao Spring que os dados do aluno vêm dentro do "corpo" do pedido no formato JSON
     (uma estrutura completa com nome, número de estudante, email, etc.).
     O Spring lê esse JSON e transforma-o automaticamente num objeto Aluno do Java.*/
    @PostMapping("/registarAluno")
    public ResponseEntity<?> registarAluno(@RequestBody Aluno aluno) {
        try {
            Aluno alunoSalvo = alunoService.guardar(aluno);
            return ResponseEntity.ok(alunoSalvo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /*@PutMapping: É o metodo HTTP padrão na internet para fazer atualizações de dados que já existem.*/
    /*@PathVariable: Captura um valor diretamente do endereço da rota. No endereço /api/alunos/{id}/status,
     se escreveres /api/alunos/5/status, o Spring sabe que o id é 5.*/
    @PutMapping("/{id}/status")
    public ResponseEntity<?> alterarStatus(@PathVariable Integer id, @RequestParam String novoStatus) {
        try {
            alunoService.atualizarStatus(id, novoStatus);
            return ResponseEntity.ok("Estatuto do aluno atualizado com sucesso para: " + novoStatus);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
