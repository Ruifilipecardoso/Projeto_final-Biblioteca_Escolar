package com.iefp.SistemaInternoBibliotecaEscolar.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmprestimoResponse {
    private Integer idEmprestimo;
    private Integer idAluno;
    private LocalDate data;
    private String estadoEmprestimo;
    private List<LinhaLivroMinimo> linhaLivros;
    private DevolucaoIdealMinimo devolucaoIdeal;

    private String nomeAluno;
    private String numeroEscolar;
    private String estadoAluno;
}
