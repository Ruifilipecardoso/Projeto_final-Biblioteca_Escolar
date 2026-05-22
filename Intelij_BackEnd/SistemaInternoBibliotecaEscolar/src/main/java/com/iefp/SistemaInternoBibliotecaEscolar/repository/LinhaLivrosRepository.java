package com.iefp.SistemaInternoBibliotecaEscolar.repository;

import com.iefp.SistemaInternoBibliotecaEscolar.model.LinhaLivros;
import com.iefp.SistemaInternoBibliotecaEscolar.model.LinhaLivrosChaveComposta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LinhaLivrosRepository extends JpaRepository<LinhaLivros, LinhaLivrosChaveComposta> {
    List <LinhaLivros>  findByEmprestimoIdEmprestimo(Integer idEmprestimo);
}
