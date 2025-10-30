<%@ page import="com.example.Model.Usuario" %>
<%@ page import="com.example.Model.Habilidades" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="java.util.Arrays" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%-- Bloco de Processamento de Atributos (para erros e repopulação) --%>
<%
    // Erros e modal atual
    String erro = (String) request.getAttribute("erro");
    String modalAberto = (String) request.getAttribute("abrirModal");
    Usuario usuarioModal = (Usuario) request.getAttribute("usuarioModal");

    // ==== CREATE ====
    // Variáveis para repopular campos após erro
    String createNome = "";
    String createCpf = "";
    String createGenero = "";
    String createStatus = "";
    String createIdSetor = "";
    String createIdSupervisor = "";
    String createCargo = "";
    Set<String> createHabilidadesSet = new HashSet<>();

    if ("create".equals(modalAberto)) {
        createNome = request.getAttribute("nome_previo") != null ? (String)request.getAttribute("nome_previo") : "";
        createCpf = request.getAttribute("cpf_previo") != null ? (String)request.getAttribute("cpf_previo") : "";
        createGenero = request.getAttribute("genero_previo") != null ? (String)request.getAttribute("genero_previo") : "";
        createStatus = request.getAttribute("status_previo") != null ? (String)request.getAttribute("status_previo") : "";
        createIdSetor = request.getAttribute("idSetor_previo") != null ? (String)request.getAttribute("idSetor_previo") : "";
        createIdSupervisor = request.getAttribute("idSupervisor_previo") != null ? (String)request.getAttribute("idSupervisor_previo") : "";
        createCargo = request.getAttribute("cargo_previo") != null ? (String)request.getAttribute("cargo_previo") : "";
        String[] createHabilidadesIds = (String[]) request.getAttribute("habilidades_previas_ids");
        if (createHabilidadesIds != null) {
            createHabilidadesSet.addAll(Arrays.asList(createHabilidadesIds));
        }
    }

    // ==== UPDATE ====
    // Variáveis para o modal UPDATE (GET ou erro de POST)
    String updateID = "";
    String updateNome = "";
    String updateCpf = "";
    String updateGenero = "";
    String updateStatus = "";
    String updateIdSetor = "";
    String updateIdSupervisor = "";
    String updateCargo = "";
    Set<Integer> updateHabilidadesSet = new HashSet<>();

    if (usuarioModal != null) {
        updateID = String.valueOf(usuarioModal.getId());
        updateNome = usuarioModal.getNome();
        updateCpf = usuarioModal.getCpf();
        updateGenero = usuarioModal.getGenero() != null ? usuarioModal.getGenero().toString() : "";
        updateStatus = usuarioModal.getStatus();
        updateIdSetor = String.valueOf(usuarioModal.getIdSetor());
        updateIdSupervisor = String.valueOf(usuarioModal.getIdSupervisor());
        updateCargo = usuarioModal.getCargo();

        // Pega habilidades do servlet (para update/delete)
        Object habilidadesPreviasObj = request.getAttribute("habilidades_previas_ids");
        if (("update".equals(modalAberto) || "delete".equals(modalAberto)) && habilidadesPreviasObj instanceof List) {
            try {
                updateHabilidadesSet.addAll((List<Integer>) habilidadesPreviasObj);
            } catch (Exception e) {
                System.err.println("JSP: Erro ao converter 'habilidades_previas_ids' para List<Integer>: " + e.getMessage());
            }
        }
    }

    if ("update".equals(modalAberto) && request.getAttribute("nome_previo") != null) {
        // ID não muda
        updateNome = (String) request.getAttribute("nome_previo");
        updateCpf = (String) request.getAttribute("cpf_previo");
        updateGenero = (String) request.getAttribute("genero_previo");
        updateStatus = (String) request.getAttribute("status_previo");
        updateIdSetor = (String) request.getAttribute("idSetor_previo");
        updateIdSupervisor = (String) request.getAttribute("idSupervisor_previo");
        updateCargo = (String) request.getAttribute("cargo_previo");
    }

    // ==== DELETE ====
    // Variáveis para o modal DELETE (para GET)
    String deleteID = "";
    String deleteNome = "";
    String deleteCpf = "";
    String deleteGenero = "";
    String deleteStatus = "";
    String deleteIdSetor = "";
    String deleteIdSupervisor = "";
    String deleteCargo = "";

    if (usuarioModal != null && "delete".equals(modalAberto)) {
        deleteID = String.valueOf(usuarioModal.getId());
        deleteNome = usuarioModal.getNome();
        deleteCpf = usuarioModal.getCpf();
        deleteGenero = usuarioModal.getGenero() != null ? usuarioModal.getGenero().toString() : "";
        deleteStatus = usuarioModal.getStatus();
        deleteIdSetor = String.valueOf(usuarioModal.getIdSetor());
        deleteIdSupervisor = String.valueOf(usuarioModal.getIdSupervisor());
        deleteCargo = usuarioModal.getCargo();
    }

    // ==== LISTA DE HABILIDADES (para o CRUD principal) ====
    List<Habilidades> todasAsHabilidades = (List<Habilidades>) request.getAttribute("todasAsHabilidades");
    if (todasAsHabilidades == null) {
        todasAsHabilidades = new ArrayList<>();
    }
%>

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
    <title>Usuários - Kronos CRUD</title>
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
            <li><a href="${pageContext.request.contextPath}/setores-crud"><img src="${pageContext.request.contextPath}/assets/crud/img/img-crud-setores.png" alt="">Setores</a></li>
            <li><a href="${pageContext.request.contextPath}/usuarios-crud" class="ativo"><img src="${pageContext.request.contextPath}/assets/crud/img/img-crud-usuario.png" alt="">Usuário</a></li>
        </ul>
    </nav>
</header>

<div class="conteudoPrincipal">
    <%-- Bloco de Exibição de Erro (Geral e Parcial) --%>
<%
            if (erro != null && !erro.isEmpty()) {
        %>
        <div class="mensagem-erro">
            <strong>Erro:</strong> <%= erro %>
        </div>
            <%
            }
        %>
            <%
            String erroParcial = (String) session.getAttribute("erro_parcial");
            if (erroParcial != null) {
        %>
        <div class="mensagem-erro">
            <strong>Aviso:</strong> <%= erroParcial %>
        </div>
            <%
                session.removeAttribute("erro_parcial");
            }
        %>

    <div class="procurarCadastrar">

        <%-- ==== FILTRO DE PESQUISA ==== --%>
        <form class="pesquisa" method="get" action="${pageContext.request.contextPath}/usuarios-crud">
            <input type="search" placeholder="Pesquisar" id="pesquisa" name="pesquisa" class="buscar" value="<%= request.getParameter("pesquisa") != null ? request.getParameter("pesquisa") : "" %>">
            <details class="filtros">
                <summary>Filtros</summary>
                <div class="conteudoFiltros">
                    <label class="opcaoFiltro"> <input type="radio" name="ordem" value="crescente" <%= "crescente".equals(request.getParameter("ordem")) ? "checked" : "" %>> Crescente </label>
                    <label class="opcaoFiltro"> <input type="radio" name="ordem" value="decrescente" <%= "decrescente".equals(request.getParameter("ordem")) ? "checked" : "" %>> Decrescente </label>
                    <button type="reset" id="botaoLimparFiltro" data-caminho='{"base":"${pageContext.request.contextPath}","tabela":"usuarios-crud"}'>Limpar filtros</button>
                    <button type="submit">Aplicar</button>
                </div>
            </details>
        </form>

            <%-- ==== CREATE ==== --%>
            <section class="create">
            <dialog id="create">
                <h2>Cadastrar usuário</h2>
                <form action="${pageContext.request.contextPath}/usuario-create" id="formCreate" method="post">
                    <div class="campos">
                        <div>
                            <div class="campo"> <label for="nomeCreate">Nome</label> <input type="text" name="nome" id="nomeCreate" autocomplete="off" required value="<%= createNome %>"> </div>
                            <div class="campo"> <label for="cpfCreate">CPF</label> <input type="text" name="cpf" id="cpfCreate" autocomplete="off" required value="<%= createCpf %>"> </div>
                            <div class="campo">
                                <label for="senhaCreate">Senha</label>
                                <input type="password" name="senha" id="senhaCreate" autocomplete="new-password" required>
                                <span id="senhaErro" class="erro-message"></span>
                            </div>
                            <div class="campoLado">
                                <div class="campo">
                                    <label for="generoCreate">Gênero</label>
                                    <select name="genero" id="generoCreate" required>
                                        <option value="" disabled <%= createGenero.isEmpty() ? "selected" : "" %>>Selecionar</option>
                                        <option value="M" <%= "M".equals(createGenero) ? "selected" : "" %>>Masculino</option>
                                        <option value="F" <%= "F".equals(createGenero) ? "selected" : "" %>>Feminino</option>
                                        <option value="O" <%= "O".equals(createGenero) ? "selected" : "" %>>Outro</option>
                                        <option value="N" <%= "N".equals(createGenero) ? "selected" : "" %>>Não Informar</option>
                                    </select>
                                </div>
                                <div class="campo">
                                    <label for="statusCreate">Status</label>
                                    <select name="status" id="statusCreate" required>
                                        <option value="" disabled <%= createStatus.isEmpty() ? "selected" : "" %>>Selecionar</option>
                                        <option value="Ativo" <%= "Ativo".equals(createStatus) ? "selected" : "" %>>Ativo</option>
                                        <option value="Inativo" <%= "Inativo".equals(createStatus) ? "selected" : "" %>>Inativo</option>
                                        <option value="Férias" <%= "Férias".equals(createStatus) ? "selected" : "" %>>Férias</option>
                                        <option value="Desligado" <%= "Desligado".equals(createStatus) ? "selected" : "" %>>Desligado</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div>
                            <div class="campo"> <label for="cargoCreate">Cargo</label> <input type="text" name="cargo" id="cargoCreate" required value="<%= createCargo %>"> </div>
                            <div class="campo"> <label for="idSetorCreate">ID do Setor</label> <input type="number" name="idSetor" id="idSetorCreate" min="1" required value="<%= createIdSetor %>"> </div>
                            <div class="campo"> <label for="idSupervisorCreate">ID do Supervisor</label> <input type="number" name="idSupervisor" id="idSupervisorCreate" min="0" required value="<%= createIdSupervisor %>"> </div> <%-- Ajustado min="0" se aplicável --%>
                            <fieldset class="campo">
                                <legend>Habilidades</legend>
                                <div id="listaHabilidadesCreate">
                                    <% for (Habilidades hab : todasAsHabilidades) { %>
                                    <label style="display: block; margin: 2px;">
                                        <input type="checkbox"
                                               name="habilidadeId"
                                               value="<%= hab.getId() %>"
                                               class="habilidade-create-cb"
                                            <%= createHabilidadesSet.contains(String.valueOf(hab.getId())) ? "checked" : "" %>
                                        >
                                        <%= hab.getNome() %>
                                    </label>
                                    <% } %>
                                    <% if (todasAsHabilidades.isEmpty()) { %>
                                    <p>Nenhuma habilidade cadastrada no sistema.</p>
                                    <% } %>
                                </div>
                            </fieldset>
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
                <h2>Editar usuário</h2>
                <form action="${pageContext.request.contextPath}/usuario-update" method="post">
                    <div class="idAtual"> <label for="idUpdate">ID:</label> <input type="number" name="id" id="idUpdate" readonly value="<%= updateID %>"> </div>
                    <div class="campos">
                        <div>
                            <div class="campo"> <label for="nomeUpdate">Nome</label> <input type="text" name="nome" id="nomeUpdate" autocomplete="off" required value="<%= updateNome %>"> </div>
                            <div class="campo"> <label for="cpfUpdate">CPF</label> <input type="text" name="cpf" id="cpfUpdate" autocomplete="off" required value="<%= updateCpf %>"> </div>
                            <div class="campo">
                                <label for="senhaUpdate">Senha</label>
                                <%-- Senha opcional no update --%>
                                <input type="password" name="senha" id="senhaUpdate" autocomplete="new-password" placeholder="Deixe em branco para manter">
                            </div>
                            <div class="campoLado">
                                <div class="campo">
                                    <label for="generoUpdate">Gênero</label>
                                    <select name="genero" id="generoUpdate" required>
                                        <option value="" disabled <%= updateGenero.isEmpty() ? "selected" : "" %>>Selecionar</option>
                                        <option value="M" <%= "M".equals(updateGenero) ? "selected" : "" %>>Masculino</option>
                                        <option value="F" <%= "F".equals(updateGenero) ? "selected" : "" %>>Feminino</option>
                                        <option value="O" <%= "O".equals(updateGenero) ? "selected" : "" %>>Outro</option>
                                        <option value="N" <%= "N".equals(updateGenero) ? "selected" : "" %>>Não Informar</option>
                                    </select>
                                </div>
                                <div class="campo">
                                    <label for="statusUpdate">Status</label>
                                    <select name="status" id="statusUpdate" required>
                                        <option value="" disabled <%= updateStatus.isEmpty() ? "selected" : "" %>>Selecionar</option>
                                        <option value="Ativo" <%= "Ativo".equals(updateStatus) ? "selected" : "" %>>Ativo</option>
                                        <option value="Inativo" <%= "Inativo".equals(updateStatus) ? "selected" : "" %>>Inativo</option>
                                        <option value="Férias" <%= "Férias".equals(updateStatus) ? "selected" : "" %>>Férias</option>
                                        <option value="Desligado" <%= "Desligado".equals(updateStatus) ? "selected" : "" %>>Desligado</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div>
                            <div class="campo"> <label for="cargoUpdate">Cargo</label> <input type="text" name="cargo" id="cargoUpdate" required value="<%= updateCargo %>"> </div>
                            <div class="campo"> <label for="idSetorUpdate">ID do Setor</label> <input type="number" name="idSetor" id="idSetorUpdate" min="1" required value="<%= updateIdSetor %>"> </div>
                            <div class="campo"> <label for="idSupervisorUpdate">ID do Supervisor</label> <input type="number" name="idSupervisor" id="idSupervisorUpdate" min="0" required value="<%= updateIdSupervisor %>"> </div>
                            <fieldset class="campo">
                                <legend>Habilidades</legend>
                                <div id="listaHabilidadesUpdate">
                                    <% for (Habilidades hab : todasAsHabilidades) { %>
                                    <label style="display: block; margin: 2px;">
                                        <input type="checkbox"
                                               name="habilidadeId"
                                               value="<%= hab.getId() %>"
                                               class="habilidade-update-cb">
                                               <%= (updateHabilidadesSet.contains(hab.getId())) ? "checked" : "" %>
                                        <%= hab.getNome() %>
                                    </label>
                                    <% } %>
                                    <% if (todasAsHabilidades.isEmpty()) { %>
                                    <p>Nenhuma habilidade cadastrada no sistema.</p>
                                    <% } %>
                                </div>
                            </fieldset>
                        </div>
                    </div>
                    <menu> <button type="button" class="cancelar acaoModal" data-acao="fechar" data-modal="update">Cancelar</button> <button type="submit" class="confirmar">Confirmar alterações</button> </menu>
                </form>
            </dialog>
        </section>

            <%-- ==== DELETE ==== --%>
            <section class="delete">
            <dialog id="delete">
                <h2>Excluir usuário</h2>
                <form action="${pageContext.request.contextPath}/usuario-delete" method="post">
                    <div class="idAtual"> <label for="idDelete">ID:</label> <input type="number" name="id" id="idDelete" readonly value="<%= deleteID %>"> </div>
                    <div class="campos">
                        <div>
                            <div class="campo"> <label for="nomeDelete">Nome</label> <input type="text" name="nome" id="nomeDelete" disabled value="<%= deleteNome %>"> </div>
                            <div class="campo"> <label for="cpfDelete">CPF</label> <input type="text" name="cpf" id="cpfDelete" disabled value="<%= deleteCpf %>"> </div>
                            <div class="campo"> <label for="senhaDelete">Senha</label> <input type="password" name="senha" id="senhaDelete" disabled value="********"> </div>
                            <div class="campoLado">
                                <div class="campo">
                                    <label for="generoDelete">Gênero</label>
                                    <select name="genero" id="generoDelete" disabled>
                                        <option value="" disabled <%= deleteGenero.isEmpty() ? "selected" : "" %>>Selecionar</option>
                                        <option value="M" <%= "M".equals(deleteGenero) ? "selected" : "" %>>Masculino</option>
                                        <option value="F" <%= "F".equals(deleteGenero) ? "selected" : "" %>>Feminino</option>
                                        <option value="O" <%= "O".equals(deleteGenero) ? "selected" : "" %>>Outro</option>
                                        <option value="N" <%= "N".equals(deleteGenero) ? "selected" : "" %>>Não Informar</option>
                                    </select>
                                </div>
                                <div class="campo">
                                    <label for="statusDelete">Status</label>
                                    <select name="status" id="statusDelete" disabled>
                                        <option value="" disabled <%= deleteStatus.isEmpty() ? "selected" : "" %>>Selecionar</option>
                                        <option value="Ativo" <%= "Ativo".equals(deleteStatus) ? "selected" : "" %>>Ativo</option>
                                        <option value="Inativo" <%= "Inativo".equals(deleteStatus) ? "selected" : "" %>>Inativo</option>
                                        <option value="Férias" <%= "Férias".equals(deleteStatus) ? "selected" : "" %>>Férias</option>
                                        <option value="Desligado" <%= "Desligado".equals(deleteStatus) ? "selected" : "" %>>Desligado</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div>
                            <div class="campo"> <label for="cargoDelete">Cargo</label> <input type="text" name="cargo" id="cargoDelete" disabled value="<%= deleteCargo %>"> </div>
                            <div class="campo"> <label for="idSetorDelete">ID do Setor</label> <input type="number" name="idSetor" id="idSetorDelete" min="1" disabled value="<%= deleteIdSetor %>"> </div>
                            <div class="campo"> <label for="idSupervisorDelete">ID do Supervisor</label> <input type="number" name="idSupervisor" id="idSupervisorDelete" min="0" disabled value="<%= deleteIdSupervisor %>"> </div> <%-- Ajustado min="0" se aplicável --%>
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
            <table class="tabelaUsuarios">
                <thead>
                <tr>
                    <th>Ver</th>
                    <th>Excluir</th>
                    <th>ID</th>
                    <th>Nome</th>
                    <th>CPF</th>
                    <th>Senha</th>
                    <th>Gênero</th>
                    <th>Cargo</th>
                    <th>Status</th>
                    <th>Setor ID</th>
                    <th>Supervisor ID</th>
                </tr>
                </thead>
                <tbody>
                <%
                    List<Usuario> listaUsuarios = (List<Usuario>) request.getAttribute("listaUsuarios");
                    if (listaUsuarios != null && !listaUsuarios.isEmpty()) {
                        for (Usuario u : listaUsuarios) {
                %>
                <tr>
                    <td> <button type="button" class="detalhes acaoModal" data-acao="abrir" data-modal="update" data-pk="<%= u.getId() %>" data-caminho='{"base":"${pageContext.request.contextPath}","tabela":"usuarios-crud"}'><img src="${pageContext.request.contextPath}/assets/crud/img/mais-detalhes.png" alt=""></button> </td>
                    <td> <button type="button" class="detalhes acaoModal" data-acao="abrir" data-modal="delete" data-pk="<%= u.getId() %>" data-caminho='{"base":"${pageContext.request.contextPath}","tabela":"usuarios-crud"}'><img src="${pageContext.request.contextPath}/assets/crud/img/deletar-kronos.png" alt=""></button> </td>
                    <td><%= u.getId() %></td>
                    <td><%= u.getNome() %></td>
                    <td><%= u.getCpf() %></td>
                    <td><%= u.getSenha() %></td>
                    <td><%= u.getGenero() %></td>
                    <td><%= u.getCargo() %></td>
                    <td><%= u.getStatus() %></td>
                    <td><%= u.getIdSetor() %></td>
                    <td><%= u.getIdSupervisor() %></td>
                    <%-- Para exibir nomes de Setor/Supervisor, precisaria de JOINs no DAO e carregar os objetos --%>
                </tr>
                <%
                    }
                } else {
                %>
                <tr> <td colspan="11">Nenhum usuário encontrado.</td> </tr>
                <%
                    }
                %>
                </tbody>
            </table>
        </div>
    </main>
</body>
</html>