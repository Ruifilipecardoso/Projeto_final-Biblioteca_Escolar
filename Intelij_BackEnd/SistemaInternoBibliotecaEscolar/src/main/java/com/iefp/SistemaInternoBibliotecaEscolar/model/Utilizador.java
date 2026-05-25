package com.iefp.SistemaInternoBibliotecaEscolar.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "utilizador")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Utilizador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_utilizador")
    private Integer idUtilizador;

    @Column(name = "email")
    private String email;

    @Column(name = "senha")
    private String senha;

    @Column(name = "perfil")
    private String perfil;

    @Column(name = "imagem_perfil")
    private String imagemPerfil;

}
