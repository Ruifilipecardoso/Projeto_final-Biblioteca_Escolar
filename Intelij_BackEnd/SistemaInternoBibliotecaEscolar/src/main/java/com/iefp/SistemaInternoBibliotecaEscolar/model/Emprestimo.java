package com.iefp.SistemaInternoBibliotecaEscolar.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "emprestimo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_emprestimo")
    private Integer idEmprestimo;

    @Column(name = "data")
    private LocalDate data;

    @Column(name = "hora")
    private LocalTime hora;

    @Column(name = "estado_emprestimo")
    private String estadoEmprestimo;

    @ManyToOne
    @JoinColumn(name = "id_bibliotecario")
    private Bibliotecario bibliotecario;

    @ManyToOne
    @JoinColumn(name = "id_aluno")
    private Aluno aluno;

    @OneToMany(mappedBy = "emprestimo")
    private List<LinhaLivros> linhaLivros;

    @OneToOne(mappedBy = "emprestimo")
    private DevolucaoIdeal devolucaoIdeal;

    @OneToOne(mappedBy = "emprestimo")
    private DevolucaoReal devolucaoReal;
}





















