/* =========================================================================
   INTERATIVIDADE DO FRONTEND (Validações, Cliques e UI)
   ========================================================================= */

const formulario = document.getElementById("formulario");
const campoSenha = document.getElementById("senha");
const campoAvisoBotao = document.getElementById("aviso_e_botao");
const textoErro = document.getElementById("mensagem_erro");

formulario.addEventListener("submit", function(event) {
    event.preventDefault();

    if (campoSenha.value.length < 8 || campoSenha.value.length > 30) {
        textoErro.textContent = "*Dados de acesso escritos incorretamente! Tente novamente.";
        campoAvisoBotao.classList.add("com-erro");
        return;
    }

    campoAvisoBotao.classList.remove("com-erro");
    textoErro.textContent = "";

    const email = document.getElementById("email").value.trim();
    const senha = campoSenha.value;

    efetuarLogin(email, senha);

});



/* =========================================================================
   LIGAÇÃO À API DO BACKEND (Pedidos HTTP / Fetch com Cookie HttpOnly)
   ========================================================================= */

async function efetuarLogin(email, senha) {
    const API_URL = "http://localhost:8080";

    try {
        const resposta = await fetch(`${API_URL}/api/utilizadores/login`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            credentials: "include",
            body: JSON.stringify({email, senha})
        });

        const textoResposta = await resposta.text();
        const dados = textoResposta ? JSON.parse(textoResposta) : {};

        if (!resposta.ok) {
            textoErro.textContent = dados.erro || `Erro ${resposta.status}: Acesso negado ou dados incorretos.`;

            campoAvisoBotao.classList.add("com-erro");

            return;
        }

        alert("Login efetuado com sucesso!");

        window.location.href = "home.html";

    } catch (error) {
        console.error("Erro ao conectar ao servidor: ", error);
        textoErro.textContent = "*Erro de ligação ao servidor!";
        campoAvisoBotao.classList.add("com-erro");
    }
}

