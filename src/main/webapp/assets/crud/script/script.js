document.addEventListener('DOMContentLoaded', () => {

    // --- LÓGICA DE AUTO-ABERTURA DE MODAL EM CASO DE ERRO ---
    const modalIdParaAbrir = document.body.dataset.modalParaAbrir;
    if (modalIdParaAbrir) {
        const modal = document.getElementById(modalIdParaAbrir);
        if (modal && typeof modal.showModal === 'function') {
            try {
                modal.showModal();
            } catch (e) {
                console.error("Erro ao tentar abrir modal automaticamente:", modalIdParaAbrir, e);
            }
        } else if (modalIdParaAbrir) {
            console.error("Tentativa de abrir um modal que não existe:", modalIdParaAbrir);
        }
    }

    // --- LÓGICA PARA BOTÕES DE FILTRAGEM ---
    const botaoLimparFiltro = document.getElementById('botaoLimparFiltro');
    if (botaoLimparFiltro) {
        botaoLimparFiltro.addEventListener('click', function (event) {
            event.preventDefault(); // Previne o reset do formulário
            try {
                const caminho = JSON.parse(botaoLimparFiltro.dataset.caminho);
                window.location.href = `${caminho.base}/${caminho.tabela}`;
            } catch (e) {
                console.error("Erro ao processar 'data-caminho' do botão Limpar Filtros:", e);
            }
        });
    }

    // --- LÓGICA GENÉRICA PARA VALIDAÇÃO ESPECÍFICA DE SENHA ---
    // (Procura por classes CSS, funcionando em admin.jsp e usuario.jsp)
    // (Seus JSPs precisam ter as classes: .form-validar-senha, .input-senha-validar, .span-erro-senha)

    // Função de validação
    function validarSenha(senha) {
        if (!senha) return "A senha não pode estar em branco.";
        if (senha.length < 8) return "A senha deve ter no mínimo 8 caracteres.";
        if (!/[a-z]/.test(senha)) return "A senha deve ter no mínimo 1 letra minúscula.";
        if (!/[A-Z]/.test(senha)) return "A senha deve ter no mínimo 1 letra maiúscula.";
        if (!/\d/.test(senha)) return "A senha deve ter no mínimo 1 dígito.";
        if (!/[^\sA-Za-z0-9]/.test(senha)) return "A senha deve ter no mínimo 1 caractere especial.";
        return ""; // Válida
    }

    // Encontra TODOS os formulários que precisam de validação de senha
    const formCreate = document.getElementById('formCreate');

    if (formCreate) {
        // Encontra os elementos DENTRO do formulário 'formCreate'
        // NOTA: Seus JSPs (admin e usuário) usam os mesmos IDs 'senhaCreate' e 'senhaErro'.
        // Isso NÃO é ideal, mas o seletor abaixo vai funcionar na página que estiver carregada.
        const senhaInput = formCreate.querySelector('#senhaCreate');
        const senhaErrorSpan = formCreate.querySelector('#senhaErro'); // Procura 'senhaErro'
        const submitButton = formCreate.querySelector('button[type="submit"].confirmar');

        // Adiciona listener de 'input' (feedback em tempo real)
        if (senhaInput && senhaErrorSpan) {
            senhaInput.addEventListener('input', () => {
                const mensagemErro = validarSenha(senhaInput.value);
                senhaErrorSpan.textContent = mensagemErro;
            });
            // Limpa erro ao fechar modal
            const parentDialog = formCreate.closest('dialog');
            if (parentDialog) {
                parentDialog.addEventListener('close', () => {
                    senhaErrorSpan.textContent = '';
                });
            }
        }

        // Adiciona listener de 'submit' ao formulário
        formCreate.addEventListener('submit', (event) => {
            let mensagemErroSenha = "";
            if (senhaInput && senhaErrorSpan) {
                mensagemErroSenha = validarSenha(senhaInput.value);
                senhaErrorSpan.textContent = mensagemErroSenha;
            }

            // Verifica se a senha OU outros campos (required) falharam
            // (Seus JSPs não usam 'novalidate', então o checkValidity() falhará
            // E o navegador mostrará as mensagens de 'required'.)
            if (mensagemErroSenha || !formCreate.checkValidity()) {
                event.preventDefault(); // Impede o envio
                if (submitButton) submitButton.disabled = false; // Reabilita o botão
            } else {
                if (submitButton) submitButton.disabled = true; // Desabilita no sucesso
            }
        });
    }


    // --- LÓGICA PARA ABRIR/FECHAR MODAIS COM CLIQUE ---
    // (Este código agora será executado, pois o erro de ID foi corrigido)
    const botoesModal = document.querySelectorAll('.acaoModal');
    const loading = document.querySelector('.carregando');
    botoesModal.forEach(botaoModal => {
        botaoModal.addEventListener('click', () => {
            botaoModal.disabled = true;
            const modal = document.getElementById(botaoModal.dataset.modal);
            const acao = botaoModal.dataset.acao;
            loading.classList.add('mostrar');

            if (modal) {
                if (acao === 'abrir') {

                    if (modal.id === 'create') {
                        // Limpa spans de erro e reseta o formulário
                        const errorSpans = modal.querySelectorAll('.error-message, .erro-senha');
                        if (errorSpans) errorSpans.forEach(span => span.textContent = '');

                        const form = modal.querySelector('form');
                        if (form) form.reset();

                        modal.showModal();
                        loading.classList.remove('mostrar');
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
                                loading.classList.remove('mostrar');
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
                    loading.classList.remove('mostrar');
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
        loading.classList.remove('mostrar');
    });

    // --- LÓGICA PARA O BOTÃO DE CONFIRMAR (OUTROS FORMS - Update/Delete) ---
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
            campos = ['id', 'nome', 'email', 'cep', 'cnpj', 'telefone', 'porte', 'horaAbertura', 'horaFechamento', 'regrasNegocios', 'plano'];
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
            campos = ['id', 'nome', 'cpf', 'telefone', 'genero', 'cargo', 'status', 'idSetor', 'idSupervisor'];
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

    // Preenche campos de texto, select, etc.
    campos.forEach(campo => {
        const elementoId = `${campo}${sufixo}`;
        const elemento = document.getElementById(elementoId);
        if (elemento) {
            let valor = (campo === 'plano' && dadosParaCampos['idPlano']) ? dadosParaCampos['idPlano'] : dadosParaCampos[campo];
            valor = (campo === 'idEmpresa' && dadosParaCampos['idEmpresa']) ? dadosParaCampos['idEmpresa'] : valor;

            elemento.value = valor !== null && valor !== undefined ? valor : '';
        } else {
            console.warn(`Elemento não encontrado no modal ${tipo} para ${tabela}: #${elementoId}`);
        }
    });

    if (tabela === 'usuarios-crud' && tipo === 'update') {
        const idsHabilidadesDoUsuario = dados.habilidadesDoUsuario || [];
        const todosOsCheckboxes = document.querySelectorAll('#listaHabilidadesUpdate .habilidade-update-cb');

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