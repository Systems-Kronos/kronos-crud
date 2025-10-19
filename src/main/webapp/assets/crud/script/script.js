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
});

const modalUpdate = document.getElementById('update');
const modalDelete = document.getElementById('delete');
if (modalUpdate.dataset.abrir === 'true') {
    modalUpdate.showModal();
}
else if (modalDelete.dataset.abrir === 'true') {
    modalDelete.showModal();
}