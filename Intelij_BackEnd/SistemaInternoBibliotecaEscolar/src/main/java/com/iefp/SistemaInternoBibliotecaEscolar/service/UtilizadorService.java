package com.iefp.SistemaInternoBibliotecaEscolar.service;

import com.iefp.SistemaInternoBibliotecaEscolar.model.Utilizador;
import com.iefp.SistemaInternoBibliotecaEscolar.repository.AlunoRepository;
import com.iefp.SistemaInternoBibliotecaEscolar.repository.BibliotecarioRepository;
import com.iefp.SistemaInternoBibliotecaEscolar.repository.UtilizadorRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Service
public class UtilizadorService {
    private final UtilizadorRepository utilizadorRepository;
    private final AlunoRepository alunoRepository;
    private final BibliotecarioRepository bibliotecarioRepository;

    private final PasswordEncoder passwordEncoder;

    public UtilizadorService(UtilizadorRepository utilizadorRepository,
                             AlunoRepository alunoRepository,
                             BibliotecarioRepository bibliotecarioRepository,
                             PasswordEncoder passwordEncoder) {
        this.utilizadorRepository = utilizadorRepository;
        this.alunoRepository = alunoRepository;
        this.bibliotecarioRepository = bibliotecarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //Mudar a senha para nome e email
    public void guardar(Utilizador utilizador) {
        try {

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            String senhaEncriptada = encoder.encode(utilizador.getSenha());
            utilizador.setSenha(senhaEncriptada);

            utilizadorRepository.save(utilizador);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Este endereço de email já está a ser utilizado por outro utilizador.");
        }

    }

    public List<Utilizador> listarTodosUtilizadores() {
        return utilizadorRepository.findAll();
    }

    public Utilizador autenticar(String email, String senha) {
        Utilizador utilizador = utilizadorRepository.findByEmail(email).orElseThrow(() ->
                new RuntimeException("Autenticação falhou: Email não encontrado."));


        //Compara a senha digitada (limpa) com a senha da BD (encriptada)
        if (!passwordEncoder.matches(senha, utilizador.getSenha())) {
            throw new RuntimeException("Autenticação falhou: Palavra-passe incorreta.");
        }

        String perfil = utilizador.getPerfil() != null ? utilizador.getPerfil().toUpperCase() : "";


        if (perfil.contains("ADMIN")) {
            return utilizador;

        } else if (perfil.contains("ALUNO")) {
            boolean existeAluno = alunoRepository.findAll().stream()
                    .anyMatch(a -> a.getUtilizador() != null && a.getUtilizador().getIdUtilizador().equals(utilizador.getIdUtilizador()));

            if (!existeAluno) {
                throw new RuntimeException("Autenticação falhou: Aluno não vinculado a este utilizador.");
            }
        } else if (perfil.contains("BIBLIOTECARIO")) {
            boolean existeBiblio = bibliotecarioRepository.findAll().stream()
                    .anyMatch(b -> b.getUtilizador() != null && b.getUtilizador().getIdUtilizador().equals(utilizador.getIdUtilizador()));

            if (!existeBiblio) {
                throw new RuntimeException("Autenticação falhou: Bibliotecário não vinculado a este utilizador.");
            }
        } else {
            throw new RuntimeException("Autenticação falhou: Perfil de utilizador desconhecido.");
        }

        return utilizador;
    }
}
