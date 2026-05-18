package com.iefp.SistemaInternoBibliotecaEscolar.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinhaLivrosChaveComposta implements Serializable {
    private Integer livro;
    private Integer emprestimo;
}
