package com.iefp.SistemaInternoBibliotecaEscolar.service;

import com.iefp.SistemaInternoBibliotecaEscolar.model.Aluno;
import com.iefp.SistemaInternoBibliotecaEscolar.model.Utilizador;
import com.iefp.SistemaInternoBibliotecaEscolar.repository.AlunoRepository;
import com.iefp.SistemaInternoBibliotecaEscolar.repository.UtilizadorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlunoService {
    private final AlunoRepository alunoRepository;
    private final UtilizadorRepository utilizadorRepository;

    public AlunoService(AlunoRepository alunoRepository, UtilizadorRepository utilizadorRepository) {
        this.alunoRepository = alunoRepository;
        this.utilizadorRepository = utilizadorRepository;
    }

    public Aluno guardar(Aluno aluno) {
        Utilizador utilizadorSalvo = utilizadorRepository.save(aluno.getUtilizador());

        aluno.setUtilizador(utilizadorSalvo);

        return alunoRepository.save(aluno);
    }

    public List<Aluno> listarTodosAlunos() {
        return alunoRepository.findAll();
    }

    public Optional<Aluno> buscarPorId(Integer id) {
        return alunoRepository.findById(id);
    }

    public void eliminar(Integer id) {
        alunoRepository.deleteById(id);
    }
}
