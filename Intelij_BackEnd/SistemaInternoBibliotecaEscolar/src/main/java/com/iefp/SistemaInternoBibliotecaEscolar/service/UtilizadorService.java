package com.iefp.SistemaInternoBibliotecaEscolar.service;

import com.iefp.SistemaInternoBibliotecaEscolar.model.Utilizador;
import com.iefp.SistemaInternoBibliotecaEscolar.repository.AlunoRepository;
import com.iefp.SistemaInternoBibliotecaEscolar.repository.BibliotecarioRepository;
import com.iefp.SistemaInternoBibliotecaEscolar.repository.UtilizadorRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtilizadorService {
    private final UtilizadorRepository utilizadorRepository;
    private final AlunoRepository alunoRepository;
    private final BibliotecarioRepository bibliotecarioRepository;

    public UtilizadorService(UtilizadorRepository utilizadorRepository,
                             AlunoRepository alunoRepository,
                             BibliotecarioRepository bibliotecarioRepository) {
        this.utilizadorRepository = utilizadorRepository;
        this.alunoRepository = alunoRepository;
        this.bibliotecarioRepository = bibliotecarioRepository;
    }

    //Mudar a senha para nome e email
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

    public Utilizador autenticar(String email, String nome) {
        Utilizador utilizador = utilizadorRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Autenticação falhou: Email não encontrado."));

        if ("Aluno".equalsIgnoreCase(utilizador.getPerfil())) {
            alunoRepository.findAll().stream().filter(a -> a.getUtilizador().getIdUtilizador().equals(utilizador.getIdUtilizador()))
                    .findFirst().filter(a -> a.getNome().equalsIgnoreCase(nome)).orElseThrow(() -> new RuntimeException("Autenticação falhou: Nome não corresponde ao Aluno registado."));
        } else if ("Bibliotecario".equalsIgnoreCase(utilizador.getPerfil())) {
            bibliotecarioRepository.findAll().stream().filter(b -> b.getUtilizador().getIdUtilizador().equals(utilizador.getIdUtilizador()))
                    .findFirst().filter(b -> b.getNome().equalsIgnoreCase(nome)).orElseThrow(() -> new RuntimeException("Autenticação falhou: Nome não corresponde ao Bibliotecário registado."));
        } else {
            throw new RuntimeException("Autenticação falhou: Perfil de utilizador desconhecido.");
        }

        return utilizador;
    }
}
