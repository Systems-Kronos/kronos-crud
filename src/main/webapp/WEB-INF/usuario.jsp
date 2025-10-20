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
    <!-- Caminho absoluto para CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/crud/style/dados.css">
    <title>CRUD - Kronos</title>
</head>

<body>
<div class="meuPlaceholder"></div>
<header>
    <h1>KRONOS</h1>
    <nav>
        <ul>
            <li><a href="${pageContext.request.contextPath}/admin-crud">Administrador</a></li>
            <li><a href="${pageContext.request.contextPath}/empresas-crud">Empresas</a></li>
            <li><a href="${pageContext.request.contextPath}/planos-crud">Planos</a></li>
            <li><a href="${pageContext.request.contextPath}/habilidades-crud">Habilidades</a></li>
            <li><a href="${pageContext.request.contextPath}/setores-crud" class="ativo">Setores</a></li>
            <li><a href="${pageContext.request.contextPath}/usuarios-crud">Usuário</a></li>
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

        <section>
            <button type="button" onclick="window.location='${pageContext.request.contextPath}/usuarios-cadastro'" class="cadastrar">Cadastrar</button>
        </section>
    </div>

    <main>
        <table class="tabelaHabilidades">
            <thead>
            <tr>
                <th class="nome">Nome</th>
                <th>Gênero</th>
                <th>CPF</th>
                <th>Telefone</th>
                <th>Ver+</th>
            </tr>
            </thead>
            <tbody>
            <%
                LinkedList<Usuario> usuarios = (LinkedList<com.example.Model.Usuario>) request.getAttribute("usuarios");
                if (usuarios != null && !usuarios.isEmpty()) {
                    for (com.example.Model.Usuario u : usuarios) {
            %>
            <tr>
                <td><%= u.getNome() %></td>
                <td><%= u.getGenero() %></td>
                <td><%= u.getCpf() %></td>
                <td><!-- Adicione o telefone se houver no model --></td>
                <td>
                    <a href="<%= request.getContextPath() + "/detalhes-usuario?id=" + u.getId() %>" class="detalhes">
                        <!-- Caminho absoluto para imagem -->
                        <img src="${pageContext.request.contextPath}/assets/crud/img/mais-detalhes.png" alt="Detalhes">
                    </a>
                </td>
            </tr>
            <%
                }
            } else {
            %>
            <tr>
                <td colspan="5" style="text-align:center;">Nenhum usuário encontrado.</td>
            </tr>
            <%
                }
            %>
            </tbody>
        </table>
    </main>
</div>
</body>
</html>
