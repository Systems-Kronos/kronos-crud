document.addEventListener('DOMContentLoaded', () => {

    // --- LÓGICA PARA BOTÕES DE FILTRAGEM ---
    const botaoLimparFiltro = document.getElementById('botaoLimparFiltro');

    if (botaoLimparFiltro) {
        botaoLimparFiltro.addEventListener('click', function() {
            const caminho = JSON.parse(botaoLimparFiltro.dataset.caminho);
            const caminhoBase = caminho.base;
            const tabelaAtual = caminho.tabela;
            window.location.href = `${caminhoBase}/${tabelaAtual}`;
        });
    }

    // --- LÓGICA PARA ABRIR/FECHAR MODAIS COM CLIQUE ---
    const botoesModal = document.querySelectorAll('.acaoModal');
    botoesModal.forEach(botaoModal => {
        botaoModal.addEventListener('click', () => {
            botaoModal.disabled = true;
            const modal = document.getElementById(botaoModal.dataset.modal);
            const acao = botaoModal.dataset.acao;

            if (modal) {
                if (acao === 'abrir') {

                    if (modal.id === 'create') {
                        modal.showModal();
                        botaoModal.disabled = false;
                        return;
                    }

                    const pk = botaoModal.dataset.pk;

                    if (botaoModal.dataset.caminho) {
                        const caminho = JSON.parse(botaoModal.dataset.caminho);
                        const caminhoBase = caminho.base;
                        const tabelaAtual = caminho.tabela;
                        
                        fetch(`${caminhoBase}/${tabelaAtual}?pk=${pk}`)
                        .then(response => response.json())
                        .then(dados => {
                            
                            // Preenche os campos do modal correspondente
                            if (modal.id === 'update') {
                                preencherCamposModal('update', dados, tabelaAtual);
                            } else if (modal.id === 'delete') {
                                preencherCamposModal('delete', dados, tabelaAtual);
                            }
                            modal.showModal();
                            botaoModal.disabled = false;
                            })
                        .catch(err => console.error('Erro ao buscar dados:', err));
                        }
                    } else if (acao === 'fechar') {
                    modal.close();
                    botaoModal.disabled = false;
                }
                else {
                    botaoModal.disabled = false;
                }
            }
        });
    });

    // --- LÓGICA PARA ABRIR MODAIS (UPDATE/DELETE) AUTOMATICAMENTE ---
    // (Isto vem do seu JSP, agora está seguro dentro do listener)
    const modalUpdate = document.getElementById('update');
    const modalDelete = document.getElementById('delete');

    // Adicionamos 'modalUpdate &&' para evitar erros em páginas que não tenham esse modal
    if (modalUpdate && modalUpdate.dataset.abrir === 'true') {
        modalUpdate.showModal();
    }
    else if (modalDelete && modalDelete.dataset.abrir === 'true') {
        modalDelete.showModal();
    }

    // --- LÓIGCA PARA O BOTÃO DE CONFIRMAR SÓ PODER SER CLICADO 1 VEZ
    const formModalCreate = document.getElementById('formCreate');
    const botoesConfirmarModal = document.querySelectorAll('.confirmar');
    
    formModalCreate.addEventListener('submit', (event) => {
        if (formModalCreate.checkValidity()) {
            botoesConfirmarModal.forEach(botaoConfirmarModal => {
                botaoConfirmarModal.disabled = true;
            });
        } else {
            event.preventDefault();
        }
    });
});


// --- FUNÇÕES DE PRÉ-PREENCHIMENTO DOS MODAIS ---

function preencherCamposModal(tipo, dados, tabela) {
    const sufixo = tipo === 'update' ? 'Update' : 'Delete';
    let campos = [];

    switch (tabela) {
        case 'admin-crud': campos = ['id', 'nome', 'email', 'senha']; break;
        case 'empresas-crud': campos = ['id', 'nome', 'email', 'cep', 'cnpj', 'telefone', 'porte', 'horaAbertura', 'horaFechamento', 'regrasNegocios']; break;
        case 'planos-crud': campos = ['id', 'nome', 'custo', 'maxFuncionarios', 'descricao']; break;
        case 'habilidades-crud': campos = ['id', 'nome', 'tag', 'descricao']; break;
        case 'setores-crud': campos = ['id', 'nome', 'qtnFuncionarios', 'turnos', 'descricao', 'empresa']; break;
        case 'usuarios-crud': campos = ['id', 'nome', 'cpf', 'senha', 'genero', 'cargo', 'status', 'idSetor', 'idSupervisor']; break;
    }

    campos.forEach(campo => {
        const elemento = document.getElementById(`${campo}${sufixo}`);
        if (elemento) elemento.value = dados[campo] || '';
    });
}
