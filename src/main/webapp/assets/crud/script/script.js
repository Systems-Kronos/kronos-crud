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
            // Previne o comportamento padrão 'reset' do formulário
            event.preventDefault();
            const caminho = JSON.parse(botaoLimparFiltro.dataset.caminho);
            const caminhoBase = caminho.base;
            const tabelaAtual = caminho.tabela;
            window.location.href = `${caminhoBase}/${tabelaAtual}`;
        });
    }

    // --- LÓGICA PARA VALIDAÇÃO ESPECÍFICA DE SENHA ---
    // (Este bloco agora também lida com a lógica de "clique único" para este formulário)

    // NOTA: Esta lógica presume que 'formCreate', 'senhaCreate' e 'senhaErro' são os IDs
    // tanto na página de admin quanto na de usuário. Se forem diferentes,
    // a lógica precisará ser duplicada com os IDs corretos.
    const formCreate = document.getElementById('formCreate');
    const senhaInput = document.getElementById('senhaCreate');
    const senhaErrorSpan = document.getElementById('senhaErro');

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

    // Valida a senha enquanto digita (feedback em tempo real)
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

    // Listener de submit ÚNICO E COMBINADO para 'formCreate'
    if (formCreate) {
        formCreate.addEventListener('submit', (event) => {
            const submitButton = formCreate.querySelector('button[type="submit"].confirmar');
            let mensagemErroSenha = "";

            // 1. Valida a senha (se o campo de senha existir)
            if (senhaInput && senhaErrorSpan) {
                mensagemErroSenha = validarSenha(senhaInput.value);
                senhaErrorSpan.textContent = mensagemErroSenha;
            }

            // 2. Verifica se a senha OU outros campos (required, email, etc) falharam
            //    (Se o form tiver 'novalidate', checkValidity() ainda funciona)
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

    // --- BLOCO REMOVIDO (LINHAS 78-89 do seu código) ---
    // O bloco que começava com 'if (formCreateAdmin)' foi removido
    // pois estava quebrado (ReferenceError) e redundante.
    // --- FIM DA REMOÇÃO ---


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
                        // Limpa qualquer span de erro de senha dentro do modal create
                        const errorSpans = modal.querySelectorAll('.error-message, .erro-senha'); // Pega ambas as classes
                        if (errorSpans) {
                            errorSpans.forEach(span => span.textContent = '');
                        }

                        // Reseta o formulário para limpar campos
                        const form = modal.querySelector('form');
                        if (form) form.reset();

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

    // --- BLOCO REMOVIDO (LINHAS 137-158 do seu código) ---
    // A lógica de "clicar uma vez" para 'formCreate' foi removida
    // pois já está integrada no listener de 'submit' combinado (linhas 62-85).
    // --- FIM DA REMOÇÃO ---

    // --- LÓGICA PARA O BOTÃO DE CONFIRMAR (OUTROS FORMS) ---
    // (Esta lógica está correta e agora será executada)
    const outrosFormsPost = document.querySelectorAll('form[method="post"]:not(#formCreate)');
    outrosFormsPost.forEach(form => {
        form.addEventListener('submit', (event) => {
            const submitButton = form.querySelector('button[type="submit"].confirmar');
            // Desabilita o botão SOMENTE se a validação HTML5 do navegador passar
            if (form.checkValidity()) {
                if (submitButton) submitButton.disabled = true;
            } else {
                // Se a validação falhar, o navegador (sem 'novalidate')
                // mostrará os erros, e o botão deve permanecer ativo
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
            campos = ['id', 'nome', 'email', 'cep', 'cnpj', 'telefone', 'porte', 'horaAbertura', 'horaFechamento', 'regrasNegocios', 'plano']; // 'plano' adicionado se o ID for 'planoUpdate'/'planoDelete'
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
            // Renomeia 'idPlano' do JSON para 'plano' do ID do select
            let valor = (campo === 'plano' && dadosParaCampos['idPlano']) ? dadosParaCampos['idPlano'] : dadosParaCampos[campo];
            elemento.value = valor !== null && valor !== undefined ? valor : '';
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