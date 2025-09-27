// Opção sublinhada em amarelo e acinzentada.

const paginaAtual = window.location.pathname.split("/").pop();
const opcaoMenu = document.querySelectorAll(".menu a");

opcaoMenu.forEach(link => {
  const opcaoPagina = link.getAttribute("href");

  if (opcaoPagina === paginaAtual || (paginaAtual === "" && opcaoPagina === "index.html")) {
    link.classList.add("ativo");
}
});

// Quando um detail é aberto, o outro fecha.

const details = document.querySelectorAll('details');

details.forEach(det => {
  const summary = det.querySelector('summary');
  summary.addEventListener('click', (e) => {
    details.forEach(detail => {
      if (detail !== det) detail.open = false;
    });
  });
});
