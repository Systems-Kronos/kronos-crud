<%@ page import="com.example.Model.Usuario" %>
<%@ page import="java.util.LinkedList" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <!-------------------- Fontes -------------------->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Cinzel:wght@400;700;900&family=Montserrat:wght@300;400;500;700;900&family=Crete+Round:wght@400;700&display=swap" rel="stylesheet">

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <script src="${pageContext.request.contextPath}/assets/crud/script/script.js" defer></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/crud/style/dados.css">
    <title>CRUD - Kronos</title>
</head>

<%
    String idAdquirido = request.getParameter("pk");

    String acao = request.getParameter("acao");
    boolean updateAberto = "update".equals(acao);
    boolean deleteAberto = "delete".equals(acao);
%>

<body>
    <div class="meuPlaceholder"></div>

    <header>
        <h1>KRONOS</h1>
        <nav>
            <ul>
                <li><a href="${pageContext.request.contextPath}/admin-crud"><img src="${pageContext.request.contextPath}/assets/crud/img/img-crud-administrador.png" alt="">Administrador</a></li>
                <li><a href="${pageContext.request.contextPath}/empresas-crud"><img src="${pageContext.request.contextPath}/assets/crud/img/img-crud-empresas.png" alt="">Empresas</a></li>
                <li><a href="${pageContext.request.contextPath}/planos-crud"><img src="${pageContext.request.contextPath}/assets/crud/img/img-crud-planos.png" alt="">Planos</a></li>
                <li><a href="${pageContext.request.contextPath}/habilidades-crud"><img src="${pageContext.request.contextPath}/assets/crud/img/img-crud-habilidades.png" alt="">Habilidades</a></li>
                <li><a href="${pageContext.request.contextPath}/setores-crud"><img src="${pageContext.request.contextPath}/assets/crud/img/img-crud-setores.png" alt="">Setores</a></li>
                <li><a href="${pageContext.request.contextPath}/usuarios-crud" class="ativo"><img src="${pageContext.request.contextPath}/assets/crud/img/img-crud-usuario.png" alt="">Usuário</a></li>
            </ul>
        </nav>
    </header>

    <div class="conteudoPrincipal">
        <div class="procurarCadastrar">
            <form class="pesquisa" method="get" action="${pageContext.request.contextPath}/usuarios-crud">
                <input type="search" placeholder="Pesquisar" id="pesquisa" name="pesquisa" class="buscar" value="<%= request.getParameter("pesquisa") != null ? request.getParameter("pesquisa") : "" %>">

                <details class="filtros">
                    <summary>Filtros</summary>
                    <div class="conteudoFiltros">
                        <label class="opcaoFiltro">
                            <input type="radio" name="ordem" value="crescente"
                                <%= "crescente".equals(request.getParameter("ordem")) ? "checked" : "" %>> Crescente
                        </label>
                        <label class="opcaoFiltro">
                            <input type="radio" name="ordem" value="decrescente"
                                <%= "decrescente".equals(request.getParameter("ordem")) ? "checked" : "" %>> Decrescente
                        </label>
                        <button type="reset" onclick="window.location='${pageContext.request.contextPath}/usuarios-crud'">Limpar filtros</button>
                        <button type="submit">Aplicar</button>
                    </div>
                </details>
            </form>

            <!-- CREATE -->

            <section class="create">
                <dialog id="create">
                    <h2>Cadastrar usuário</h2>
                    <form action="" method="post">
                        <div class="campos">
                            <div>
                                <div class="campo">
                                    <label for="nomeCreate">Nome</label>
                                    <input type="text" name="nome" id="nomeCreate" autocomplete="off" required>
                                </div>
                                <div class="campo">
                                    <label for="cpfCreate">CPF</label>
                                    <input type="text" name="cpf" id="cpfCreate" autocomplete="off" required>
                                </div>
                                <div class="campo">
                                    <label for="senhaCreate">Senha</label>
                                    <input type="password" name="senha" id="senhaCreate" autocomplete="new-password" required minlength="8">
                                </div>
                                <div class="campoLado">
                                    <div class="campo">
                                        <label for="generoCreate">Gênero</label>
                                        <select name="genero" id="generoCreate" required>
                                            <option value="M">M</option>
                                            <option value="F">F</option>
                                            <option value="O">O</option>
                                        </select>
                                    </div>
                                    <div class="campo">
                                        <label for="statusCreate">Status</label>
                                        <select name="status" id="statusCreate" required>
                                            <option value="Ativo" selected>Ativo</option>
                                            <option value="Inativo">Inativo</option>
                                            <option value="Férias">Férias</option>
                                            <option value="Desligado">Desligado</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div>
                                <div class="campo">
                                    <label for="idSetorCreate">ID do Setor</label>
                                    <input type="number" name="idSetor" id="idSetorCreate" min="1" required>
                                </div>
                                <div class="campo">
                                    <label for="idSupervisorCreate">ID do Supervisor</label>
                                    <input type="number" name="idSupervisor" id="idSupervisorCreate" min="1" required>
                                </div>
                            </div>
                        </div>
                        <menu>
                            <button type="submit">Cadastrar</button>
                            <button type="button" class="acaoModal" data-acao="fechar" data-modal="create">Cancelar</button>
                        </menu>
                    </form>
                </dialog>

                <button type="button" class="cadastrar acaoModal" data-acao="abrir" data-modal="create">Cadastrar</button>
            </section>


            <!-- UPDATE -->

            <section class="update">
                <dialog id="update" data-abrir="<%=updateAberto%>">
                    <h2>Editar usuário</h2>
                    <form action="" method="post">
                        <div class="campos">
                            <div>
                                <div class="campo">
                                    <label for="nomeUpdate">Nome</label>
                                    <input type="text" name="nome" id="nomeUpdate" autocomplete="off" required>
                                </div>
                                <div class="campo">
                                    <label for="cpfUpdate">CPF</label>
                                    <input type="text" name="cpf" id="cpfUpdate" autocomplete="off" required>
                                </div>
                                <div class="campo">
                                    <label for="senhaUpdate">Senha</label>
                                    <input type="password" name="senha" id="senhaUpdate" autocomplete="new-password" required minlength="8">
                                </div>
                                <div class="campoLado">
                                    <div class="campo">
                                        <label for="generoUpdate">Gênero</label>
                                        <select name="genero" id="generoUpdate" required>
                                            <option value="M">M</option>
                                            <option value="F">F</option>
                                            <option value="O">O</option>
                                        </select>
                                    </div>
                                    <div class="campo">
                                        <label for="statusUpdate">Status</label>
                                        <select name="status" id="statusUpdate" required>
                                            <option value="Ativo" selected>Ativo</option>
                                            <option value="Inativo">Inativo</option>
                                            <option value="Férias">Férias</option>
                                            <option value="Desligado">Desligado</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div>
                                <div class="campo">
                                    <label for="idSetorUpdate">ID do Setor</label>
                                    <input type="number" name="idSetor" id="idSetorUpdate" min="1" required>
                                </div>
                                <div class="campo">
                                    <label for="idSupervisorUpdate">ID do Supervisor</label>
                                    <input type="number" name="idSupervisor" id="idSupervisorUpdate" min="1" required>
                                </div>
                            </div>
                        </div>
                        <menu>
                            <button type="submit">Confirmar alterações</button>
                            <button type="button" class="acaoModal" data-acao="fechar" data-modal="update">Cancelar</button>
                        </menu>
                    </form>
                </dialog>
            </section>

            <!-- DELETE -->

            <section class="delete">
                <dialog id="delete" data-abrir="<%=deleteAberto%>">
                    <h2>Excluir usuário</h2>
                    <form action="" method="post">
                        <div class="campos">
                            <div>
                                <div class="campo">
                                    <label for="nomeDelete">Nome</label>
                                    <input type="text" name="nome" id="nomeDelete" autocomplete="off" disabled>
                                </div>
                                <div class="campo">
                                    <label for="cpfDelete">CPF</label>
                                    <input type="text" name="cpf" id="cpfDelete" autocomplete="off" disabled>
                                </div>
                                <div class="campo">
                                    <label for="senhaDelete">Senha</label>
                                    <input type="password" name="senha" id="senhaDelete" autocomplete="new-password" disabled>
                                </div>
                                <div class="campoLado">
                                    <div class="campo">
                                        <label for="generoDelete">Gênero</label>
                                        <select name="genero" id="generoDelete">
                                            <option value="M">M</option>
                                            <option value="F">F</option>
                                            <option value="O">O</option>
                                        </select>
                                    </div>
                                    <div class="campo">
                                        <label for="statusDelete">Status</label>
                                        <select name="status" id="statusDelete">
                                            <option value="Ativo" selected>Ativo</option>
                                            <option value="Inativo">Inativo</option>
                                            <option value="Férias">Férias</option>
                                            <option value="Desligado">Desligado</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div>
                                <div class="campo">
                                    <label for="idSetorDelete">ID do Setor</label>
                                    <input type="number" name="idSetor" id="idSetorDelete" min="1" disabled>
                                </div>
                                <div class="campo">
                                    <label for="idSupervisorDelete">ID do Supervisor</label>
                                    <input type="number" name="idSupervisor" id="idSupervisorDelete" min="1" disabled>
                                </div>
                            </div>
                        </div>
                        <menu>
                            <button type="submit" value="true">Confirmar exclusão</button>
                            <button type="button" class="acaoModal" data-acao="fechar" data-modal="delete" value="false">Cancelar</button>
                        </menu>
                    </form>
                </dialog>
            </section>
        </div>

            <!-- READ -->

        <main>
            <div class="tabelaScroll">
                <table class="tabelaHabilidades">
                    <thead>
                        <tr>
                            <th>Ver</th>
                            <th>ID</th>
                            <th>Nome</th>
                            <th>CPF</th>
                            <th>Senha</th>
                            <th>Gênero</th>
                            <th>Status</th>
                            <th>Setor</th>
                            <th>Supervisor</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                    <%
                        LinkedList<Usuario> usuarios = (LinkedList<com.example.Model.Usuario>) request.getAttribute("usuarios");
                        if (usuarios != null && !usuarios.isEmpty()) {
                            for (com.example.Model.Usuario u : usuarios) {
                    %>
                        <tr>
                            <td>
                                <form method="get"><input type="hidden" name="acao" value="update"><input type="hidden" name="pk" value="<%= u.getId() %>"><button type="submit" class="detalhes"><img src="${pageContext.request.contextPath}/assets/crud/img/mais-detalhes.png" alt=""></button></form>
                            </td>
                            <td><%= u.getId() %></td>
                            <td><%= u.getNome() %></td>
                            <td><%= u.getCpf() %></td>
                            <td><%= u.getSenha() %></td>
                            <td><%= u.getGenero() %></td>
                            <td><%= u.getStatus() %></td>
                            <td> Setor Join</td>
                            <td> Supervisor Join</td>
                            <td>
                                <form method="get"><input type="hidden" name="acao" value="delete"><input type="hidden" name="pk" value="<%= u.getId() %>"><button type="submit" class="detalhes"><img src="${pageContext.request.contextPath}/assets/crud/img/deletar-kronos.png" alt=""></button></form>
                            </td>
                        </tr>
                    <%
                        }
                    } else {
                    %>
                        <tr>
                            <td colspan="10">Nenhum usuário encontrado.</td>
                        </tr>
                    <%
                        }
                    %>
                    </tbody>
                </table>
            </div>
        </main>
    </div>

    <!-- MÉTODOS DE VALIDAÇÃO -->

    <script>
        document.addEventListener('DOMContentLoaded', () => {
            // Anexa o validador ao formulário do CREATE
            const formCreate = document.querySelector('#create form');
            if (formCreate) {
                formCreate.addEventListener('submit', (event) => {
                    validarFormulario(event, 'senhaCreate', 'cpfCreate');
                });
            }

            // Anexa o validador ao formulário do UPDATE
            const formUpdate = document.querySelector('#update form');
            if (formUpdate) {
                formUpdate.addEventListener('submit', (event) => {
                    validarFormulario(event, 'senhaUpdate', 'cpfUpdate');
                });
            }
        });

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
    </script>
</body>
</html>
