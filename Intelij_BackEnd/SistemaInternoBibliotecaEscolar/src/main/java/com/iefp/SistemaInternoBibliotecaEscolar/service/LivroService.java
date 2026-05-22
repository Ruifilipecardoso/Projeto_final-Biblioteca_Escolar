package com.iefp.SistemaInternoBibliotecaEscolar.service;

import com.iefp.SistemaInternoBibliotecaEscolar.model.Livro;
import com.iefp.SistemaInternoBibliotecaEscolar.repository.LivroRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LivroService {
    private final LivroRepository livroRepository;

    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    public void guardar(Livro livro) {
        livroRepository.save(livro);
    }

    public List<Livro> listarTodosLivros() {
        return livroRepository.findAll();
    }

    public Optional<Livro> buscarPorId(Integer id) {
        return livroRepository.findById(id);
    }

    public void diminuirStock(Integer idLivro) {
        livroRepository.findById(idLivro).ifPresent(livro -> {
            if (livro.getStockAtual() > 0) {
                livro.setStockAtual(livro.getStockAtual() - 1);

                if (livro.getStockAtual() == 0) {
                    livro.setDisponibilidade("Indisponível");
                }
                livroRepository.save(livro);
            } else {
                throw new RuntimeException("Livro sem stock disponível");
            }
        });
    }

    public void aumentarStock(Integer idLivro) {
        livroRepository.findById(idLivro).ifPresent(livro -> {
            livro.setStockAtual(livro.getStockAtual() + 1);
            livro.setDisponibilidade("Disponível");
            livroRepository.save(livro);
        });
    }

    public void abaterLivroInutilizavel(Integer idLivro) {
        livroRepository.findById(idLivro).ifPresent(livro -> {
            if (livro.getStockTotal() > 0) {
                livro.setStockTotal(livro.getStockTotal() - 1);

                if (livro.getStockTotal() == 0) {
                    livro.setDisponibilidade("Indisponível");
                }
                livroRepository.save(livro);
            } else {
                throw new RuntimeException("Erro: O stock total deste livro já é zero.");
            }
        });
    }

    public void abastecerStock(Integer idLivro, int quantidadeNovasCopias) {
        if (quantidadeNovasCopias <= 0) {
            throw new RuntimeException("A quantidade a abastecer deve ser superior a zero.");
        }

        livroRepository.findById(idLivro).ifPresent(livro -> {
            livro.setStockAtual(livro.getStockAtual() + quantidadeNovasCopias);
            livro.setStockTotal(livro.getStockTotal() + quantidadeNovasCopias);

            livro.setDisponibilidade("Disponível");

            livroRepository.save(livro);
        });
    }
}
