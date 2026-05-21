package com.iefp.SistemaInternoBibliotecaEscolar.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "aluno")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aluno")
    private Integer idAluno;

    @Column(name = "nome")
    private String nome;

    @Column(name = "contacto")
    private String contacto;

    @Column(name = "nif")
    private String nif;

    @Column(name = "status")
    private String status;

    @Column(name = "numero_escolar")
    private String numeroEscolar;

    @OneToOne
    @JoinColumn(name = "id_utilizador")
    private Utilizador utilizador;

    @OneToMany(mappedBy = "aluno")
    private List<Emprestimo> emprestimos;
}
