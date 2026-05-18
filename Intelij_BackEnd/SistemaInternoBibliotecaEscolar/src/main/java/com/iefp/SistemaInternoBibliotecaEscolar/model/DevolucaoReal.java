package com.iefp.SistemaInternoBibliotecaEscolar.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "devolucao_real")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DevolucaoReal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_devolucao_r")
    private Integer idDevolucaoReal;

    @Column(name = "data")
    private LocalDate data;

    @Column(name = "hora")
    private LocalTime hora;

    @Column(name = "qualidade")
    private String qualidade;

    @OneToOne
    @JoinColumn(name = "id_emprestimo")
    private Emprestimo emprestimo;
}
