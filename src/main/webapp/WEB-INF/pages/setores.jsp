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
            <form class="pesquisa" method="get" action="${pageContext.request.contextPath}/setores-crud">
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
                        <button type="reset" id="botaoLimparFiltro" data-caminho='{"base":"${pageContext.request.contextPath}","tabela":"setores-crud"}'>Limpar filtros</button>
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
                                    <label for="qtnFuncionariosCreate">Número de funcionários</label>
                                    <input type="number" name="qtnFuncionarios" id="qtnFuncionariosCreate" min="0" required>
                                </div>
                                <div class="campo">
                                    <label for="turnosCreate">Turnos</label>
                                    <select name="turnos" id="turnosCreate" required>
                                        <option value="" disabled selected>Selecionar</option>
                                        <option value="Integral">Integral</option>
                                        <option value="Manhã">Manhã</option>
                                        <option value="Tarde">Tarde</option>
                                        <option value="Noite">Noite</option>
                                        <option value="Madrugada">Madrugada</option>
                                    </select>                                
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
                            <button type="button" class="cancelar acaoModal" data-acao="fechar" data-modal="create">Cancelar</button>
                            <button type="submit" class="confirmar">Cadastrar</button>
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
                        <div class="idAtual">
                            <label for="idUpdate">ID:</label>
                            <input type="button" name="id" id="idUpdate" disabled>
                        </div>
                        <div class="campos">
                            <div>
                                <div class="campo">
                                    <label for="nomeUpdate">Nome</label>
                                    <input type="text" name="nome" id="nomeUpdate" autocomplete="off" required>
                                </div>
                                <div class="campo">
                                    <label for="qtnFuncionariosUpdate">Número de funcionários</label>
                                    <input type="number" name="qtnFuncionarios" id="qtnFuncionariosUpdate" min="0" required>
                                </div>
                                <div class="campo">
                                    <label for="turnosUpdate">Turnos</label>
                                    <select name="turnos" id="turnosUpdate" required>
                                        <option value="" disabled selected>Selecionar</option>
                                        <option value="Integral">Integral</option>
                                        <option value="Manhã">Manhã</option>
                                        <option value="Tarde">Tarde</option>
                                        <option value="Noite">Noite</option>
                                        <option value="Madrugada">Madrugada</option>
                                    </select>                                 
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
                            <button type="button" class="cancelar acaoModal" data-acao="fechar" data-modal="update">Cancelar</button>
                            <button type="submit" class="confirmar">Confirmar alterações</button>

                        </menu>
                    </form>
                </dialog>
            </section>

            <!-- DELETE -->

            <section class="delete">
                <dialog id="delete">
                    <h2>Excluir setor</h2>
                    <form action="${pageContext.request.contextPath}/setores-delete" method="post">
                        <div class="idAtual">
                            <label for="idDelete">ID:</label>
                            <input type="button" name="id" id="idDelete" disabled>
                        </div>
                        <div class="campos">
                            <div>
                                <div class="campo">
                                    <label for="nomeDelete">Nome</label>
                                    <input type="text" name="nome" id="nomeDelete" autocomplete="off" disabled>
                                </div>
                                <div class="campo">
                                    <label for="qtnFuncionariosDelete">Número de funcionários</label>
                                    <input type="number" name="qtnFuncionarios" id="qtnFuncionariosDelete" min="1" disabled>
                                </div>
                                <div class="campo">
                                    <label for="turnosDelete">Turnos</label>
                                    <select name="turnos" id="turnosDelete" disabled>
                                        <option value="" disabled selected>Selecionar</option>
                                        <option value="Integral">Integral</option>
                                        <option value="Manhã">Manhã</option>
                                        <option value="Tarde">Tarde</option>
                                        <option value="Noite">Noite</option>
                                        <option value="Madrugada">Madrugada</option>
                                    </select>                                 
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
                            <button type="button" class="cancelar acaoModal" data-acao="fechar" data-modal="delete">Cancelar</button>
                            <button type="submit" class="confirmar">Confirmar exclusão</button>
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
                            <th>Excluir</th>
                            <th>ID</th>
                            <th>Nome</th>
                            <th>Empresa</th>
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
                                <button type="button" class="detalhes acaoModal" data-acao="abrir" data-modal="update" data-pk="<%= setor.getId() %>" data-caminho='{"base":"${pageContext.request.contextPath}","tabela":"setores-crud"}'><img src="${pageContext.request.contextPath}/assets/crud/img/mais-detalhes.png" alt=""></button>
                            </td>
                            <td>
                                <button type="button" class="detalhes acaoModal" data-acao="abrir" data-modal="delete" data-pk="<%= setor.getId() %>" data-caminho='{"base":"${pageContext.request.contextPath}","tabela":"setores-crud"}'><img src="${pageContext.request.contextPath}/assets/crud/img/deletar-kronos.png" alt=""></button>
                            </td>
                            <td><%= setor.getId() %></td>
                            <td><%= setor.getNome() %></td>
                            <td>Empresa Join</td>
                            <td><%= setor.getQntFuncionarios() %></td>
                            <td><%= setor.getTurnos() %></td>
                            <td><%= setor.getDescricao() %></td>
                        </tr>
                    <%
                        }
                    } else {
                    %>
                        <tr>
                            <td colspan="8">Nenhum setor encontrado.</td>
                        </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
        </main>
    </div>
</body>
</html>
