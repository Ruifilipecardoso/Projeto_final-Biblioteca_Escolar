package com.iefp.SistemaInternoBibliotecaEscolar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SistemaInternoBibliotecaEscolarApplication {

	public static void main(String[] args) {
		SpringApplication.run(SistemaInternoBibliotecaEscolarApplication.class, args);
	}

}


//✅Caso os livros não estejam disponíveis, aparecere a data ideal mais curta nas informações do livro
//✅No UtilizadorService mudar a senha para nome e email
//Aluno(1) -> Emprestimos(N) -> Livros(N)
// 1 Aluno pode ter até 12 livros em no máximo 3 empréstimos.
//1 Aluno com o empréstimo mais específico tem prioridade no livro.
//Colucar a recursiva como easter egg.