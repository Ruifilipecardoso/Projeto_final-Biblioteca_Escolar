/* =========================================================================
   INTERATIVIDADE DO FRONTEND (Validações, Cliques e UI)
   ========================================================================= */

document.addEventListener("DOMContentLoaded", async function() {

    const utilizadorLogado = JSON.parse(localStorage.getItem("utilizador"));

    if (!utilizadorLogado) {
        window.location.href = 'login.html';
        return;
    };

    let aluno = null;
    let bibliotecario = null;

    
    const divTitulo = document.getElementById("boas_vindas");
    
    const divImagemPerfil = document.getElementById("imagem_perfil");

    let nomeImagem = "Conta_sem_perfile.jpg"
    
    const divEtiquetas = document.getElementById("coluna_etiquetas");
    
    const divInfo = document.getElementById("coluna_info");

    let htmlEtiquetas = "";
    let htmlInfo = "";

    htmlEtiquetas += `<p>Email:</p>`;

    htmlInfo += `<p>${utilizadorLogado.email}</p>`;

    const perfilEnum = utilizadorLogado.perfil.toUpperCase();

    if (perfilEnum === "ALUNO") {
        try {

            const resposta = await fetch("http://localhost:8080/api/alunos")

            if (resposta.ok) {
                const alunos = await resposta.json();

                aluno = alunos.find(a => a.utilizador.idUtilizador === utilizadorLogado.idUtilizador);

                if (aluno) {
                    divTitulo.textContent = `Bem vindo, ${aluno.nome}!`;

                    htmlEtiquetas += `
                        <p>Nome:</p>
                        <p>Nº Escolar:</p>
                        <p>Contacto:</p>
                        <p>NIF:</p>
                        <p>Estado:</p>
                    `;

                    htmlInfo += `
                        <p>${aluno.nome || 'Não definido'}</p>
                        <p>${aluno.numeroEscolar || 'Não definido'}</p>
                        <p>${aluno.contacto || 'Não definido'}</p>
                        <p>${aluno.nif || 'Não definido'}</p>
                        <p><span id="status">${aluno.status || 'Ativo'}</span></p>
                    `;

                    nomeImagem = aluno.utilizador.imagemPerfil;

                } else {
                    divTitulo.textContent = `Bem-vindo, utilizador (Perfil Aluno não localizado)!`;

                    nomeImagem = "Conta_sem_perfile.jpg";
                }
            }

        } catch (erro) {
            console.error("Erro ao carregar dados do aluno:", erro);
            divTitulo.textContent = `Bem-vindo! (Erro ao carregar perfil do servidor)`;
        }
        
    } else if (perfilEnum === "BIBLIOTECARIO") {
        try {

            const resposta = await fetch("http://localhost:8080/api/bibliotecarios")

            if (resposta.ok) {
                const bibliotecarios = await resposta.json();

                bibliotecario = bibliotecarios.find(b => b.utilizador.idUtilizador === utilizadorLogado.idUtilizador);

                if (bibliotecario) {

                    divTitulo.textContent = `Bem vindo, ${bibliotecario.nome}!`;

                    htmlEtiquetas += `
                        <p>Nome:</p>
                        <p>Nº Funcionário:</p>
                    `;

                    htmlInfo += `
                        <p>${bibliotecario.nome || 'Não definido'}</p>
                        <p>${bibliotecario.numeroEscolar || 'N/A'}</p>
                    `;

                    nomeImagem = bibliotecario.utilizador.imagemPerfil;

                } else {
                    divTitulo.textContent = `Bem-vindo, utilizador (Perfil Bibliotecário não localizado)!`;

                    nomeImagem = "Conta_sem_perfile.jpg";
                }
            }

        } catch (erro) {
            console.error("Erro ao carregar dados do Bibliotecário:", erro);
            divTitulo.textContent = `Bem-vindo! (Erro ao carregar perfil do servidor)`;
        }
    } else if (perfilEnum === "ADMIN") {

        if( utilizadorLogado && utilizadorLogado.imagemPerfil) {
            divTitulo.textContent = `Bem vindo, Admin!`;

            nomeImagem = utilizadorLogado.imagemPerfil;

        } else {
            divTitulo.textContent = `Bem-vindo, utilizador (Perfil Admin não localizado)!`;

            nomeImagem = "Conta_sem_perfile.jpg";
        }

        
    }
        
    divEtiquetas.innerHTML = htmlEtiquetas;
    divInfo.innerHTML = htmlInfo;

    divImagemPerfil.style.backgroundImage = `url('../imagens/perfis/${nomeImagem}')`;

    const status = document.getElementById("status");

    if (status) {
        const textoEstado = status.textContent.trim().toUpperCase();

        if (textoEstado === "BOM") {
            status.classList.add("estado-bom");

        } else if (textoEstado === "REGULAR") {
            status.classList.add("estado-regular");

        } else if (textoEstado === "NEGATIVO") {
            status.classList.add("estado-negativo");
        }
    }

   /* =========================================================================
   INJEÇÃO DE CONTEÚDO NA SECÇÃO DE EMPRÉSTIMOS
   ========================================================================= */

const seccaoEmprestimos = document.getElementById("emprestimos");

if (seccaoEmprestimos) {

    if (perfilEnum === "ALUNO" && aluno) {
        try {

            const resposta = await fetch("http://localhost:8080/api/emprestimos/todos");

            if (resposta.ok) {
                const todosEmprestimos = await resposta.json();

                const meusEmprestimos = todosEmprestimos.filter(e => e.aluno && e.aluno.idAluno === aluno.idAluno);

                const solicitados = meusEmprestimos.filter(e => e.estadoEmprestimo === "Solicitado");

                const agendados = meusEmprestimos.filter(e => e.estadoEmprestimo === "Agendado")

                const ativos = meusEmprestimos.filter(e => e.estadoEmprestimo === "Ativo");

                const finalizados = meusEmprestimos.filter(e => e.estadoEmprestimo === "Devolvido");

                //Html
                const gerarTabelaAluno = (lista) => {
                    if (lista.length === 0) return "<p class = 'sem-dados'>Sem registos nesta categoria.</p>";

                    let tabela = `
                        <table>
                            <thead>
                                <tr>
                                    <th>Livro(s)</th>
                                    <th>Data Pedido</th>
                                    <th>Prazo Limite</th>
                                </tr>
                            </thead>
                            <tbody>
                    `;

                    lista.forEach(e => {
                        const titulosLivros = e.linhaLivros.length > 0 ? e.linhaLivros.map(linha => linha.livro ? linha.livro.titulo : 'Sem título').join(", ") : 'Nenhum livro associado';

                        const prazoLimite = e.devolucaoIdeal ? e.devolucaoIdeal.data : 'Aguardar Aprovação';

                        tabela += `
                            <tr>
                                <td><strong>${titulosLivros}</strong></td>
                                <td>${e.data || '---'}</td>
                                <td>${prazoLimite}</td>
                            </tr>
                        `;
                    });

                    return tabela + "</tbody></table>";
                };

                seccaoEmprestimos.innerHTML = `
                    <h2 class='titulo-seccao'>O Meu Histórico de Empréstimos</h2>
                    <div class='bloco-conteudo'>
                        <h3>Solicitados (Aguardam Aprovação)</h3>
                        ${gerarTabelaAluno(solicitados)}
                    </div>
                    <div class='bloco-conteudo'>
                        <h3>Agendados (Prontos para Levantamento)</h3>
                        ${gerarTabelaAluno(agendados)}
                    </div>
                    <div class='bloco-conteudo'>
                        <h3>Empréstimos Ativos (Livros Comigo)</h3>
                        ${gerarTabelaAluno(ativos)}
                    </div>
                    <div class='bloco-conteudo'>
                        <h3>Finalizados (Devoluções Concluídas)</h3>
                        ${gerarTabelaAluno(finalizados)}
                    </div>
                `;
            }
        } catch (erro) {
            console.error("Erro ao carregar empréstimos:", erro);
            seccaoEmprestimos.innerHTML = "<p class='erro-msg'>Erro ao ligar ao servidor para carregar o histórico.</p>";
        }
    }
}

});
