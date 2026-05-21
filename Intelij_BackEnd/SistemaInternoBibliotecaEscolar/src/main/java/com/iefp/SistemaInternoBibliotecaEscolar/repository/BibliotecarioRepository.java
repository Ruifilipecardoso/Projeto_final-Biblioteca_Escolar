package com.iefp.SistemaInternoBibliotecaEscolar.repository;

import com.iefp.SistemaInternoBibliotecaEscolar.model.Bibliotecario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BibliotecarioRepository extends JpaRepository<Bibliotecario, Integer> {
}
