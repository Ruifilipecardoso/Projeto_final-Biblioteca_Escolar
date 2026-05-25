package com.iefp.SistemaInternoBibliotecaEscolar.dto;


import lombok.Data;

@Data
public class DevolucaoRequest {
    private Integer idLivro;
    private String qualidadeEntrega;
    private String statusSugeridoBibliotecário;
}
