package com.iefp.SistemaInternoBibliotecaEscolar.service;

import com.iefp.SistemaInternoBibliotecaEscolar.model.*;
import com.iefp.SistemaInternoBibliotecaEscolar.repository.DevolucaoIdealRepository;
import com.iefp.SistemaInternoBibliotecaEscolar.repository.EmprestimoRepository;
import com.iefp.SistemaInternoBibliotecaEscolar.repository.LinhaLivrosRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class EmprestimoService {
    private final EmprestimoRepository emprestimoRepository;
    private final LinhaLivrosRepository linhaLivrosRepository;
    private final DevolucaoIdealRepository devolucaoIdealRepository;
    private final LivroService livroService;

    public EmprestimoService(EmprestimoRepository emprestimoRepository, LinhaLivrosRepository linhaLivrosRepository, DevolucaoIdealRepository devolucaoIdealRepository, LivroService livroService) {
        this.emprestimoRepository = emprestimoRepository;
        this.linhaLivrosRepository = linhaLivrosRepository;
        this.devolucaoIdealRepository = devolucaoIdealRepository;
        this.livroService = livroService;
    }

    @Transactional
    public Emprestimo criarEmprestimo(Aluno aluno, Bibliotecario bibliotecario, List<Livro> livrosParaEmprestar, LocalDate dataInicio, LocalTime horaInicio, LocalDate dataFimSugeriado, LocalTime horaFimSugerido) {
        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setData(dataInicio);
        emprestimo.setHora(horaInicio);
        emprestimo.setEstadoEmprestimo(dataInicio.isAfter(LocalDate.now()) ? "Agendado" : "Ativo");
        emprestimo.setAluno(aluno);
        emprestimo.setBibliotecario(bibliotecario);

        Emprestimo emprestimoSalvo = emprestimoRepository.save(emprestimo);


        int menorPrazoCalculado = 30;

        for (Livro livro : livrosParaEmprestar) {
            livroService.diminuirStock(livro.getIdLivro());

            double livrosEmprestados = livro.getStockTotal() - livro.getStockAtual();
            int popularidadeReal = 1 + (int) ((livrosEmprestados / livro.getStockTotal()) * 4);

            int prazoDinamicoDoLivro = calcularPrazoDinamico(livro.getStockAtual(), popularidadeReal);

            if (prazoDinamicoDoLivro < menorPrazoCalculado) {
                menorPrazoCalculado = prazoDinamicoDoLivro;
            }

            LinhaLivros linha = new LinhaLivros();
            linha.setEmprestimo(emprestimoSalvo);
            linha.setLivro(livro);
            linha.setQualidade("Impecável");

            linhaLivrosRepository.save(linha);
        }

        DevolucaoIdeal devolucaoIdeal = new DevolucaoIdeal();
        devolucaoIdeal.setEstadoDevolucao("Pendente");
        devolucaoIdeal.setEmprestimo(emprestimoSalvo);

        if (dataFimSugeriado != null) {
            devolucaoIdeal.setData(dataFimSugeriado);
            devolucaoIdeal.setHora(horaFimSugerido != null ? horaFimSugerido : LocalTime.of(18, 0));
        } else {
            devolucaoIdeal.setData(dataInicio.plusDays(menorPrazoCalculado));
            devolucaoIdeal.setHora(LocalTime.of(18, 0));
        }

        devolucaoIdealRepository.save(devolucaoIdeal);

        return emprestimoSalvo;
    }

    public List<Emprestimo> listarTodosEmprestimos() {
        return emprestimoRepository.findAll();
    }

    private int calcularPrazoDinamico(int stockAtual, int popularidade) {
        int prazoBase = 7;

        int prazoSugerido = prazoBase + (stockAtual * 2) - (popularidade * 2);

        if (prazoSugerido < 3) return 3;
        if (prazoSugerido > 30) return 30;

        return prazoSugerido;
    }
}
