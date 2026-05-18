package com.iefp.SistemaInternoBibliotecaEscolar.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "devolucao_ideal")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DevolucaoIdeal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_devolucao_i")
    private Integer idDevolucaoIdeal;

    @Column(name = "data")
    private LocalDate data;

    @Column(name = "hora")
    private LocalTime hora;

    @Column(name = "estado_devolucao")
    private String estadoDevolucao;

    @OneToOne
    @JoinColumn(name = "id_emprestimo")
    private Emprestimo emprestimo;
}
