document.addEventListener('DOMContentLoaded', () => {

    // --- LÓGICA PARA BOTÕES DE FILTRAGEM ---
    const botaoLimparFiltro = document.getElementById("botaoLimparFiltro");

    botaoLimparFiltro.addEventListener("click", function() {
        const caminho = JSON.parse(botaoLimparFiltro.dataset.caminho);
        const caminhoBase = caminho.base;
        const tabelaAtual = caminho.tabela;
        window.location.href = `${caminhoBase}/${tabelaAtual}`;
    });

    // --- LÓGICA PARA ABRIR/FECHAR MODAIS COM CLIQUE ---
    const botoesModal = document.querySelectorAll('.acaoModal');
    botoesModal.forEach(botaoModal => {
        botaoModal.addEventListener('click', () => {
            const modal = document.getElementById(botaoModal.dataset.modal);
            const acao = botaoModal.dataset.acao;

            if (modal) {
                if (acao === 'abrir') {
                    const pk = botaoModal.dataset.pk;
                    if (modal.id === 'update') { document.getElementById('idUpdate').value = pk }
                    else if (modal.id === 'delete') { document.getElementById('idDelete').value = pk }

                    if (botaoModal.dataset.caminho) {
                        const caminho = JSON.parse(botaoModal.dataset.caminho);
                        const caminhoBase = caminho.base;
                        const tabelaAtual = caminho.tabela;
                        fetch(`${caminhoBase}/${tabelaAtual}?pk=${pk}`);
                    }
                    modal.showModal();

                } else if (acao === 'fechar') {
                    modal.close();
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

    // --- LÓGICA DE VALIDAÇÃO DE FORMULÁRIOS (ex: página de Usuários) ---
    // Anexa o validador ao formulário do CREATE (só se ele existir na página)
    const formCreate = document.querySelector('#create form');
    if (formCreate) {
        formCreate.addEventListener('submit', (event) => {
            // Verifica se os campos de senha/cpf existem antes de validar
            if (document.getElementById('senhaCreate') && document.getElementById('cpfCreate')) {
                validarFormulario(event, 'senhaCreate', 'cpfCreate');
            }
        });
    }

    // Anexa o validador ao formulário do UPDATE (só se ele existir na página)
    const formUpdate = document.querySelector('#update form');
    if (formUpdate) {
        formUpdate.addEventListener('submit', (event) => {
            // Verifica se os campos de senha/cpf existem antes de validar
            if (document.getElementById('senhaUpdate') && document.getElementById('cpfUpdate')) {
                validarFormulario(event, 'senhaUpdate', 'cpfUpdate');
            }
        });
    }

}); 


/*
 * ===================================================================
 * FUNÇÕES AUXILIARES DE VALIDAÇÃO
 * ===================================================================
 * Estas funções podem ficar fora, pois são apenas "definições"
 * que serão chamadas pelos 'event listeners' acima.
 */

// Função que valida os campos complexos senha e CPF
function validarFormulario(event, idSenha, idCpf) {
    try {
        const cpf = document.getElementById(idCpf).value;
        const senha = document.getElementById(idSenha).value;

        if (cpf && !isValidCpf(cpf)) { // Exceção: verifica se o CPF é válido
            throw new Error("O formato do CPF é inválido. Use 123.123.123-12 ou 12312312312.");
        }

        if (senha) { // Exceção: verifica se a senha é válida
            isValidSenha(senha); // Esta função já joga o erro específico
        }

    } catch (e) { // Se receber um erro de exceção
        event.preventDefault(); // Impede o envio
        alert(e.message);       // Mostra o erro personalizado
    }
}

/*
 * Verifica se o CPF é válido
 * Exemplos de CPF aceitável:
 * "123.123.123-12", "12312312312"
 */
function isValidCpf(cpf) {
    // Regex do seu Model: ^\d{3}\.?\d{3}\.?\d{3}-?\d{2}$
    const regex = /^\d{3}\.?\d{3}\.?\d{3}-?\d{2}$/;
    return regex.test(cpf.trim());
}

/*
 * Verifica se a senha é válida
 * Regras de senha:
 * -Mínimo 8 caracteres
 * -Mínimo 1 letra maiúscula
 * -Mínimo 1 letra minúscula
 * -Mínimo 1 caractere especial
 * -Mínimo 1 número
 */
function isValidSenha(senha) {
    if (senha.length < 8) { // Exceção: verifica se a senha tem no mínimo 8 caracteres
        throw new Error("A senha deve ter no mínimo 8 caracteres");
    }
    if (!/[a-z]/.test(senha)) { // Exceção: verifica se a senha tem no mínimo 1 letra minúscula
        throw new Error("A senha deve ter no mínimo 1 letra minúscula");
    }
    if (!/[A-Z]/.test(senha)) { // Exceção: verifica se a senha tem no mínimo 1 letra maiúscula
        throw new Error("A senha deve ter no mínimo 1 letra maiúscula");
    }
    if (!/\d/.test(senha)) { // Exceção: verifica se a senha tem no mínimo 1 dígito
        throw new Error("A senha deve ter no mínimo 1 dígito");
    }
    if (!/[^A-Za-z0-9]/.test(senha)) { // Exceção: verifica se a senha tem no mínimo 1 caractere especial
        throw new Error("A senha deve ter no mínimo 1 caractere especial");
    }
    return true;
}

// --- FUNÇÕES DE PRÉ-PREENCHIMENTO DOS MODAIS ---

function preencherCamposModal(tipo, dados) {
    if (tipo === 'update') {
        if (document.getElementById('idUpdate')) document.getElementById('idUpdate').value = dados.id || '';
        if (document.getElementById('nomeUpdate')) document.getElementById('nomeUpdate').value = dados.nome || '';
        if (document.getElementById('emailUpdate')) document.getElementById('emailUpdate').value = dados.email || '';
        if (document.getElementById('senhaUpdate')) document.getElementById('senhaUpdate').value = dados.senha || '';
        if (document.getElementById('cpfUpdate')) document.getElementById('cpfUpdate').value = dados.cpf || '';
        // adiciona outros campos que tiver no form update
    }

    else if (tipo === 'delete') {
        if (document.getElementById('idDelete')) document.getElementById('idDelete').value = dados.id || '';
        if (document.getElementById('nomeDelete')) document.getElementById('nomeDelete').value = dados.nome || '';
        if (document.getElementById('emailDelete')) document.getElementById('emailDelete').value = dados.email || '';
        // se quiser mostrar no modal de exclusão algo tipo “Tem certeza que quer excluir X?”
        const nomeAlvo = document.getElementById('nomeDeleteTexto');
        if (nomeAlvo) nomeAlvo.textContent = dados.nome ? `Tem certeza que deseja excluir ${dados.nome}?` : '';
    }
}