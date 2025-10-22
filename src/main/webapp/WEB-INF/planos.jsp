<%@ page import="com.example.Model.Plano" %>
<%@ page import="java.util.List" %>
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
                <li><a href="${pageContext.request.contextPath}/planos-crud" class="ativo"><img src="${pageContext.request.contextPath}/assets/crud/img/img-crud-planos.png" alt="">Planos</a></li>
                <li><a href="${pageContext.request.contextPath}/habilidades-crud"><img src="${pageContext.request.contextPath}/assets/crud/img/img-crud-habilidades.png" alt="">Habilidades</a></li>
                <li><a href="${pageContext.request.contextPath}/setores-crud"><img src="${pageContext.request.contextPath}/assets/crud/img/img-crud-setores.png" alt="">Setores</a></li>
                <li><a href="${pageContext.request.contextPath}/usuarios-crud"><img src="${pageContext.request.contextPath}/assets/crud/img/img-crud-usuario.png" alt="">Usuário</a></li>
            </ul>
        </nav>
    </header>

    <div class="conteudoPrincipal">
        <div class="procurarCadastrar">
            <form class="pesquisa">
                <input type="search" placeholder="Pesquisar" id="pesquisa" name="pesquisa" class="buscar">

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

            <!-- CREATE -->

            <section class="create">
                <dialog id="create">
                    <h2>Cadastrar plano</h2>
                    <form action="${pageContext.request.contextPath}/create-plano" method="post">
                        <div class="campos">
                            <div>
                                <div class="campo">
                                    <label for="nomeCreate">Nome</label>
                                    <input type="text" name="nome" id="nomeCreate" autocomplete="off" required>
                                </div>
                                <div class="campo">
                                    <label for="maxFuncionariosCreate">Número de funcionários</label>
                                    <input type="number" name="maxFuncionarios" id="maxFuncionariosCreate" min="1" required>
                                </div>
                                <div class="campo">
                                    <label for="precoCreate">Preço</label>
                                    <input type="number" name="preco" id="precoCreate" step="0.01" min="0.01" placeholder="0,00" required>
                                </div>
                            </div>
                            <div>
                                <div class="campo">
                                    <label for="descricaoCreate">Descrição</label>
                                    <textarea type="text" name="descricao" id="descricaoCreate" required></textarea>
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
                    <h2>Editar plano</h2>
                    <form action="" method="post">
                        <div class="campos">
                            <div>
                                <div class="campo">
                                    <label for="nomeUpdate">Nome</label>
                                    <input type="text" name="nome" id="nomeUpdate" autocomplete="off" required>
                                </div>
                                <div class="campo">
                                    <label for="maxFuncionariosUpdate">Número de funcionários</label>
                                    <input type="number" name="maxFuncionarios" id="maxFuncionariosUpdate" min="1" required>
                                </div>
                                <div class="campo">
                                    <label for="precoUpdate">Preço</label>
                                    <input type="number" name="preco" id="precoUpdate" step="0.01" min="0.01" placeholder="0,00" required>                                </div>
                            </div>
                            <div>
                                <div class="campo">
                                    <label for="descricaoUpdate">Descrição</label>
                                    <textarea type="text" name="descricao" id="descricaoUpdate" required></textarea>
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
                    <h2>Excluir plano</h2>
                    <form action="" method="post">
                        <div class="campos">
                            <div>
                                <div class="campo">
                                    <label for="nomeDelete">Nome</label>
                                    <input type="text" name="nome" id="nomeDelete" autocomplete="off" disabled>
                                </div>
                                <div class="campo">
                                    <label for="maxFuncionariosDelete">Número de funcionários</label>
                                    <input type="number" name="maxFuncionarios" id="maxFuncionariosDelete" min="1" disabled>
                                </div>
                                <div class="campo">
                                    <label for="precoDelete">Preço</label>
                                    <input type="number" name="preco" id="precoDelete" step="0.01" min="0.01" placeholder="0,00" disabled>
                                </div>
                            </div>
                            <div>
                                <div class="campo">
                                    <label for="descricaoDelete">Descrição</label>
                                    <textarea type="text" name="descricao" id="descricaoDelete" disabled></textarea>
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

        <div class="tabelaScroll">
            <table class="tabelaPlanos">
                <thead>
                    <tr>
                        <th>Excluir</th>
                        <th>Ver</th>
                        <th>ID</th>
                        <th>Nome</th>
                        <th>Custo</th>
                        <th>Máximo de Funcionários</th>
                        <th>Descrição</th>
                    </tr>
                </thead>
                <tbody>
                <%
                    List<Plano> listaPlanos = (List<Plano>) request.getAttribute("listaPlanos");
                    if (listaPlanos != null && !listaPlanos.isEmpty()) {
                        for (Plano plano : listaPlanos) {
                %>
                    <tr>
                        <td>
                            <form method="get"><input type="hidden" name="acao" value="delete"><input type="hidden" name="pk" value="<%= plano.getId() %>"><button type="submit" class="detalhes"><img src="${pageContext.request.contextPath}/assets/crud/img/deletar-kronos.png" alt=""></button></form>
                        </td>
                        <td>
                            <form method="get"><input type="hidden" name="acao" value="update"><input type="hidden" name="pk" value="<%= plano.getId() %>"><button type="submit" class="detalhes"><img src="${pageContext.request.contextPath}/assets/crud/img/mais-detalhes.png" alt=""></button></form>                        
                        </td>
                        <td><%= plano.getId() %></td>
                        <td><%= plano.getNome() %></td>
                        <td><%= plano.getCusto() %></td>
                        <td><%= plano.getMaxFuncionarios() %></td>
                        <td><%= plano.getDescricao() %></td>

                    </tr>
                <%
                    }
                } else {
                %>
                    <tr>
                        <td colspan="6">Nenhum plano encontrado.</td>
                    </tr>
                <% } %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>
