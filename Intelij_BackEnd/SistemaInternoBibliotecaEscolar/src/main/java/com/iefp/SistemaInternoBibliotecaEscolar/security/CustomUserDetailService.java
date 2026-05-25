package com.iefp.SistemaInternoBibliotecaEscolar.security;


import com.iefp.SistemaInternoBibliotecaEscolar.model.Utilizador;
import com.iefp.SistemaInternoBibliotecaEscolar.repository.UtilizadorRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UtilizadorRepository utilizadorRepository;

    public CustomUserDetailService(UtilizadorRepository utilizadorRepository) {
        this.utilizadorRepository = utilizadorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Utilizador utilizador = utilizadorRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilizador não encontrado com o email: " + email));

        String cargoComum = "ROLE_" + utilizador.getPerfil().toUpperCase();

        return User.builder()
                .username(utilizador.getEmail())
                .password(utilizador.getSenha())
                .roles(utilizador.getPerfil().toUpperCase())
                .build();
    }
}
