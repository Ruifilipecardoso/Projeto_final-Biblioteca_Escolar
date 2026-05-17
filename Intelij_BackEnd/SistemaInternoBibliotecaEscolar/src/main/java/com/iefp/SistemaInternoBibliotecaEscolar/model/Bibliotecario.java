package com.iefp.SistemaInternoBibliotecaEscolar.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bibliotecario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bibliotecario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bibliotecario")
    private Long idBibliotecario;

    @Column(name = "nome")
    private String nome;

    @Column(name = "password")
    private String password;

    @Column(name = "numero_escolar")
    private String numeroEscolar;
}
