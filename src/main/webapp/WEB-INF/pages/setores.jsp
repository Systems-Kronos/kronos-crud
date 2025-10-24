<%@ page import="com.example.Model.Setor" %>
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
    <link rel="icon" href="${pageContext.request.contextPath}/assets/crud/img/favikronos.ico" type="image/x-icon">
    <script src="${pageContext.request.contextPath}/assets/crud/script/script.js" defer></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/crud/style/dados.css">
    <title>Setores - Kronos CRUD</title>
</head>

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
                <li><a href="${pageContext.request.contextPath}/setores-crud" class="ativo"><img src="${pageContext.request.contextPath}/assets/crud/img/img-crud-setores.png" alt="">Setores</a></li>
                <li><a href="${pageContext.request.contextPath}/usuarios-crud"><img src="${pageContext.request.contextPath}/assets/crud/img/img-crud-usuario.png" alt="">Usuário</a></li>
            </ul>
        </nav>
    </header>

    <div class="conteudoPrincipal">
        <div class="procurarCadastrar">
            <form class="pesquisa">
                <input type="search" placeholder="Pesquisar" name="pesquisa" class="buscar">
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
                        <button type="submit">Aplicar</button>

                    </div>
                </details>
            </form>
            
                <!-- CREATE -->

                <section class="create">
                    <dialog id="create">
                        <h2>Cadastrar setor</h2>
                        <form action="" method="post">
                            <div class="campos">
                                <div>
                                    <div class="campo">
                                        <label for="nomeCreate">Nome</label>
                                        <input type="text" name="nome" id="nomeCreate" autocomplete="off" required>
                                    </div>
                                    <div class="campo">
                                        <label for="turnosCreate">Turnos</label>
                                        <input type="text" name="turnos" id="turnosCreate" autocomplete="off" required>
                                    </div>
                                    <div class="campo">
                                        <label for="qtdFuncionariosCreate">Número de funcionários</label>
                                        <input type="number" name="qtdFuncionarios" id="qtdFuncionariosCreate" min="0" required>
                                    </div>
                                </div>
                                <div>
                                    <div class="campo">
                                        <label for="descricaoCreate">Descrição</label>
                                        <textarea type="text" name="descricao" id="descricaoCreate" required></textarea>
                                    </div>
                                    <div class="campo">
                                        <label for="idEmpresaCreate">ID da Empresa</label>
                                        <input type="number" name="idEmpresa" id="idEmpresaCreate" min="1" required>
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
                    <dialog id="update">
                        <h2>Editar setor</h2>
                        <form action="" method="post">
                            <div class="campos">
                                <div>
                                    <div class="campo">
                                        <label for="nomeUpdate">Nome</label>
                                        <input type="text" name="nome" id="nomeUpdate" autocomplete="off" required>
                                    </div>
                                    <div class="campo">
                                        <label for="turnosUpdate">Turnos</label>
                                        <input type="text" name="turnos" id="turnosUpdate" autocomplete="off" required>
                                    </div>
                                    <div class="campo">
                                        <label for="qtdFuncionariosUpdate">Número de funcionários</label>
                                        <input type="number" name="qtdFuncionarios" id="qtdFuncionariosUpdate" min="0" required>
                                    </div>
                                </div>
                                <div>
                                    <div class="campo">
                                        <label for="descricaoUpdate">Descrição</label>
                                        <textarea type="text" name="descricao" id="descricaoUpdate" required></textarea>
                                    </div>
                                    <div class="campo">
                                        <label for="idEmpresaUpdate">ID da Empresa</label>
                                        <input type="number" name="idEmpresa" id="idEmpresaUpdate" min="1" required>
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
                    <dialog id="delete">
                        <h2>Excluir setor</h2>
                        <form action="" method="post">
                            <div class="campos">
                                <div>
                                    <div class="campo">
                                        <label for="nomeDelete">Nome</label>
                                        <input type="text" name="nome" id="nomeDelete" autocomplete="off" disabled>
                                    </div>
                                    <div class="campo">
                                        <label for="turnosDelete">Turnos</label>
                                        <input type="text" name="turnos" id="turnosDelete" autocomplete="off" disabled>
                                    </div>
                                    <div class="campo">
                                        <label for="qtdFuncionariosDelete">Número de funcionários</label>
                                        <input type="number" name="qtdFuncionarios" id="qtdFuncionariosDelete" min="1" disabled>
                                    </div>
                                </div>
                                <div>
                                    <div class="campo">
                                        <label for="descricaoDelete">Descrição</label>
                                        <textarea type="text" name="descricao" id="descricaoDelete" disabled></textarea>
                                    </div>
                                    <div class="campo">
                                        <label for="idEmpresaDelete">ID da Empresa</label>
                                        <input type="number" name="idEmpresa" id="idEmpresaDelete" disabled>
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
            <table class="tabelaHabilidades">
                <thead>
                    <tr>
                        <th>Excluir</th>
                        <th>Ver</th>
                        <th>ID</th>
                        <th>Nome</th>
                        <th>Quantidade de Funcionários</th>
                        <th>Turnos</th>
                        <th>Descrição</th>
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
                            <button type="button" id="modalUpdate" class="detalhes acaoModal" data-acao="abrir" data-modal="update" data-pk="<%= setor.getId() %>" data-caminho='{"base":"${pageContext.request.contextPath}","tabela":"admin-crud"}'><img src="${pageContext.request.contextPath}/assets/crud/img/mais-detalhes.png" alt=""></button>
                        </td>
                        <td>
                            <button type="button" id="modalDelete" class="detalhes acaoModal" data-acao="abrir" data-modal="delete" data-pk="<%= setor.getId() %>" data-caminho='{"base":"${pageContext.request.contextPath}","tabela":"admin-crud"}'><img src="${pageContext.request.contextPath}/assets/crud/img/deletar-kronos.png" alt=""></button>
                        </td>
                        <td><%= setor.getId() %></td>
                        <td><%= setor.getNome() %></td>
                        <td><%= setor.getQntFuncionarios() %></td>
                        <td><%= setor.getTurnos() %></td>
                        <td><%= setor.getDescricao() %></td>

                    </tr>
                <%
                    }
                } else {
                %>
                    <tr>
                        <td colspan="6">Nenhum setor encontrado.</td>
                    </tr>
                <% } %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>
