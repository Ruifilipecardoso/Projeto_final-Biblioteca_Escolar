package com.iefp.SistemaInternoBibliotecaEscolar.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "linha_livros")
@IdClass(LinhaLivrosChaveComposta.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinhaLivros {

    @Column(name = "qualidade")
    private String qualidade;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_livro")
    private Livro livro;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_emprestimo")
    private Emprestimo emprestimo;

}
