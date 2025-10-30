document.addEventListener('DOMContentLoaded', () => {

    // --- LÓGICA DE AUTO-ABERTURA DE MODAL EM CASO DE ERRO ---
    const modalIdParaAbrir = document.body.dataset.modalParaAbrir;
    if (modalIdParaAbrir) {
        const modal = document.getElementById(modalIdParaAbrir);
        if (modal && typeof modal.showModal === 'function') {
            modal.showModal();
        } else {
            console.error("Tentativa de abrir um modal que não existe: '" + modalIdParaAbrir + "'");
        }
    }

    // --- LÓGICA PARA BOTÕES DE FILTRAGEM ---
    const botaoLimparFiltro = document.getElementById('botaoLimparFiltro');

    if (botaoLimparFiltro) {
        botaoLimparFiltro.addEventListener('click', function () {
            const caminho = JSON.parse(botaoLimparFiltro.dataset.caminho);
            const caminhoBase = caminho.base;
            const tabelaAtual = caminho.tabela;
            window.location.href = `${caminhoBase}/${tabelaAtual}`;
        });
    }

    // --- LÓGICA PARA VALIDAÇÃO ESPECÍFICA DE SENHA ---
    // (Este bloco agora também lida com a lógica de "clique único" para este formulário)
    const formCreate = document.getElementById('formCreate');
    const senhaInput = document.getElementById('senhaCreate'); // Procura por 'senhaCreate' (Admin)
    const senhaErrorSpan = document.getElementById('senhaErro'); // Procura por 'senhaErro' (Admin)

    // (Adicione seletores para 'usuario.jsp' se os IDs forem diferentes e adapte a lógica)
    // const senhaInputUsuario = document.getElementById('senhaCreateUsuario');
    // const senhaErrorSpanUsuario = document.getElementById('senhaErrorUsuario');

    // Função que valida a senha (igual)
    function validarSenha(senha) {
        if (!senha) return "A senha não pode estar em branco.";
        if (senha.length < 8) return "A senha deve ter no mínimo 8 caracteres.";
        if (!/[a-z]/.test(senha)) return "A senha deve ter no mínimo 1 letra minúscula.";
        if (!/[A-Z]/.test(senha)) return "A senha deve ter no mínimo 1 letra maiúscula.";
        if (!/\d/.test(senha)) return "A senha deve ter no mínimo 1 dígito.";
        if (!/[^\sA-Za-z0-9]/.test(senha)) return "A senha deve ter no mínimo 1 caractere especial.";
        return ""; // Válida
    }

    // Valida a senha enquanto digita (feedback em tempo real) - Focado no Admin
    if (senhaInput && senhaErrorSpan) {
        senhaInput.addEventListener('input', () => {
            const mensagemErro = validarSenha(senhaInput.value);
            senhaErrorSpan.textContent = mensagemErro;
        });
        const parentDialogCreate = formCreate ? formCreate.closest('dialog') : null;
        if (parentDialogCreate) {
            parentDialogCreate.addEventListener('close', () => {
                senhaErrorSpan.textContent = '';
            });
        }
    }
    // (Adicione um 'if (senhaInputUsuario && senhaErrorSpanUsuario)' similar para a página de usuário)

    // Listener de submit ÚNICO E COMBINADO para 'formCreate'
    if (formCreate) {
        formCreate.addEventListener('submit', (event) => {
            const submitButton = formCreate.querySelector('button[type="submit"].confirmar');
            let mensagemErroSenha = "";

            // 1. Valida a senha (se o campo de senha existir)
            //    (Adapte este 'if' se 'formCreate' for usado em múltiplas páginas)
            if (senhaInput && senhaErrorSpan) {
                mensagemErroSenha = validarSenha(senhaInput.value);
                senhaErrorSpan.textContent = mensagemErroSenha;
            }

            // 2. Verifica se a senha OU outros campos (required, email, etc) falharam
            if (mensagemErroSenha || !formCreate.checkValidity()) {
                // Se falhou, impede o envio e garante que o botão esteja ATIVO
                event.preventDefault();
                if (submitButton) submitButton.disabled = false;
            } else {
                // 3. Se TUDO estiver válido, desabilita o botão (lógica do "clique único")
                if (submitButton) submitButton.disabled = true;
                // ... e permite que o formulário seja enviado
            }
        });
    }

    // --- BLOCO REMOVIDO (LINHAS 78-89) ---
    // O bloco que começava com 'if (formCreateAdmin)' foi removido
    // pois era quebrado (ReferenceError) e redundante.

    // --- LÓGICA PARA ABRIR/FECHAR MODAIS COM CLIQUE ---
    // (Este código agora será executado, pois o erro anterior foi removido)
    const botoesModal = document.querySelectorAll('.acaoModal');
    botoesModal.forEach(botaoModal => {
        botaoModal.addEventListener('click', () => {
            botaoModal.disabled = true;
            const modal = document.getElementById(botaoModal.dataset.modal);
            const acao = botaoModal.dataset.acao;

            if (modal) {
                if (acao === 'abrir') {

                    if (modal.id === 'create') {
                        // Limpa erro da senha se existir nesse modal ao abrir
                        const errorSpan = modal.querySelector('#senhaErro'); // Procura ID da senha admin
                        if (errorSpan) errorSpan.textContent = '';
                        // Adicionar limpeza para span de senha de usuário se/quando for implementado
                        const errorSpanUser = modal.querySelector('#senhaErrorUsuario');
                        if (errorSpanUser) errorSpanUser.textContent = '';

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
                            .then(response => {
                                if (!response.ok) {
                                    return response.json().catch(() => null).then(errData => {
                                        throw new Error(`Erro ${response.status}: ${response.statusText}. ${errData?.erro || 'Não foi possível buscar dados.'}`);
                                    });
                                }
                                return response.json();
                            })
                            .then(dados => {
                                if (dados.erro) { throw new Error('Erro retornado pela API: ' + dados.erro); }

                                if (modal.id === 'update') {
                                    preencherCamposModal('update', dados, tabelaAtual);
                                } else if (modal.id === 'delete') {
                                    preencherCamposModal('delete', dados, tabelaAtual);
                                }
                                modal.showModal();
                                botaoModal.disabled = false;
                            })
                            .catch(err => {
                                console.error('Erro ao buscar ou preencher dados do modal:', err);
                                alert(`Não foi possível carregar os dados.\nDetalhe: ${err.message}`);
                                botaoModal.disabled = false;
                            });
                    } else {
                        console.error("Botão 'abrir' (Update/Delete) sem 'data-caminho'", botaoModal);
                        alert("Dados incompletos no botão para carregar informações.");
                        botaoModal.disabled = false;
                    }
                } else if (acao === 'fechar') {
                    modal.close();
                    botaoModal.disabled = false;
                }
                else {
                    console.warn("Ação desconhecida no botão modal:", acao, botaoModal);
                    botaoModal.disabled = false;
                }
            } else {
                console.error("Modal não encontrado:", botaoModal.dataset.modal);
                botaoModal.disabled = false;
            }
        });
    });

    // --- BLOCO REMOVIDO (LINHAS 137-158) ---
    // A lógica de "clicar uma vez" para 'formCreate' foi removida
    // pois já está integrada no listener de 'submit' (linhas 57-75).

    // --- LÓGICA PARA O BOTÃO DE CONFIRMAR (OUTROS FORMS) ---
    // (Esta lógica está correta e agora será executada)
    const outrosFormsPost = document.querySelectorAll('form[method="post"]:not(#formCreate)');
    outrosFormsPost.forEach(form => {
        form.addEventListener('submit', (event) => {
            const submitButton = form.querySelector('button[type="submit"].confirmar');
            if (form.checkValidity()) {
                if (submitButton) submitButton.disabled = true;
            } else {
                if (submitButton) submitButton.disabled = false;
            }
        });
    });


}); // --- Fim do DOMContentLoaded ---


// --- FUNÇÕES DE PRÉ-PREENCHIMENTO DOS MODAIS ---

function preencherCamposModal(tipo, dados, tabela) {
    const sufixo = tipo.charAt(0).toUpperCase() + tipo.slice(1);
    let campos = [];
    let dadosParaCampos = dados;

    switch (tabela) {
        case 'admin-crud':
            campos = ['id', 'nome', 'email'];
            break;
        case 'empresas-crud':
            campos = ['id', 'nome', 'email', 'cep', 'cnpj', 'telefone', 'porte', 'horaAbertura', 'horaFechamento', 'regrasNegocios'];
            break;
        case 'planos-crud':
            campos = ['id', 'nome', 'custo', 'maxFuncionarios', 'descricao'];
            break;
        case 'habilidades-crud':
            campos = ['id', 'nome', 'tag', 'descricao'];
            break;
        case 'setores-crud':
            campos = ['id', 'nome', 'qtnFuncionarios', 'turnos', 'descricao', 'idEmpresa'];
            break;
        case 'usuarios-crud':
            campos = ['id', 'nome', 'cpf', 'genero', 'cargo', 'status', 'idSetor', 'idSupervisor'];
            if (dados.usuario) {
                dadosParaCampos = dados.usuario;
            } else {
                console.warn("Formato de dados para 'usuarios-crud' não continha o objeto 'usuario' aninhado. Usando a raiz.");
            }
            break;
        default:
            console.warn("Tabela não reconhecida em preencherCamposModal:", tabela);
            return;
    }

    campos.forEach(campo => {
        const elementoId = `${campo}${sufixo}`;
        const elemento = document.getElementById(elementoId);
        if (elemento) {
            elemento.value = dadosParaCampos[campo] !== null && dadosParaCampos[campo] !== undefined ? dadosParaCampos[campo] : '';
        } else {
            console.warn(`Elemento não encontrado no modal ${tipo} para ${tabela}: #${elementoId}`);
        }
    });

    if (tabela === 'usuarios-crud' && tipo === 'update') {
        const idsHabilidadesDoUsuario = dados.habilidadesDoUsuario || [];
        const todosOsCheckboxes = document.querySelectorAll('#lista-checkboxes-habilidades .habilidade-update-cb');

        if (todosOsCheckboxes.length > 0) {
            todosOsCheckboxes.forEach(checkbox => {
                const idCheckbox = parseInt(checkbox.value, 10);
                checkbox.checked = idsHabilidadesDoUsuario.includes(idCheckbox);
            });
        }
    }

    if ((tabela === 'admin-crud' || tabela === 'usuarios-crud') && tipo === 'delete') {
        const senhaDeleteEl = document.getElementById(`senhaDelete`);
        if (senhaDeleteEl) senhaDeleteEl.value = '********';
    }
}