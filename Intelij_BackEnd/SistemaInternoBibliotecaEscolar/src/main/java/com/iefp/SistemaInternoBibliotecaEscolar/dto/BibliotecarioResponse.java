package com.iefp.SistemaInternoBibliotecaEscolar.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BibliotecarioResponse {
    private Integer idBibliotecario;
    private String nome;
    private String numeroEscolar;
    private UtilizadorMinimo utilizador;
}
