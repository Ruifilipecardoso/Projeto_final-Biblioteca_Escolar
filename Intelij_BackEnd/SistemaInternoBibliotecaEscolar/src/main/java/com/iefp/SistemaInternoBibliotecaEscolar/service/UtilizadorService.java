package com.iefp.SistemaInternoBibliotecaEscolar.service;

import com.iefp.SistemaInternoBibliotecaEscolar.model.Utilizador;
import com.iefp.SistemaInternoBibliotecaEscolar.repository.UtilizadorRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtilizadorService {
    private final UtilizadorRepository utilizadorRepository;

    public UtilizadorService(UtilizadorRepository utilizadorRepository) {
        this.utilizadorRepository = utilizadorRepository;
    }

    public void guardar(Utilizador utilizador) {
        try {
            utilizadorRepository.save(utilizador);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Esta palavra-passe já está a ser utilizada!");
        }

    }

    public List<Utilizador> listarTodosUtilizadores() {
        return utilizadorRepository.findAll();
    }

    public Utilizador autenticar(String email, String senha) {
        return utilizadorRepository.findByEmailAndSenha(email, senha);
    }
}
