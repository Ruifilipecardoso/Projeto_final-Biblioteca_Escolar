package com.iefp.SistemaInternoBibliotecaEscolar.controller;


import com.iefp.SistemaInternoBibliotecaEscolar.dto.*;
import com.iefp.SistemaInternoBibliotecaEscolar.model.Aluno;
import com.iefp.SistemaInternoBibliotecaEscolar.model.Bibliotecario;
import com.iefp.SistemaInternoBibliotecaEscolar.model.Emprestimo;
import com.iefp.SistemaInternoBibliotecaEscolar.model.Livro;
import com.iefp.SistemaInternoBibliotecaEscolar.repository.EmprestimoRepository;
import com.iefp.SistemaInternoBibliotecaEscolar.service.AlunoService;
import com.iefp.SistemaInternoBibliotecaEscolar.service.BibliotecarioService;
import com.iefp.SistemaInternoBibliotecaEscolar.service.EmprestimoService;
import com.iefp.SistemaInternoBibliotecaEscolar.service.LivroService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/emprestimos")
public class EmprestimoController {
    private final EmprestimoService emprestimoService;
    private final AlunoService alunoService;
    private final BibliotecarioService bibliotecarioService;
    private final LivroService livroService;
    private final EmprestimoRepository emprestimoRepository;

    public EmprestimoController(EmprestimoService emprestimoService,
                                AlunoService alunoService,
                                BibliotecarioService bibliotecarioService,
                                LivroService livroService,
                                EmprestimoRepository emprestimoRepository) {
        this.emprestimoService = emprestimoService;
        this.alunoService = alunoService;
        this.bibliotecarioService = bibliotecarioService;
        this.livroService = livroService;
        this.emprestimoRepository = emprestimoRepository;
    }

    @PostMapping("/solicitar")
    public ResponseEntity<?> solicitarEmprestimo(@RequestBody EmprestimoRequest request) {
        try {
            Aluno aluno = alunoService.buscarPorId(request.getIdAluno())
                    .orElseThrow(() -> new RuntimeException("Erro: Aluno não encontrado no sistema."));

            List<Livro> livrosSolicitados = new ArrayList<>();
            for (Integer id : request.getIdLivros()) {
                Livro livro = livroService.buscarPorId(id)
                        .orElseThrow(() -> new RuntimeException("Erro: Livro com ID " + id + " não existe."));
                livrosSolicitados.add(livro);
            }

            Emprestimo solicitacao = emprestimoService.solicitarEmprestimo(aluno, livrosSolicitados);
            return ResponseEntity.ok(solicitacao);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/solicitacoes")
    public ResponseEntity<List<EmprestimoResponse>> listarSolicitacoesPendentes() {
        List<Emprestimo> todos = emprestimoService.listarTodosEmprestimos();

        List<EmprestimoResponse> resposta = todos.stream()
                .filter(e -> e.getEstadoEmprestimo() != null && e.getEstadoEmprestimo().equalsIgnoreCase("Solicitado"))
                .map(e -> {
                    List<LinhaLivroMinimo> livros = e.getLinhaLivros().stream()
                            .map(linha -> new LinhaLivroMinimo(
                                    new LivroMinimo(linha.getLivro().getTitulo())
                            )).toList();

                    DevolucaoIdealMinimo devolucaoIdealMinimo = null;
                    if (e.getDevolucaoIdeal() != null) {
                        devolucaoIdealMinimo = new DevolucaoIdealMinimo(e.getDevolucaoIdeal().getData());
                    }

                    return new EmprestimoResponse(
                            e.getIdEmprestimo(),
                            e.getAluno().getIdAluno(),
                            e.getData(),
                            e.getEstadoEmprestimo(),
                            livros,
                            devolucaoIdealMinimo,
                            e.getAluno().getNome(),
                            e.getAluno().getNumeroEscolar(),
                            e.getAluno().getStatus()
                    );
                }).toList();

        return ResponseEntity.ok(resposta);
    }

    @PutMapping("/{id}/aprovar")
    public ResponseEntity<?> aprovarSolicitacao(@PathVariable Integer id, @RequestBody EmprestimoRequest request) {
        try {
            Bibliotecario bibliotecario = bibliotecarioService.buscarPorId(request.getIdBibliotecario())
                    .orElseThrow(() -> new RuntimeException("Erro: Bibliotecário não encontrado no sistema."));

            List<Livro> livrosAprovados = new ArrayList<>();
            for (Integer idLivro : request.getIdLivros()) {
                Livro livro = livroService.buscarPorId(idLivro)
                        .orElseThrow(() -> new RuntimeException("Erro: Livro com ID " + idLivro + " inválido."));

                livrosAprovados.add(livro);
            }

            Emprestimo ativo = emprestimoService.aprovarSolicitacao(id, bibliotecario, livrosAprovados,
                    request.getDataFimSugerido(), request.getHoraFimSugerido());

            return ResponseEntity.ok(ativo);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/todos")
    public ResponseEntity<List<EmprestimoResponse>> listarTodosOsEmprestimos() {
       List<Emprestimo> todosEmprestimos = emprestimoService.listarTodosEmprestimos();

       List<EmprestimoResponse> reposta = todosEmprestimos.stream().map(emp -> {
           List<LinhaLivroMinimo> livros = emp.getLinhaLivros().stream()
                   .map(linha -> new LinhaLivroMinimo(
                        new LivroMinimo(linha.getLivro().getTitulo())
                   )).toList();

           String estado = emp.getEstadoEmprestimo() != null ? emp.getEstadoEmprestimo().toUpperCase() : "";
           DevolucaoIdealMinimo devolucaoIdeal = null;

           if (estado.equals("SOLICITADO")) {
               if (emp.getDevolucaoIdeal() != null) {
                   devolucaoIdeal = new DevolucaoIdealMinimo(emp.getDevolucaoIdeal().getData());
               }
           } else if (estado.equals("AGENDADO") || estado.equals("ATIVO")) {
               if (emp.getDevolucaoIdeal() != null) {
                   devolucaoIdeal = new DevolucaoIdealMinimo(emp.getDevolucaoIdeal().getData());
               } else {
                   devolucaoIdeal = new DevolucaoIdealMinimo(emp.getData().plusDays(14));
               }
           } else if (estado.equals("DEVOLVIDO")) {
               if (emp.getDevolucaoReal() != null) {
                   devolucaoIdeal = new DevolucaoIdealMinimo(emp.getDevolucaoReal().getData());
               } else if (emp.getDevolucaoIdeal() != null) {
                   devolucaoIdeal = new DevolucaoIdealMinimo(emp.getDevolucaoIdeal().getData());
               }
           }

           return new EmprestimoResponse(emp.getIdEmprestimo(),
                   emp.getAluno().getIdAluno(),
                   emp.getData(),
                   emp.getEstadoEmprestimo(),
                   livros,
                   devolucaoIdeal,
                   emp.getAluno().getNome(),
                   emp.getAluno().getNumeroEscolar(),
                   emp.getAluno().getStatus()
           );


       }).toList();

       return ResponseEntity.ok(reposta);
    }
}
