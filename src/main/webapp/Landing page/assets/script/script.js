// Opção sublinhada em amarelo e acinzentada.

const paginaAtual = window.location.pathname.split("/").pop();
const opcaoMenu = document.querySelectorAll(".menu a");

opcaoMenu.forEach(link => {
  const opcaoPagina = link.getAttribute("href");

  if (opcaoPagina === paginaAtual || (paginaAtual === "" && opcaoPagina === "index.html")) {
    link.classList.add("ativo");
}
});
