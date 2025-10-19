<%@ page import="com.example.Model.Administracao" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <!-------------------- Fontes -------------------->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link
            href="https://fonts.googleapis.com/css2?family=Cinzel:wght@400;700;900&family=Montserrat:wght@300;400;500;700;900&family=Crete+Round:wght@400;700&display=swap"
            rel="stylesheet">

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/crud/style/dados.css">
    <title>CRUD - Kronos</title>
</head>

<body>
<div class="meuPlaceholder"></div>
<div class="meuPlaceholder"></div>

<header>
    <h1>KRONOS</h1>
    <nav>
        <ul>
            <li>
                <a href="${pageContext.request.contextPath}/admin-crud" class="ativo">
                    <img src="${pageContext.request.contextPath}/assets/crud/img/img-crud-administrador.png" alt="">Administrador
                </a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/empresas-crud">
                    <img src="${pageContext.request.contextPath}/assets/crud/img/img-crud-empresas.png" alt="">Empresas
                </a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/planos-crud">
                    <img src="${pageContext.request.contextPath}/assets/crud/img/img-crud-planos.png" alt="">Planos
                </a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/habilidades-crud">
                    <img src="${pageContext.request.contextPath}/assets/crud/img/img-crud-habilidades.png" alt="">Habilidades
                </a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/setores-crud">
                    <img src="${pageContext.request.contextPath}/assets/crud/img/img-crud-setores.png" alt="">Setores
                </a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/usuarios-crud">
                    <img src="${pageContext.request.contextPath}/assets/crud/img/img-crud-usuario.png" alt="">Usuário
                </a>
            </li>
        </ul>
    </nav>
</header>

<div class="conteudoPrincipal">
    <div class="procurarCadastrar">
        <form class="pesquisa">
            <input type="search" placeholder="Pesquisar" id="pesquisa" name = "pesquisa" nameclass="buscar">

            <details class="filtros">
                <summary>Filtros</summary>
                <div class="conteudoFiltros">
                    <label class="opcaoFiltro">
                        <input type="radio" name="ordem" value="crescente">Crescente
                    </label>
                    <label class="opcaoFiltro">
                        <input type="radio" name="ordem" value="decrescente">Decrescente
                    </label>
                    <button type="reset">Limpar filtros</button>
                </div>
            </details>
        </form>

        <section>
            <button type="button" onclick="" class="cadastrar">Cadastrar</button>
        </section>
    </div>

    <div class="tabelaScroll">
        <table class="tabelaAdministrador">
            <thead>
            <tr>
                <th>ID</th>
                <th class="nome">Nome</th>
                <th>E-mail</th>
                <th>Cod_Acesso</th>
                <th>Senha</th>
                <th>Ver+</th>
            </tr>
            </thead>
            <tbody>
            <%
                List<Administracao> listaAdmins = (List<Administracao>) request.getAttribute("listaAdmins");
                if (listaAdmins != null && !listaAdmins.isEmpty()) {
                    for (Administracao admin : listaAdmins) {
            %>
            <tr>
                <td><%= admin.getId() %></td>
                <td><%= admin.getNome() %></td>
                <td><%= admin.getEmail() %></td>
                <td>Vinte</td> <!-- Se tiver campo específico, substitua -->
                <td>*********</td>
                <td>
                    <a href="#" target="_blank" class="detalhes">
                        <img src="${pageContext.request.contextPath}/assets/crud/img/mais-detalhes.png" alt="">
                    </a>
                </td>
            </tr>
            <%
                }
            } else {
            %>
            <tr>
                <td colspan="6">Nenhum administrador encontrado.</td>
            </tr>
            <% } %>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>