const utilizador = JSON.parse(localStorage.getItem("utilizador"));

if (!utilizador) {
    alert("Acesso negado! Inicie a sessão primeiro.");
    window.location.href = "login.html";
}

