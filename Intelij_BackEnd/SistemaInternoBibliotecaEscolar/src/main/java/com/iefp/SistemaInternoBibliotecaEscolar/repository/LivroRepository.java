package com.iefp.SistemaInternoBibliotecaEscolar.repository;

import com.iefp.SistemaInternoBibliotecaEscolar.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LivroRepository extends JpaRepository<Livro, Integer> {
    Optional<Livro> findByIsbn (String isbn);
}
