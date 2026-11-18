package com.iefp.SistemaInternoBibliotecaEscolar.repository;

import com.iefp.SistemaInternoBibliotecaEscolar.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlunoRepository extends JpaRepository<Aluno, Integer> {
    Optional<Aluno> findByUtilizadorIdUtilizador(Integer idUtilizador);
}
