package com.iefp.SistemaInternoBibliotecaEscolar.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "livro")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_livro")
    private Integer idLivro;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "categoria")
    private String categoria;

    @Column(name = "autor")
    private String autor;

    @Column(name = "disponibilidade")
    private String disponibilidade;

    @Column(name = "stock_atual")
    private Integer stockAtual;

    @Column(name = "stock_total")
    private Integer stockTotal;

    @Column(name = "isbn")
    private String isbn;

    @OneToMany(mappedBy = "livro")
    private List<LinhaLivros> linhaLivros;
}
