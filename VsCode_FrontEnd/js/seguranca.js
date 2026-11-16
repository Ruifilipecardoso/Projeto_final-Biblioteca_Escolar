/*=======================================================================================
  Proteção de rotas (Verifica a sessão via Cookie HttpOnly no Backend)
  =====================================================================================*/

window.sessaoPromise = (async function verificarAutenticacao() {
    const API_URL = "http://localhost:8080";

    try {
        const resposta = await fetch(`${API_URL}/api/utilizadores/me`, {
            method: "GET",
            credentials: "include"
        });

        if (!resposta.ok) {
            window.location.href = "login.html";
            return null;
        }

        const utilizador = await resposta.json();
        window.utilizadorAtual = utilizador;
        return utilizador;

    } catch (error) {
        console.error("Erro ao validar sessão: ", erro);
        window.location.href = "login.html";
        return null;
    }
})();
