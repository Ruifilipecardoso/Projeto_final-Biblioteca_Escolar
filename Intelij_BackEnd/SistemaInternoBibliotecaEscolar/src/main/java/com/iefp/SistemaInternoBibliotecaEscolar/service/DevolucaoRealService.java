package com.iefp.SistemaInternoBibliotecaEscolar.service;

import com.iefp.SistemaInternoBibliotecaEscolar.model.*;
import com.iefp.SistemaInternoBibliotecaEscolar.repository.DevolucaoIdealRepository;
import com.iefp.SistemaInternoBibliotecaEscolar.repository.DevolucaoRealRepository;
import com.iefp.SistemaInternoBibliotecaEscolar.repository.EmprestimoRepository;
import com.iefp.SistemaInternoBibliotecaEscolar.repository.LinhaLivrosRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class DevolucaoRealService {
    private final DevolucaoRealRepository devolucaoRealRepository;
    private final DevolucaoIdealRepository devolucaoIdealRepository;
    private final EmprestimoRepository emprestimoRepository;
    private final LinhaLivrosRepository linhaLivrosRepository;
    private final LivroService livroService;
    private final AlunoService alunoService;

    public DevolucaoRealService(DevolucaoRealRepository devolucaoRealRepository,
                                DevolucaoIdealRepository devolucaoIdealRepository,
                                EmprestimoRepository emprestimoRepository,
                                LinhaLivrosRepository linhaLivrosRepository,
                                LivroService livroService, AlunoService alunoService) {
        this.devolucaoRealRepository = devolucaoRealRepository;
        this.devolucaoIdealRepository = devolucaoIdealRepository;
        this.emprestimoRepository = emprestimoRepository;
        this.linhaLivrosRepository = linhaLivrosRepository;
        this.livroService = livroService;
        this.alunoService = alunoService;
    }

    @Transactional
    public DevolucaoReal processarDevolucao(Integer idEmprestimo, Integer idLivro,  String qualidadeEntrega, String statusSugeridoBibliotecario) {
        Emprestimo emprestimo = emprestimoRepository.findById(idEmprestimo).orElseThrow(() -> new RuntimeException("Empréstimo não encontrado com o ID: " + idEmprestimo));

        LinhaLivrosChaveComposta chaveComposta = new LinhaLivrosChaveComposta(idLivro, idEmprestimo);
        LinhaLivros linhaEspecifica = linhaLivrosRepository.findById(chaveComposta).orElseThrow(() -> new RuntimeException("Este livro não pertence a este empréstimo ou já foi processado."));

        if (!"Impecável".equalsIgnoreCase(linhaEspecifica.getQualidade())) {
            throw new RuntimeException("Este livro já foi devolvido anteriormente.");
        }

        DevolucaoIdeal ideal = devolucaoIdealRepository.findByEmprestimoIdEmprestimo(idEmprestimo).orElseThrow(() -> new RuntimeException("Prazo ideal não encontrado para este empréstimo."));

        LocalDate hoje = LocalDate.now();

        long prazoOriginalDias = ChronoUnit.DAYS.between(emprestimo.getData(), ideal.getData());
        if (prazoOriginalDias <= 0) {
            prazoOriginalDias = 7;
        }

        LocalDate dataLimiteTolerancia = ideal.getData().plusDays(prazoOriginalDias);

        Aluno aluno = emprestimo.getAluno();

        if (hoje.isAfter(dataLimiteTolerancia)) {
            alunoService.atualizarStatus(aluno.getIdAluno(), "Negativo");
        } else {
            String statusFinal;

            if (statusSugeridoBibliotecario != null && !statusSugeridoBibliotecario.trim().isEmpty()) {
                statusFinal = statusSugeridoBibliotecario;
            } else {
                if ("Inutilizável".equalsIgnoreCase(qualidadeEntrega)) {
                    statusFinal = "Negativo";
                } else if ("Deformado".equalsIgnoreCase(qualidadeEntrega)) {
                    statusFinal = "Regular";
                } else {
                    statusFinal = "Bom";
                }

                alunoService.atualizarStatus(aluno.getIdAluno(), statusFinal);
            }
        }

        linhaEspecifica.setQualidade(qualidadeEntrega);
        linhaLivrosRepository.save(linhaEspecifica);

        if ("Inutilizavel".equalsIgnoreCase(qualidadeEntrega)) {
            livroService.abaterLivroInutilizavel(idLivro);
        } else {
            livroService.aumentarStock(idLivro);
        }

        List<LinhaLivros> todasAsLinhas = linhaLivrosRepository.findByEmprestimoIdEmprestimo(idEmprestimo);
        boolean aindaTemLivrosPendentes = todasAsLinhas.stream().anyMatch(l -> "Impecável".equalsIgnoreCase(l.getQualidade()));

        if (!aindaTemLivrosPendentes) {
            emprestimo.setEstadoEmprestimo("Devolvido");
            emprestimoRepository.save(emprestimo);

            ideal.setEstadoDevolucao("Concluído");
            devolucaoIdealRepository.save(ideal);
        }

        DevolucaoReal devolucaoReal = new DevolucaoReal();
        devolucaoReal.setEmprestimo(emprestimo);
        devolucaoReal.setData(hoje);
        devolucaoReal.setHora(LocalTime.now());
        devolucaoReal.setQualidade(qualidadeEntrega);

        return devolucaoRealRepository.save(devolucaoReal);
    }
}
