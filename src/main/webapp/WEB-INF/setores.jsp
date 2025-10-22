
<%@ page import="com.example.Model.Setor" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String acaoURL = request.getParameter("acao");
    String idAdquirido = request.getParameter("pk");
    String abrirModal = (String) request.getAttribute("abrirModal");
    String erro = (String) request.getAttribute("erro");
    Setor setorModal = (Setor) request.getAttribute("setorModal");
    String nome_previo = (String) request.getAttribute("nome_previo");
    String turnos_previo = (String) request.getAttribute("turnos_previo");
    String qtdFuncionarios_previo = (String) request.getAttribute("qtdFuncionarios_previo");
    String descricao_previo = (String) request.getAttribute("descricao_previo");
    String idEmpresa_previo = (String) request.getAttribute("idEmpresa_previo");

    if (abrirModal == null) abrirModal = acaoURL;
    if (abrirModal == null) abrirModal = "";

    boolean updateAberto = "update".equals(abrirModal);
    boolean deleteAberto = "delete".equals(abrirModal);
    boolean createAberto = "create".equals(abrirModal);
%>

<%!
    // MÉTODOS AUXILIARES (tratar null)
    String stringValue(String s) { return (s != null) ? s : ""; }
    String intValue(String s) { return (s != null) ? s : ""; }
    String intValue(Integer i) { return (i != null) ? i.toString() : ""; }
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <%-- Links <head> --%>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Cinzel:wght@400;700;900&family=Montserrat:wght@300;400;500;700;900&family=Crete+Round:wght@400;700&display=swap" rel="stylesheet">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <script src="<%= request.getContextPath() %>/assets/crud/script/script.js" defer></script>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/crud/style/dados.css">
    <title>CRUD - Setores</title>
</head>

<body>
<div class="meuPlaceholder"></div>
<header>
    <h1>KRONOS</h1>
    <nav>
        <ul>
            <%-- Links Menu --%>
            <li><a href="<%= request.getContextPath() %>/admin-crud"><img src="<%= request.getContextPath() %>/assets/crud/img/img-crud-administrador.png" alt="">Administrador</a></li>
            <li><a href="<%= request.getContextPath() %>/empresas-crud"><img src="<%= request.getContextPath() %>/assets/crud/img/img-crud-empresas.png" alt="">Empresas</a></li>
            <li><a href="<%= request.getContextPath() %>/planos-crud"><img src="<%= request.getContextPath() %>/assets/crud/img/img-crud-planos.png" alt="">Planos</a></li>
            <li><a href="<%= request.getContextPath() %>/habilidades-crud"><img src="<%= request.getContextPath() %>/assets/crud/img/img-crud-habilidades.png" alt="">Habilidades</a></li>
            <li><a href="<%= request.getContextPath() %>/setores-crud" class="ativo"><img src="<%= request.getContextPath() %>/assets/crud/img/img-crud-setores.png" alt="">Setores</a></li>
            <li><a href="<%= request.getContextPath() %>/usuarios-crud"><img src="<%= request.getContextPath() %>/assets/crud/img/img-crud-usuario.png" alt="">Usuário</a></li>
        </ul>
    </nav>
</header>


<div class="conteudoPrincipal">
    <div class="procurarCadastrar">
        <%-- Form Pesquisa --%>
        <form class="pesquisa">
            <input type="search" placeholder="Pesquisar" name="pesquisa" class="buscar">
            <details class="filtros">
                <summary>Filtros</summary>
                <div class="conteudoFiltros">
                    <label class="opcaoFiltro"><input type="radio" name="ordem" value="crescente">Crescente</label>
                    <label class="opcaoFiltro"><input type="radio" name="ordem" value="decrescente">Decrescente</label>
                    <button type="reset">Limpar filtros</button>
                </div>
            </details>
        </form>

        <section class="create">
            <%-- 3. DIALOG CREATE: data-abrir adicionado --%>
            <dialog id="create" data-abrir="<%=createAberto%>">
                <h2>Cadastrar setor</h2>
                <%-- 4. FORM CREATE: action corrigido --%>
                <form action="<%= request.getContextPath() %>/setor-create" method="post">
                    <div class="campos">
                        <div>
                            <div class="campo">
                                <label for="nomeCreate">Nome</label>
                                <%-- 5. CAMPOS CREATE: value adicionado (repopular) --%>
                                <input type="text" name="nome" id="nomeCreate" autocomplete="off" required
                                       value="<%= stringValue(nome_previo) %>">
                            </div>
                            <div class="campo">
                                <label for="turnosCreate">Turnos</label>
                                <input type="text" name="turnos" id="turnosCreate" autocomplete="off" required
                                       value="<%= stringValue(turnos_previo) %>">
                            </div>
                            <div class="campo">
                                <label for="qtdFuncionariosCreate">Número de funcionários</label>
                                <input type="number" name="qtdFuncionarios" id="qtdFuncionariosCreate" min="0" required
                                       value="<%= intValue(qtdFuncionarios_previo) %>">
                            </div>
                        </div>
                        <div>
                            <div class="campo">
                                <label for="descricaoCreate">Descrição</label>
                                <textarea type="text" name="descricao" id="descricaoCreate" required><%= stringValue(descricao_previo) %></textarea>
                            </div>
                            <div class="campo">
                                <label for="idEmpresaCreate">ID da Empresa</label>
                                <input type="number" name="idEmpresa" id="idEmpresaCreate" min="1" required
                                       value="<%= intValue(idEmpresa_previo) %>">
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

        <section class="update">
            <%-- DIALOG UPDATE: data-abrir, action, pk, values adicionados --%>
            <dialog id="update" data-abrir="<%=updateAberto%>">
                <h2>Editar setor</h2>
                <form action="<%= request.getContextPath() %>/setor-update" method="post">
                    <input type="hidden" name="pk" value="<%= (setorModal != null) ? setorModal.getId() : "" %>">
                    <div class="campos">
                        <div>
                            <div class="campo">
                                <label for="nomeUpdate">Nome</label>
                                <input type="text" name="nome" id="nomeUpdate" autocomplete="off" required
                                       value="<%= (setorModal != null) ? stringValue(setorModal.getNome()) : "" %>">
                            </div>
                            <div class="campo">
                                <label for="turnosUpdate">Turnos</label>
                                <input type="text" name="turnos" id="turnosUpdate" autocomplete="off" required
                                       value="<%= (setorModal != null) ? stringValue(setorModal.getTurnos()) : "" %>">
                            </div>
                            <div class="campo">
                                <label for="qtdFuncionariosUpdate">Número de funcionários</label>
                                <input type="number" name="qtdFuncionarios" id="qtdFuncionariosUpdate" min="0" required
                                       value="<%= (setorModal != null) ? intValue(Integer.valueOf(setorModal.getQntFuncionarios())) : "" %>">
                            </div>
                        </div>
                        <div>
                            <div class="campo">
                                <label for="descricaoUpdate">Descrição</label>
                                <textarea type="text" name="descricao" id="descricaoUpdate" required><%= (setorModal != null) ? stringValue(setorModal.getDescricao()) : "" %></textarea>
                            </div>
                            <div class="campo">
                                <label for="idEmpresaUpdate">ID da Empresa</label>
                                <input type="number" name="idEmpresa" id="idEmpresaUpdate" min="1" required
                                       value="<%= (setorModal != null) ? intValue(Integer.valueOf(setorModal.getIdEmpresa())) : "" %>">
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

        <section class="delete">
            <%-- DIALOG DELETE: data-abrir, action, pk, values adicionados --%>
            <dialog id="delete" data-abrir="<%=deleteAberto%>">
                <h2>Excluir setor</h2>
                <form action="<%= request.getContextPath() %>/setor-delete" method="post">
                    <input type="hidden" name="pk" value="<%= (setorModal != null) ? setorModal.getId() : "" %>">
                    <div class="campos">
                        <div>
                            <div class="campo">
                                <label for="nomeDelete">Nome</label>
                                <input type="text" name="nome" id="nomeDelete" autocomplete="off" disabled
                                       value="<%= (setorModal != null) ? stringValue(setorModal.getNome()) : "" %>">
                            </div>
                            <div class="campo">
                                <label for="turnosDelete">Turnos</label>
                                <input type="text" name="turnos" id="turnosDelete" autocomplete="off" disabled
                                       value="<%= (setorModal != null) ? stringValue(setorModal.getTurnos()) : "" %>">
                            </div>
                            <div class="campo">
                                <label for="qtdFuncionariosDelete">Número de funcionários</label>
                                <input type="number" name="qtdFuncionarios" id="qtdFuncionariosDelete" min="0" disabled
                                       value="<%= (setorModal != null) ? intValue(Integer.valueOf(setorModal.getQntFuncionarios())) : "" %>">
                            </div>
                        </div>
                        <div>
                            <div class="campo">
                                <label for="descricaoDelete">Descrição</label>
                                <textarea type="text" name="descricao" id="descricaoDelete" disabled><%= (setorModal != null) ? stringValue(setorModal.getDescricao()) : "" %></textarea>
                            </div>
                            <div class="campo">
                                <label for="idEmpresaDelete">ID da Empresa</label>
                                <input type="number" name="idEmpresa" id="idEmpresaDelete" min="1" disabled
                                       value="<%= (setorModal != null) ? intValue(Integer.valueOf(setorModal.getIdEmpresa())) : "" %>">
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

    <div class="tabelaScroll">
        <%-- Tabela READ: actions corrigidos, values usam helpers --%>
        <table class="tabelaSetores"> <%-- Ajuste a classe CSS se necessário --%>
            <thead>
            <tr>
                <th>Ver</th>
                <th>ID</th>
                <th>Nome</th>
                <th>Quantidade de Funcionários</th>
                <th>Turnos</th>
                <th>Descrição</th>
                <th></th> <%-- Coluna Delete --%>
            </tr>
            </thead>
            <tbody>
            <%
                List<Setor> listaSetores = (List<Setor>) request.getAttribute("listaSetores");
                if (listaSetores != null && !listaSetores.isEmpty()) {
                    for (Setor setor : listaSetores) {
            %>
            <tr>
                <td>
                    <%-- Botão Editar: action corrigida --%>
                    <form method="get" action="<%= request.getContextPath() %>/setor-update">
                        <input type="hidden" name="acao" value="update">
                        <input type="hidden" name="pk" value="<%= setor.getId() %>">
                        <button type="submit" class="detalhes"><img src="<%= request.getContextPath() %>/assets/crud/img/mais-detalhes.png" alt=""></button>
                    </form>
                </td>
                <td><%= setor.getId() %></td>
                <td><%= stringValue(setor.getNome()) %></td>
                <td><%= setor.getQntFuncionarios() %></td>
                <td><%= stringValue(setor.getTurnos()) %></td>
                <td><%= stringValue(setor.getDescricao()) %></td>
                <td>
                    <%-- Botão Deletar: action corrigida --%>
                    <form method="get" action="<%= request.getContextPath() %>/setor-delete">
                        <input type="hidden" name="acao" value="delete">
                        <input type="hidden" name="pk" value="<%= setor.getId() %>">
                        <button type="submit" class="detalhes"><img src="<%= request.getContextPath() %>/assets/crud/img/deletar-kronos.png" alt=""></button>
                    </form>
                </td>
            </tr>
            <%
                }
            } else {
            %>
            <tr>
                <td colspan="7">Nenhum setor encontrado.</td> <%-- Ajuste o colspan --%>
            </tr>
            <% } %>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>