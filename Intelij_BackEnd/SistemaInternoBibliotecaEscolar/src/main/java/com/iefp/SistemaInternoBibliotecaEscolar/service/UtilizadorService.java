package com.iefp.SistemaInternoBibliotecaEscolar.service;

import com.iefp.SistemaInternoBibliotecaEscolar.model.Utilizador;
import com.iefp.SistemaInternoBibliotecaEscolar.repository.UtilizadorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtilizadorService {
    private final UtilizadorRepository utilizadorRepository;

    public UtilizadorService(UtilizadorRepository utilizadorRepository) {
        this.utilizadorRepository = utilizadorRepository;
    }

    public void guardar(Utilizador utilizador) {
        utilizadorRepository.save(utilizador);
    }

    public List<Utilizador> listarTodosUtilizadores() {
        return utilizadorRepository.findAll();
    }

    public Utilizador autenticar(String email, String senha) {
        return utilizadorRepository.findByEmailAndSenha(email, senha);
    }
}
