package com.iefp.SistemaInternoBibliotecaEscolar.service;

import com.iefp.SistemaInternoBibliotecaEscolar.model.Bibliotecario;
import com.iefp.SistemaInternoBibliotecaEscolar.model.Utilizador;
import com.iefp.SistemaInternoBibliotecaEscolar.repository.BibliotecarioRepository;
import com.iefp.SistemaInternoBibliotecaEscolar.repository.UtilizadorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BibliotecarioService {
    private final BibliotecarioRepository bibliotecarioRepository;
    private final UtilizadorRepository utilizadorRepository;

    public BibliotecarioService(BibliotecarioRepository bibliotecarioRepository, UtilizadorRepository utilizadorRepository) {
        this.bibliotecarioRepository = bibliotecarioRepository;
        this.utilizadorRepository = utilizadorRepository;
    }

    public Bibliotecario guardar (Bibliotecario bibliotecario) {
        Utilizador utilizadorSalvo = utilizadorRepository.save(bibliotecario.getUtilizador());

        bibliotecario.setUtilizador(utilizadorSalvo);

        return bibliotecarioRepository.save(bibliotecario);
    }

    public List<Bibliotecario> listarTodosBibliotecarios() {
        return bibliotecarioRepository.findAll();
    }

    public Optional<Bibliotecario> buscarPorId(Integer id) {
        return bibliotecarioRepository.findById(id);
    }
}
