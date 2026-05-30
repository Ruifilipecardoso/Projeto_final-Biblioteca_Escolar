/* =========================================================================
   INTERATIVIDADE DO FRONTEND (Validações, Cliques e UI)
   ========================================================================= */

const formulario = document.getElementById("formulario");
const campoSenha = document.getElementById("senha");
const campoAvisoBotao = document.getElementById("aviso_e_botao");
const textoErro = document.getElementById("mensagem_erro");

formulario.addEventListener("submit", function(event) {
    event.preventDefault();

    const blocoSenha = campoSenha.closest(".campo_form");

    if (campoSenha.value.length < 8 || campoSenha.value.length > 15) {
        textoErro.textContent = "*Dados de acesso escritos incorretamente! Tente novamente.";
        campoAvisoBotao.classList.add("com-erro");
        return;
    }

    blocoSenha.classList.remove("com-erro");
    document.getElementById("mensagem_erro").textContent = "";

    efetuarLogin(document.getElementById("nome").value, document.getElementById("email").value, campoSenha.value);

})



/* =========================================================================
   LIGAÇÃO À API DO BACKEND (Pedidos HTTP / Fetch)
   ========================================================================= */

async function efetuarLogin(nome, email, senha) {
    const API_URL = "http://localhost:8080";
    const blocoSenha = document.getElementById("senha").closest(".campo_form");

    try {
        const parametros = new URLSearchParams({nome, email, senha});

        const resposta = await fetch(`${API_URL}/api/utilizadores/login?${parametros}`, {method: 'POST'});

        if (!resposta.ok) {
            textoErro.textContent = "*Dados de acesso incorretos! Tente novamente.";

            campoAvisoBotao.classList.add("com-erro");

            return;
        }

        const utilizadorLogado = await resposta.json();

        localStorage.setItem("utilizador", JSON.stringify(utilizadorLogado));

        alert("Login efetuado com sucesso!");

        window.location.href = "home.html";

    } catch (error) {
        console.error("Erro ao conectar ao servidor: ", error);
        textoErro.textContent = "*Erro de ligação ao servidor!";
        campoAvisoBotao.classList.add("com-erro");
    }
}

