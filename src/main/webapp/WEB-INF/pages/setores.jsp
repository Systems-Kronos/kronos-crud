<%@ page import="com.example.Model.Setor" %>
<%@ page import="com.example.Model.Empresa" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%-- Bloco de Processamento de Atributos (para erros e repopulação) --%>
<%
    /* --- Processamento de Atributos (Setor) --- */
    String erro = (String) request.getAttribute("erro");
    String modalAberto = (String) request.getAttribute("abrirModal");
    Setor setorModal = (Setor) request.getAttribute("setorModal");

    // Pega a lista de TODAS as empresas (para os dropdowns)
    List<Empresa> listaEmpresas = (List<Empresa>) request.getAttribute("listaEmpresas");
    if (listaEmpresas == null) {
        listaEmpresas = new ArrayList<>();
    }

    // Pega a lista de SETORES (para a tabela principal)
    List<Setor> listaSetores = (List<Setor>) request.getAttribute("listaSetores");
    if (listaSetores == null) {
        listaSetores = new ArrayList<>();
    }

    // Variáveis CREATE (pós-erro)
    String createNome = "";
    String createTurnos = "";
    String createQtdFunc = "";
    String createDescricao = "";
    String createIdEmpresa = "";

    if ("create".equals(modalAberto)) {
        createNome = request.getAttribute("nome_previo") != null ? (String)request.getAttribute("nome_previo") : "";
        createTurnos = request.getAttribute("turnos_previo") != null ? (String)request.getAttribute("turnos_previo") : "";
        createQtdFunc = request.getAttribute("qtdFuncionarios_previo") != null ? (String)request.getAttribute("qtdFuncionarios_previo") : "";
        createDescricao = request.getAttribute("descricao_previo") != null ? (String)request.getAttribute("descricao_previo") : "";
        createIdEmpresa = request.getAttribute("idEmpresa_previo") != null ? (String)request.getAttribute("idEmpresa_previo") : "";
    }

    // Variáveis UPDATE
    String updateID = "";
    String updateNome = "";
    String updateTurnos = "";
    String updateQtdFunc = "";
    String updateDescricao = "";
    String updateIdEmpresa = "";

    if (setorModal != null) { // GET ou POST-failure
        updateID = String.valueOf(setorModal.getId());
        updateNome = setorModal.getNome();
        updateTurnos = setorModal.getTurnos();
        updateQtdFunc = String.valueOf(setorModal.getQntFuncionarios());
        updateDescricao = setorModal.getDescricao();
        updateIdEmpresa = String.valueOf(setorModal.getIdEmpresa());
    }
    if ("update".equals(modalAberto) && request.getAttribute("nome_previo") != null) { // Erro no POST
        updateNome = (String) request.getAttribute("nome_previo");
        updateTurnos = (String) request.getAttribute("turnos_previo");
        updateQtdFunc = (String) request.getAttribute("qtdFuncionarios_previo");
        updateDescricao = (String) request.getAttribute("descricao_previo");
        updateIdEmpresa = (String) request.getAttribute("idEmpresa_previo");
    }

    // Variáveis DELETE
    String deleteID = "";
    String deleteNome = "";
    String deleteTurnos = "";
    String deleteQtdFunc = "";
    String deleteDescricao = "";
    String deleteIdEmpresa = "";

    if (setorModal != null && "delete".equals(modalAberto)) {
        deleteID = String.valueOf(setorModal.getId());
        deleteNome = setorModal.getNome();
        deleteTurnos = setorModal.getTurnos();
        deleteQtdFunc = String.valueOf(setorModal.getQntFuncionarios());
        deleteDescricao = setorModal.getDescricao();
        deleteIdEmpresa = String.valueOf(setorModal.getIdEmpresa());
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
    <title>Setores - Kronos CRUD</title>
</head>

<%-- Passa o modal a ser aberto (em caso de erro) para o script.js --%>
<body data-modal-para-abrir="<%= modalAberto != null ? modalAberto : "" %>">
    <div class="meuPlaceholder"></div>

    <header>
        <h1>KRONOS</h1>
        <nav>
            <ul>
                <li><a href="${pageContext.request.contextPath}/admin-crud"><img src="${pageContext.request.contextPath}/assets/crud/img/img-crud-administrador.png" alt="">Administrador</a></li>
                <li><a href="${pageContext.request.contextPath}/empresas-crud"><img src="${pageContext.request.contextPath}/assets/crud/img/img-crud-empresas.png" alt="">Empresas</a></li>
                <li><a href="${pageContext.request.contextPath}/planos-crud"><img src="${pageContext.request.contextPath}/assets/crud/img/img-crud-planos.png" alt="">Planos</a></li>
                <li><a href="${pageContext.request.contextPath}/habilidades-crud"><img src="${pageContext.request.contextPath}/assets/crud/img/img-crud-habilidades.png" alt="">Habilidades</a></li>
                <li><a href="${pageContext.request.contextPath}/setores-crud" class="ativo"><img src="${pageContext.request.contextPath}/assets/crud/img/img-crud-setores.png" alt="">Setores</a></li>
                <li><a href="${pageContext.request.contextPath}/usuarios-crud"><img src="${pageContext.request.contextPath}/assets/crud/img/img-crud-usuario.png" alt="">Usuário</a></li>
            </ul>
        </nav>
        <a href="${pageContext.request.contextPath}/landingpage/index.html" class="sairCrud">Voltar para Landing Page</a>
    </header>
    <div class="navegacaoContexto">
        <a href="${pageContext.request.contextPath}/admin-crud">Administrador</a>
        <a href="${pageContext.request.contextPath}/empresas-crud">Empresas</a>
        <a href="${pageContext.request.contextPath}/planos-crud">Planos</a>
        <a href="${pageContext.request.contextPath}/habilidades-crud">Habilidades</a>
        <a href="${pageContext.request.contextPath}/setores-crud" class="ativo">Setores</a>
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
            <form class="pesquisa" method="get" action="${pageContext.request.contextPath}/setores-crud">
                <input type="search" placeholder="Pesquisar" id="pesquisa" name="pesquisa" class="buscar" value="<%= request.getParameter("pesquisa") != null ? request.getParameter("pesquisa") : "" %>">
                <details class="filtros">
                    <summary>Filtros</summary>
                    <div class="conteudoFiltros">
                        <label class="opcaoFiltro"> <input type="radio" name="ordem" value="crescente" <%= "crescente".equals(request.getParameter("ordem")) ? "checked" : "" %>> Crescente </label>
                        <label class="opcaoFiltro"> <input type="radio" name="ordem" value="decrescente" <%= "decrescente".equals(request.getParameter("ordem")) ? "checked" : "" %>> Decrescente </label>
                        <button type="reset" id="botaoLimparFiltro" data-caminho='{"base":"${pageContext.request.contextPath}","tabela":"setores-crud"}'>Limpar filtros</button>
                        <button type="submit">Aplicar</button>
                    </div>
                </details>
            </form>

            <%-- ==== CREATE ==== --%>
            <section class="create">
                <dialog id="create">
                    <h2>Cadastrar setor</h2>
                    <% if ("create".equals(modalAberto) && erro != null && !erro.isEmpty()) { %>
                    <div class="mensagem-erro">
                        <strong>Erro:</strong> <%= erro %>
                    </div>
                    <% } %>
                    <form action="${pageContext.request.contextPath}/setor-create" id="formCreate" method="post">
                        <div class="campos">
                            <div>
                                <div class="campo"> <label for="nomeCreate">Nome</label> <input type="text" name="nome" id="nomeCreate" autocomplete="off" required value="<%= createNome %>"> </div>
                                <div class="campo"> <label for="qtnFuncionariosCreate">Número de funcionários</label> <input type="number" name="qtnFuncionarios" id="qtnFuncionariosCreate" min="0" required value="<%= createQtdFunc %>"> </div>
                                <div class="campo">
                                    <label for="turnosCreate">Turnos</label>
                                    <select name="turnos" id="turnosCreate" required>
                                        <option value="" disabled <%= createTurnos.isEmpty() ? "selected" : "" %>>Selecionar</option>
                                        <option value="Integral" <%= "Integral".equals(createTurnos) ? "selected" : "" %>>Integral</option>
                                        <option value="Manhã" <%= "Manhã".equals(createTurnos) ? "selected" : "" %>>Manhã</option>
                                        <option value="Tarde" <%= "Tarde".equals(createTurnos) ? "selected" : "" %>>Tarde</option>
                                        <option value="Noite" <%= "Noite".equals(createTurnos) ? "selected" : "" %>>Noite</option>
                                        <option value="Madrugada" <%= "Madrugada".equals(createTurnos) ? "selected" : "" %>>Madrugada</option>
                                    </select>
                                </div>
                            </div>
                            <div>
                                <div class="campo"> <label for="descricaoCreate">Descrição</label> <textarea type="text" name="descricao" id="descricaoCreate" required><%= createDescricao %></textarea> </div>
                                <div class="campo">
                                    <label for="idEmpresaCreate">Empresa</label>
                                    <select name="idEmpresa" id="idEmpresaCreate" required>
                                        <option value="" <%= createIdEmpresa.isEmpty() ? "selected" : "" %> disabled>Selecionar Empresa</option>
                                        <% for (Empresa empresa : listaEmpresas) { %>
                                        <option value="<%= empresa.getId() %>" <%= String.valueOf(empresa.getId()).equals(createIdEmpresa) ? "selected" : "" %>>
                                            <%= empresa.getId() %> - <%= empresa.getNome() %>
                                        </option>
                                        <% } %>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <menu> <button type="button" class="cancelar acaoModal" data-acao="fechar" data-modal="create">Cancelar</button> <button type="submit" class="confirmar">Cadastrar</button> </menu>
                    </form>
                </dialog>
                <button type="button" class="cadastrar acaoModal" data-acao="abrir" data-modal="create">Cadastrar</button>
            </section>

            <%-- ==== UPDATE ==== --%>
            <section class="update">
                <dialog id="update">
                    <h2>Editar setor</h2>
                    <% if ("update".equals(modalAberto) && erro != null && !erro.isEmpty()) { %>
                    <div class="mensagem-erro">
                        <strong>Erro:</strong> <%= erro %>
                    </div>
                    <% } %>
                    <form action="${pageContext.request.contextPath}/setor-update" method="post">
                        <div class="idAtual"> <label for="idUpdate">ID:</label> <input type="number" name="id" id="idUpdate" readonly value="<%= updateID %>"> </div>
                        <div class="campos">
                            <div>
                                <div class="campo"> <label for="nomeUpdate">Nome</label> <input type="text" name="nome" id="nomeUpdate" autocomplete="off" required value="<%= updateNome %>"> </div>
                                <div class="campo"> <label for="qtnFuncionariosUpdate">Número de funcionários</label> <input type="number" name="qtnFuncionarios" id="qtnFuncionariosUpdate" min="0" required value="<%= updateQtdFunc %>"> </div>
                                <div class="campo">
                                    <label for="turnosUpdate">Turnos</label>
                                    <select name="turnos" id="turnosUpdate" required>
                                        <option value="" disabled <%= updateTurnos.isEmpty() ? "selected" : "" %>>Selecionar</option>
                                        <option value="Integral" <%= "Integral".equals(updateTurnos) ? "selected" : "" %>>Integral</option>
                                        <option value="Manhã" <%= "Manhã".equals(updateTurnos) ? "selected" : "" %>>Manhã</option>
                                        <option value="Tarde" <%= "Tarde".equals(updateTurnos) ? "selected" : "" %>>Tarde</option>
                                        <option value="Noite" <%= "Noite".equals(updateTurnos) ? "selected" : "" %>>Noite</option>
                                        <option value="Madrugada" <%= "Madrugada".equals(updateTurnos) ? "selected" : "" %>>Madrugada</option>
                                    </select>
                                </div>
                            </div>
                            <div>
                                <div class="campo"> <label for="descricaoUpdate">Descrição</label> <textarea type="text" name="descricao" id="descricaoUpdate" required><%= updateDescricao %></textarea> </div>
                                <div class="campo">
                                    <label for="idEmpresaUpdate">Empresa</label>
                                    <select name="idEmpresa" id="idEmpresaUpdate" required>
                                        <option value="" <%= updateIdEmpresa.isEmpty() ? "selected" : "" %> disabled>Selecionar Empresa</option>
                                        <% for (Empresa empresa : listaEmpresas) { %>
                                        <option value="<%= empresa.getId() %>" <%= String.valueOf(empresa.getId()).equals(updateIdEmpresa) ? "selected" : "" %>>
                                            <%= empresa.getId() %> - <%= empresa.getNome() %>
                                        </option>
                                        <% } %>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <menu> <button type="button" class="cancelar acaoModal" data-acao="fechar" data-modal="update">Cancelar</button> <button type="submit" class="confirmar">Confirmar alterações</button> </menu>
                    </form>
                </dialog>
            </section>

            <%-- ==== DELETE ==== --%>
            <section class="delete">
                <dialog id="delete">
                    <h2>Excluir setor</h2>
                    <% if ("delete".equals(modalAberto) && erro != null && !erro.isEmpty()) { %>
                    <div class="mensagem-erro">
                        <strong>Erro:</strong> <%= erro %>
                    </div>
                    <% } %>
                    <form action="${pageContext.request.contextPath}/setores-delete" method="post">
                        <div class="idAtual"> <label for="idDelete">ID:</label> <input type="number" name="id" id="idDelete" readonly value="<%= deleteID %>"> </div>
                        <div class="campos">
                            <div>
                                <div class="campo"> <label for="nomeDelete">Nome</label> <input type="text" name="nome" id="nomeDelete" autocomplete="off" disabled value="<%= deleteNome %>"> </div>
                                <div class="campo"> <label for="qtnFuncionariosDelete">Número de funcionários</label> <input type="number" name="qtnFuncionarios" id="qtnFuncionariosDelete" min="1" disabled value="<%= deleteQtdFunc %>"> </div>
                                <div class="campo">
                                    <label for="turnosDelete">Turnos</label>
                                    <select name="turnos" id="turnosDelete" disabled>
                                        <option value="" disabled <%= deleteTurnos.isEmpty() ? "selected" : "" %>>Selecionar</option>
                                        <option value="Integral" <%= "Integral".equals(deleteTurnos) ? "selected" : "" %>>Integral</option>
                                        <option value="Manhã" <%= "Manhã".equals(deleteTurnos) ? "selected" : "" %>>Manhã</option>
                                        <option value="Tarde" <%= "Tarde".equals(deleteTurnos) ? "selected" : "" %>>Tarde</option>
                                        <option value="Noite" <%= "Noite".equals(deleteTurnos) ? "selected" : "" %>>Noite</option>
                                        <option value="Madrugada" <%= "Madrugada".equals(deleteTurnos) ? "selected" : "" %>>Madrugada</option>
                                    </select>
                                </div>
                            </div>
                            <div>
                                <div class="campo"> <label for="descricaoDelete">Descrição</label> <textarea type="text" name="descricao" id="descricaoDelete" disabled><%= deleteDescricao %></textarea> </div>
                                <div class="campo">
                                    <label for="idEmpresaDelete">Empresa</label>
                                    <select name="idEmpresa" id="idEmpresaDelete" disabled>
                                        <option value="" <%= deleteIdEmpresa.isEmpty() ? "selected" : "" %> disabled>Selecionar Empresa</option>
                                        <% for (Empresa empresa : listaEmpresas) { %>
                                        <option value="<%= empresa.getId() %>" <%= String.valueOf(empresa.getId()).equals(deleteIdEmpresa) ? "selected" : "" %>>
                                            <%= empresa.getId() %> - <%= empresa.getNome() %>
                                        </option>
                                        <% } %>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <menu> <button type="button" class="cancelar acaoModal" data-acao="fechar" data-modal="delete">Cancelar</button> <button type="submit" class="confirmar">Confirmar exclusão</button> </menu>
                    </form>
                </dialog>
            </section>
        </div>

        <%-- ==== TABELA DE VISUALIZAÇÃO (READ) ==== --%>
        <main>
            <div class="tabelaScroll">
                <table class="tabelaHabilidades">
                    <thead>
                    <tr>
                        <th>Ver</th>
                        <th>Excluir</th>
                        <th>ID</th>
                        <th>Nome</th>
                        <th>Empresa (ID)</th>
                        <th>Quantidade de Funcionários</th>
                        <th>Turnos</th>
                        <th>Descrição</th>
                    </tr>
                    </thead>
                    <tbody>
                    <%
                        if (listaSetores != null && !listaSetores.isEmpty()) {
                            for (Setor setor : listaSetores) {
                    %>
                    <tr>
                        <%-- O <td> do botão "Ver" está presente --%>
                        <td> <button type="button" class="detalhes acaoModal" data-acao="abrir" data-modal="update" data-pk="<%= setor.getId() %>" data-caminho='{"base":"${pageContext.request.contextPath}","tabela":"setores-crud"}'><img src="${pageContext.request.contextPath}/assets/crud/img/mais-detalhes.png" alt="Ver/Editar"></button> </td>
                        <td> <button type="button" class="detalhes acaoModal" data-acao="abrir" data-modal="delete" data-pk="<%= setor.getId() %>" data-caminho='{"base":"${pageContext.request.contextPath}","tabela":"setores-crud"}'><img src="${pageContext.request.contextPath}/assets/crud/img/deletar-kronos.png" alt="Excluir"></button> </td>
                        <td><%= setor.getId() %></td>
                        <td><%= setor.getNome() %></td>
                        <td><%= setor.getIdEmpresa() %></td>
                        <td><%= setor.getQntFuncionarios() %></td>
                        <td><%= setor.getTurnos() %></td>
                        <td><%= setor.getDescricao() %></td>
                    </tr>
                    <%
                        }
                    } else {
                    %>
                    <tr> <td colspan="8">Nenhum setor encontrado.</td> </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
        </main>
    </div>
</body>
</html>