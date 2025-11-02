<%@ page import="com.example.Model.Administracao" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%-- Bloco de Processamento de Atributos (para erros e repopulação) --%>
<%
    String erro = (String) request.getAttribute("erro");
    String modalAberto = (String) request.getAttribute("abrirModal");
    Administracao adminModal = (Administracao) request.getAttribute("adminModal");

    // Variáveis para o modal CREATE (em caso de erro)
    String createNome = "";
    String createEmail = "";
    if ("create".equals(modalAberto)) {
        createNome = request.getAttribute("nome_previo") != null ? (String)request.getAttribute("nome_previo") : "";
        createEmail = request.getAttribute("email_previo") != null ? (String)request.getAttribute("email_previo") : "";
    }

    // Variáveis para o modal UPDATE (para GET ou erro de POST)
    String updateID = "";
    String updateNome = "";
    String updateEmail = "";
    if (adminModal != null) {
        updateID = String.valueOf(adminModal.getId());
        updateNome = adminModal.getNome();
        updateEmail = adminModal.getEmail();
    }
    if ("update".equals(modalAberto) && request.getAttribute("nome_previo") != null) {
        updateNome = (String) request.getAttribute("nome_previo");
        updateEmail = (String) request.getAttribute("email_previo");
    }

    // Variáveis para o modal DELETE (para GET)
    String deleteID = "";
    String deleteNome = "";
    String deleteEmail = "";
    if (adminModal != null && "delete".equals(modalAberto)) {
        deleteID = String.valueOf(adminModal.getId());
        deleteNome = adminModal.getNome();
        deleteEmail = adminModal.getEmail();
    }

    // Pega a lista principal de admins (para a tabela)
    List<Administracao> listaAdmins = (List<Administracao>) request.getAttribute("listaAdmins");
    if (listaAdmins == null) {
        listaAdmins = new ArrayList<>();
    }
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Cinzel:wght@400;700;900&family=Montserrat:wght@300;400;500;700;900&family=Crete+Round:wght@400;700&display=swap" rel="stylesheet">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" href="${pageContext.request.contextPath}/assets/crud/img/favikronos.ico" type="image/x-icon">
    <script src="${pageContext.request.contextPath}/assets/crud/script/script.js" defer></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/crud/style/dados.css">
    <title>Administrador - Kronos CRUD</title>
</head>

<%-- Passa o modal a ser aberto (em caso de erro) para o script.js --%>

<body data-modal-para-abrir="<%= modalAberto != null ? modalAberto : "" %>">
    <div class="meuPlaceholder"></div>

    <header>
        <h1>KRONOS</h1>
        <nav>
            <ul>
                <li><a href="${pageContext.request.contextPath}/admin-crud" class="ativo"><img src="${pageContext.request.contextPath}/assets/crud/img/img-crud-administrador.png" alt="">Administrador</a></li>
                <li><a href="${pageContext.request.contextPath}/empresas-crud"><img src="${pageContext.request.contextPath}/assets/crud/img/img-crud-empresas.png" alt="">Empresas</a></li>
                <li><a href="${pageContext.request.contextPath}/planos-crud"><img src="${pageContext.request.contextPath}/assets/crud/img/img-crud-planos.png" alt="">Planos</a></li>
                <li><a href="${pageContext.request.contextPath}/habilidades-crud"><img src="${pageContext.request.contextPath}/assets/crud/img/img-crud-habilidades.png" alt="">Habilidades</a></li>
                <li><a href="${pageContext.request.contextPath}/setores-crud"><img src="${pageContext.request.contextPath}/assets/crud/img/img-crud-setores.png" alt="">Setores</a></li>
                <li><a href="${pageContext.request.contextPath}/usuarios-crud"><img src="${pageContext.request.contextPath}/assets/crud/img/img-crud-usuario.png" alt="">Usuário</a></li>
            </ul>
        </nav>
        <a href="${pageContext.request.contextPath}/landingpage/index.html" class="sairCrud">Voltar para Landing Page</a>
    </header>
    <div class="navegacaoContexto">
        <a href="${pageContext.request.contextPath}/admin-crud" class="ativo">Administrador</a>
        <a href="${pageContext.request.contextPath}/empresas-crud">Empresas</a>
        <a href="${pageContext.request.contextPath}/planos-crud">Planos</a>
        <a href="${pageContext.request.contextPath}/habilidades-crud">Habilidades</a>
        <a href="${pageContext.request.contextPath}/setores-crud">Setores</a>
        <a href="${pageContext.request.contextPath}/usuarios-crud">Usuário</a>
    </div>

    <div class="conteudoPrincipal">

        <%-- Bloco de Exibição de Erro (Parcial) --%>
            <%
            String erroParcial = (String) session.getAttribute("erro_parcial");
            if (erroParcial != null) {
        %>
        <div class="mensagem-aviso">
            <strong>Aviso:</strong> <%= erroParcial %>
        </div>
            <%
                session.removeAttribute("erro_parcial");
            }
        %>


        <div class="procurarCadastrar">

            <%-- ==== FILTRO DE PESQUISA ==== --%>
            <form class="pesquisa" method="get" action="${pageContext.request.contextPath}/admin-crud">
                <input type="search" placeholder="Pesquisar" id="pesquisa" name="pesquisa" class="buscar" value="<%= request.getParameter("pesquisa") != null ? request.getParameter("pesquisa") : "" %>">
                <details class="filtros">
                    <summary>Filtros</summary>
                    <div class="conteudoFiltros">
                        <label class="opcaoFiltro"> <input type="radio" name="ordem" value="crescente" <%= "crescente".equals(request.getParameter("ordem")) ? "checked" : "" %>> Crescente </label>
                        <label class="opcaoFiltro"> <input type="radio" name="ordem" value="decrescente" <%= "decrescente".equals(request.getParameter("ordem")) ? "checked" : "" %>> Decrescente </label>
                        <button type="reset" id="botaoLimparFiltro" data-caminho='{"base":"${pageContext.request.contextPath}","tabela":"admin-crud"}'>Limpar filtros</button>
                        <button type="submit">Aplicar</button>
                    </div>
                </details>
            </form>

                <%-- ==== CREATE ==== --%>
                <section class="create">
                <dialog id="create">
                    <h2>Cadastrar administrador</h2>
                    <% if ("create".equals(modalAberto) && erro != null && !erro.isEmpty()) { %>
                    <div class="mensagem-erro">
                        <strong>Erro:</strong> <%= erro %>
                    </div>
                    <% } %>
                    <%-- Este formulário é validado pelo script.js (lógica de senha) --%>
                    <form action="${pageContext.request.contextPath}/admin-create" id="formCreateAdmin" method="post" novalidate>
                        <div class="campos">
                            <div>
                                <div class="campo">
                                    <label for="nomeCreate">Nome</label>
                                    <input type="text" name="nome" id="nomeCreate" autocomplete="off" required value="<%= createNome %>">
                                </div>
                                <div class="campo">
                                    <label for="senhaCreate">Senha</label>
                                    <input type="password" name="senha" id="senhaCreate" autocomplete="new-password" required>
                                    <span id="senhaErro" class="erro-senha"></span>
                                </div>
                            </div>
                            <div>
                                <div class="campo">
                                    <label for="emailCreate">E-mail</label>
                                    <input type="email" name="email" id="emailCreate" required value="<%= createEmail %>">
                                </div>
                            </div>
                        </div>
                        <menu>
                            <button type="button" class="cancelar acaoModal" data-acao="fechar" data-modal="create">Cancelar</button>
                            <button type="submit" class="confirmar">Cadastrar</button>
                        </menu>
                    </form>
                </dialog>
                <button type="button" class="cadastrar acaoModal" data-acao="abrir" data-modal="create">Cadastrar</button>
            </section>

                <%-- ==== UPDATE ==== --%>
                <section class="update">
                <dialog id="update">
                    <h2>Editar administrador</h2>
                    <% if ("update".equals(modalAberto) && erro != null && !erro.isEmpty()) { %>
                    <div class="mensagem-erro">
                        <strong>Erro:</strong> <%= erro %>
                    </div>
                    <% } %>
                    <form action="${pageContext.request.contextPath}/admin-update" method="post">
                        <div class="idAtual"> <label for="idUpdate">ID:</label> <input type="number" name="id" id="idUpdate" readonly value="<%= updateID %>"> </div>
                        <div class="campos">
                            <div>
                                <div class="campo"> <label for="nomeUpdate">Nome</label> <input type="text" name="nome" id="nomeUpdate" required value="<%= updateNome %>"> </div>
                                <div class="campo"> <label for="senhaUpdate">Senha</label> <input type="password" name="senha" id="senhaUpdate" autocomplete="new-password" placeholder="Deixe em branco para manter a atual"> </div>
                            </div>
                            <div>
                                <div class="campo"> <label for="emailUpdate">E-mail</label> <input type="email" name="email" id="emailUpdate" required value="<%= updateEmail %>"> </div>
                            </div>
                        </div>
                        <menu>
                            <button type="button" class="cancelar acaoModal" data-acao="fechar" data-modal="update">Cancelar</button>
                            <button type="submit" class="confirmar">Confirmar alterações</button>
                        </menu>
                    </form>
                </dialog>
            </section>

                <%-- ==== DELETE ==== --%>
                <section class="delete">
                <dialog id="delete">
                    <h2>Excluir administrador</h2>
                    <% if ("delete".equals(modalAberto) && erro != null && !erro.isEmpty()) { %>
                    <div class="mensagem-erro">
                        <strong>Erro:</strong> <%= erro %>
                    </div>
                    <% } %>
                    <form action="${pageContext.request.contextPath}/admin-delete" method="post">
                        <div class="idAtual"> <label for="idDelete">ID:</label> <input type="number" name="id" id="idDelete" readonly value="<%= deleteID %>"> </div>
                        <div class="campos">
                            <div>
                                <div class="campo"> <label for="nomeDelete">Nome</label> <input type="text" name="nome" id="nomeDelete" disabled value="<%= deleteNome %>"> </div>
                                <div class="campo"> <label for="senhaDelete">Senha</label> <input type="password" name="senha" id="senhaDelete" disabled value="********"> </div>
                            </div>
                            <div>
                                <div class="campo"> <label for="emailDelete">E-mail</label> <input type="email" name="email" id="emailDelete" disabled value="<%= deleteEmail %>"> </div>
                            </div>
                        </div>
                        <menu>
                            <button type="button" class="cancelar acaoModal" data-acao="fechar" data-modal="delete">Cancelar</button>
                            <button type="submit" class="confirmar">Confirmar exclusão</button>
                        </menu>
                    </form>
                </dialog>
            </section>
        </div>

            <%-- ==== TABELA DE VISUALIZAÇÃO (READ) ==== --%>
            <main>
            <div class="tabelaScroll">
                <table class="tabelaAdministrador">
                    <thead> <tr> <th>Ver</th> <th>Excluir</th> <th>ID</th> <th>Nome</th> <th>E-mail</th> <th>Senha</th> </tr> </thead>
                    <tbody>
                    <%-- Loop para renderizar a tabela principal (Read) --%>
                    <%
                        if (listaAdmins != null && !listaAdmins.isEmpty()) {
                            for (Administracao admin : listaAdmins) {
                    %>
                    <tr>
                        <td> <button type="button" class="detalhes acaoModal" data-acao="abrir" data-modal="update" data-pk="<%= admin.getId() %>" data-caminho='{"base":"${pageContext.request.contextPath}","tabela":"admin-crud"}'><img src="${pageContext.request.contextPath}/assets/crud/img/mais-detalhes.png" alt="Ver/Editar"></button> </td>
                        <td> <button type="button" class="detalhes acaoModal" data-acao="abrir" data-modal="delete" data-pk="<%= admin.getId() %>" data-caminho='{"base":"${pageContext.request.contextPath}","tabela":"admin-crud"}'><img src="${pageContext.request.contextPath}/assets/crud/img/deletar-kronos.png" alt="Excluir"></button> </td>
                        <td><%= admin.getId() %></td>
                        <td><%= admin.getNome() %></td>
                        <td><%= admin.getEmail() %></td>
                        <td><%= admin.getSenha() %></td>
                    </tr>
                    <%
                        }
                    } else {
                    %>
                    <tr> <td colspan="6">Nenhum administrador encontrado.</td> </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
        </main>
    </div>
</body>
</html>