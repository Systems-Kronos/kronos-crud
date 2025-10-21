document.addEventListener('DOMContentLoaded', function() {
    const botoesModal = document.querySelectorAll('.acaoModal');

    botoesModal.forEach(function(botao) {
        botao.addEventListener('click', function(evento) {
            
            const acao = evento.currentTarget.getAttribute('data-acao');
            const idModal = evento.currentTarget.getAttribute('data-modal');
            
            const modalAlvo = document.getElementById(idModal);
            
            if (modalAlvo && typeof modalAlvo.showModal === 'function') {
                
                if (acao === 'abrir') {
                    modalAlvo.showModal();
                    
                } else if (acao === 'fechar') {
                    modalAlvo.close();
                }
            }
        });
    });
    var botaoFiltro = document.getElementById('botaoLimparFiltro');
    botaoFiltro.addEventListener("click", function (){
        window.location.href='${pageContext.request.contextPath}/usuarios-crud'
    })
});
