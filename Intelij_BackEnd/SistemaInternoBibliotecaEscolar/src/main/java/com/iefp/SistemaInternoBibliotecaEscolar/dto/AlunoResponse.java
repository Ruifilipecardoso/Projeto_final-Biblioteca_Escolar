package com.iefp.SistemaInternoBibliotecaEscolar.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlunoResponse {
    private Integer idAluno;
    private String nome;
    private String numeroEscolar;
    private String contacto;
    private String nif;
    private String status;
    private UtilizadorMinimo utilizador;
}
