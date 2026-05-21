package com.iefp.SistemaInternoBibliotecaEscolar.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "bibliotecario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bibliotecario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bibliotecario")
    private Integer idBibliotecario;

    @Column(name = "nome")
    private String nome;

    @Column(name = "numero_escolar")
    private String numeroEscolar;

    @OneToOne
    @JoinColumn(name = "id_utilizador")
    private Utilizador utilizador;

    @OneToMany(mappedBy = "bibliotecario")
    private List<Emprestimo> emprestimos;
}
