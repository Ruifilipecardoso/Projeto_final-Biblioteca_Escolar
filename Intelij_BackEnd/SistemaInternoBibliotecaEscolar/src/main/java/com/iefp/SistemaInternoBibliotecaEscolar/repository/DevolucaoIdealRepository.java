package com.iefp.SistemaInternoBibliotecaEscolar.repository;

import com.iefp.SistemaInternoBibliotecaEscolar.model.DevolucaoIdeal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DevolucaoIdealRepository extends JpaRepository<DevolucaoIdeal, Integer> {
    Optional <DevolucaoIdeal> findByEmprestimoIdEmprestimo(Integer idEmprestimo);
}
