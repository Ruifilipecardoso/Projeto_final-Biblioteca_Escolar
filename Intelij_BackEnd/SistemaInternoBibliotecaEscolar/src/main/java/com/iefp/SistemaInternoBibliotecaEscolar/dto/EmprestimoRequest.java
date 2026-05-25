package com.iefp.SistemaInternoBibliotecaEscolar.dto;
/*DTO: Para que a comunicação entre o VS Code e o IntelliJ aconteça de forma limpa,
 precisamos de usar um DTO (Data Transfer Object).
 Como o ecrã não envia os objetos inteiros com todas as ligações da base de dados,
 ele envia apenas uma estrutura de texto leve (JSON) contendo os códigos e as datas.*/


import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class EmprestimoRequest {
    private Integer idAluno;
    private Integer idBibliotecario;
    private List<Integer> idLivros;
    private LocalDate dataInicio;
    private LocalTime horaInicio;
    private LocalDate dataFimSugerido;
    private LocalTime horaFimSugerido;
}
