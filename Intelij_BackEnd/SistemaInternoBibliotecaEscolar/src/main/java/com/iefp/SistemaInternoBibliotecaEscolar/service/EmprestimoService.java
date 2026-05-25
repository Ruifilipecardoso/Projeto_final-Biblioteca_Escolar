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

    public EmprestimoService(EmprestimoRepository emprestimoRepository,
                             LinhaLivrosRepository linhaLivrosRepository,
                             DevolucaoIdealRepository devolucaoIdealRepository, LivroService livroService) {
        this.emprestimoRepository = emprestimoRepository;
        this.linhaLivrosRepository = linhaLivrosRepository;
        this.devolucaoIdealRepository = devolucaoIdealRepository;
        this.livroService = livroService;
    }

    @Transactional
    public Emprestimo solicitarEmprestimo(Aluno aluno, List<Livro> livrosSolicitados) {
        if ("Negativo".equalsIgnoreCase(aluno.getStatus())) {
            throw new RuntimeException("Solicitação recusada: O teu estatuto encontra-se como 'Negativo'.");
        }

        Emprestimo solicitacao = new Emprestimo();
        solicitacao.setAluno(aluno);
        solicitacao.setData(LocalDate.now());
        solicitacao.setHora(LocalTime.now());
        solicitacao.setEstadoEmprestimo("Solicitado");

        Emprestimo guardado = emprestimoRepository.save(solicitacao);

        for (Livro livro : livrosSolicitados) {
            LinhaLivros linha = new LinhaLivros();
            linha.setEmprestimo(guardado);
            linha.setLivro(livro);
            linha.setQualidade("Solicitado");
            linhaLivrosRepository.save(linha);
        }

        return guardado;
    }



    @Transactional
    public Emprestimo aprovarSolicitacao(Integer idEmprestimo, Bibliotecario bibliotecario,
                                         List<Livro> livrosAprovados, LocalDate dataFimSugerido,
                                          LocalTime horaFimSugerido) {

        Emprestimo emprestimo = emprestimoRepository.findById(idEmprestimo)
                .orElseThrow(() -> new RuntimeException("Solicitação não encontrada."));

        Aluno aluno = emprestimo.getAluno();

        if ("Negativo".equalsIgnoreCase(aluno.getStatus())) {
            throw new RuntimeException("Empréstimo recusado: O aluno encontra-se com o status 'Negativo'" +
                    " e deve regularizar a sua situação.");
        }

        List<Emprestimo> historicoEmprestimos = emprestimoRepository.findByAlunoIdAluno(aluno.getIdAluno());

        long emprestimosAtivos = historicoEmprestimos.stream()
                .filter(e -> !"Devolvido".equalsIgnoreCase(e.getEstadoEmprestimo())).count();

        if (emprestimosAtivos >= 3) {
            throw new RuntimeException("Empréstimo recusado: O aluno já atingiu o limite máximo de 3 empréstimos ativos em aberto.");
        }

        long totalLivrosNaPosse = historicoEmprestimos.stream()
                .filter(e -> !"Devolvido".equalsIgnoreCase(e.getEstadoEmprestimo()))
                .flatMap(e -> e.getLinhaLivros().stream()).count();

        if ((totalLivrosNaPosse + livrosAprovados.size()) > 12) {
            throw new RuntimeException("Empréstimo recusado: O limite de 12 livros em posse será excedido." +
                    " O aluno tem atualmente "
                    + totalLivrosNaPosse + " livro(s) e tenta requisitar mais " + livrosAprovados.size() + ".");
        }

        if (livrosAprovados.size() > 4) {
            throw new RuntimeException("Empréstimo recusado: Não é permitido requisitar mais de 4 livros num único empréstimo.");
        }

        for (Livro livro : livrosAprovados) {
            if (livro.getStockAtual() <= 0) {

                LocalDate dataMaisProxima = devolucaoIdealRepository.findAll().stream()
                        .filter(di -> "Pendente".equalsIgnoreCase(di.getEstadoDevolucao()))
                        .filter(di -> di.getEmprestimo().getLinhaLivros().stream()
                                .anyMatch(linha -> linha.getLivro().getIdLivro().equals(livro.getIdLivro())))
                        .map(DevolucaoIdeal::getData).min(LocalDate::compareTo).orElse(LocalDate.now().plusDays(7));

                throw new RuntimeException("Empréstimo recusado: O livro '" + livro.getTitulo()
                        + "' está esgotado. Próxima entrega prevista por outro aluno em: " + dataMaisProxima);
            }

            int prioridadeDestePediido = livrosAprovados.size();

            long pedidosComMaiorPrioridade = emprestimoRepository.findAll().stream()
                    .filter(e -> !"Devolvido".equalsIgnoreCase(e.getEstadoEmprestimo()))
                    .filter(e -> e.getData().equals(LocalDate.now()))
                    .filter(e -> e.getLinhaLivros().stream().anyMatch(linha -> linha.getLivro().getIdLivro().equals(livro.getIdLivro())))
                    .filter(e -> e.getLinhaLivros().size() < prioridadeDestePediido).count();

            if (livro.getStockAtual() <= pedidosComMaiorPrioridade) {
                throw new RuntimeException("Empréstimo recusado: A cópia do livro '" + livro.getTitulo()
                        + "' está reservada para uma solicitação com maior prioridade de especificidade (pedido com menos livros).");
            }
        }

        emprestimo.setData(emprestimo.getData());
        emprestimo.setHora(emprestimo.getHora());
        emprestimo.setEstadoEmprestimo(emprestimo.getData().isAfter(LocalDate.now()) ? "Agendado" : "Ativo");
        emprestimo.setAluno(aluno);
        emprestimo.setBibliotecario(bibliotecario);

        Emprestimo emprestimoSalvo = emprestimoRepository.save(emprestimo);


        List<LinhaLivros> linhasAntigas = linhaLivrosRepository.findByEmprestimoIdEmprestimo(idEmprestimo);
        linhaLivrosRepository.deleteAll(linhasAntigas);

        int menorPrazoCalculado = 30;

        for (Livro livro : livrosAprovados) {
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

        if (dataFimSugerido != null) {
            devolucaoIdeal.setData(dataFimSugerido);
            devolucaoIdeal.setHora(horaFimSugerido != null ? horaFimSugerido : LocalTime.of(18, 0));
        } else {
            devolucaoIdeal.setData(emprestimoSalvo.getData().plusDays(menorPrazoCalculado));
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
