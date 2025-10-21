<%@ page import="com.example.Model.Empresa" %>
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
                <li><a href="${pageContext.request.contextPath}/empresas-crud" class="ativo"><img src="${pageContext.request.contextPath}/assets/crud/img/img-crud-empresas.png" alt="">Empresas</a></li>
                <li><a href="${pageContext.request.contextPath}/planos-crud"><img src="${pageContext.request.contextPath}/assets/crud/img/img-crud-planos.png" alt="">Planos</a></li>
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
                    <h2>Cadastrar empresa</h2>
                    <form action="" method="post">
                        <div class="campos">
                            <div>
                                <div class="campo">
                                    <label for="nomeCreate">Nome</label>
                                    <input type="text" name="nome" id="nomeCreate" autocomplete="off" required>
                                </div>
                                <div class="campo">
                                    <label for="senhaCreate">Senha</label>
                                    <input type="password" name="senha" id="senhaCreate" autocomplete="new-password" required>
                                </div>
                                <div class="campo">
                                    <label for="emailCreate">E-mail</label>
                                    <input type="email" name="email" id="emailCreate" autocomplete="off" required>
                                </div>
                                <div class="campo">
                                    <label for="cepCreate">CEP</label>
                                    <input type="text" name="cep" id="cepCreate" inputmode="numeric" autocomplete="off" pattern="\d{5}-?\d{3}" required>
                                </div>
                                <div class="campo">
                                    <label for="cnjpCreate">CNPJ</label>
                                    <input type="text" name="cnpj" id="cnjpCreate" inputmode="numeric" autocomplete="off" required>
                                </div>
                                <div class="campo">
                                    <label for="telefoneCreate">Telefone</label>
                                    <input type="tel" name="telefone" id="telefoneCreate" autocomplete="off" required>
                                </div>
                            </div>
                            <div>
                                <div class="campo">
                                    <label for="porteCreate">Porte</label>
                                    <input type="text" name="porte" id="porteCreate" autocomplete="off" required>
                                </div>
                                <div class="campo">
                                    <label for="segmentoCreate">Segmento</label>
                                    <input type="text" name="segmento" id="segmentoCreate" autocomplete="off" required>
                                </div>
                                <div class="campo">
                                    <label for="horaEntradaCreate">Horário de abertura</label>
                                    <input type="time" name="horaEntrada" id="horaEntradaCreate" required>
                                </div>
                                <div class="campo">
                                    <label for="horaFechamentoCreate">Horário de fechamento</label>
                                    <input type="time" name="horaFechamento" id="horaFechamentoCreate" required>
                                </div>
                                <div class="campo">
                                    <label for="regrasNegociosCreate">Regras de Negócios</label>
                                    <textarea type="text" name="regrasNegocios" id="regrasNegociosCreate" required></textarea>
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
                    <h2>Editar empresa</h2>
                    <form action="" method="post">
                        <div class="campos">
                            <div>
                                <div class="campo">
                                    <label for="nomeUpdate">Nome</label>
                                    <input type="text" name="nome" id="nomeUpdate" autocomplete="off" required>
                                </div>
                                <div class="campo">
                                    <label for="senhaUpdate">Senha</label>
                                    <input type="password" name="senha" id="senhaUpdate" autocomplete="new-password" required>
                                </div>
                                <div class="campo">
                                    <label for="emailUpdate">E-mail</label>
                                    <input type="email" name="email" id="emailUpdate" autocomplete="off" required>
                                </div>
                                <div class="campo">
                                    <label for="cepUpdate">CEP</label>
                                    <input type="text" name="cep" id="cepUpdate" inputmode="numeric" autocomplete="off" pattern="\d{5}-?\d{3}" required>
                                </div>
                                <div class="campo">
                                    <label for="cnjpUpdate">CNPJ</label>
                                    <input type="text" name="cnpj" id="cnjpUpdate" inputmode="numeric" autocomplete="off" required>
                                </div>
                                <div class="campo">
                                    <label for="telefoneUpdate">Telefone</label>
                                    <input type="tel" name="telefone" id="telefoneUpdate" autocomplete="off" required>
                                </div>
                            </div>
                            <div>
                                <div class="campo">
                                    <label for="porteUpdate">Porte</label>
                                    <input type="text" name="porte" id="porteUpdate" autocomplete="off" required>
                                </div>
                                <div class="campo">
                                    <label for="segmentoUpdate">Segmento</label>
                                    <input type="text" name="segmento" id="segmentoUpdate" autocomplete="off" required>
                                </div>
                                <div class="campo">
                                    <label for="horaEntradaUpdate">Horário de abertura</label>
                                    <input type="time" name="horaEntrada" id="horaEntradaUpdate" required>
                                </div>
                                <div class="campo">
                                    <label for="horaFechamentoUpdate">Horário de fechamento</label>
                                    <input type="time" name="horaFechamento" id="horaFechamentoUpdate" required>
                                </div>
                                <div class="campo">
                                    <label for="regrasNegociosUpdate">Regras de Negócios</label>
                                    <textarea type="text" name="regrasNegocios" id="regrasNegociosUpdate" required></textarea>
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
                    <h2>Excluir empresa</h2>
                    <form action="" method="post">
                        <div class="campos">
                            <div>
                                <div class="campo">
                                    <label for="nomeDelete">Nome</label>
                                    <input type="text" name="nome" id="nomeDelete" autocomplete="off" disabled>
                                </div>
                                <div class="campo">
                                    <label for="senhaDelete">Senha</label>
                                    <input type="password" name="senha" id="senhaDelete" autocomplete="new-password" disabled>
                                </div>
                                <div class="campo">
                                    <label for="emailDelete">E-mail</label>
                                    <input type="email" name="email" id="emailDelete" autocomplete="off" disabled>
                                </div>
                                <div class="campo">
                                    <label for="cepDelete">CEP</label>
                                    <input type="text" name="cep" id="cepDelete" inputmode="numeric" autocomplete="off" pattern="\d{5}-?\d{3}" disabled>
                                </div>
                                <div class="campo">
                                    <label for="cnjpDelete">CNPJ</label>
                                    <input type="text" name="cnpj" id="cnjpDelete" inputmode="numeric" autocomplete="off" disabled>
                                </div>
                                <div class="campo">
                                    <label for="telefoneDelete">Telefone</label>
                                    <input type="tel" name="telefone" id="telefoneDelete" autocomplete="off" disabled>
                                </div>
                            </div>
                            <div>
                                <div class="campo">
                                    <label for="porteDelete">Porte</label>
                                    <input type="text" name="porte" id="porteDelete" autocomplete="off" disabled>
                                </div>
                                <div class="campo">
                                    <label for="segmentoDelete">Segmento</label>
                                    <input type="text" name="segmento" id="segmentoDelete" autocomplete="off" disabled>
                                </div>
                                <div class="campo">
                                    <label for="horaEntradaDelete">Horário de abertura</label>
                                    <input type="time" name="horaEntrada" id="horaEntradaDelete" disabled>
                                </div>
                                <div class="campo">
                                    <label for="horaFechamentoDelete">Horário de fechamento</label>
                                    <input type="time" name="horaFechamento" id="horaFechamentoDelete" disabled>
                                </div>
                                <div class="campo">
                                    <label for="regrasNegociosDelete">Regras de Negócios</label>
                                    <textarea type="text" name="regrasNegocios" id="regrasNegociosDelete" disabled></textarea>
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
            <table class="tabelaEmpresas">
                <thead>
                    <tr>
                        <th>Ver</th>
                        <th>ID</th>
                        <th>Nome</th>
                        <!--- <th>Senha</th> --->
                        <th>E-mail</th>
                        <th>CEP</th>
                        <th>CNPJ</th>
                        <th>Telefone</th>
                        <th>Porte</th>
                        <!--- <th>Segmento</th> --->
                        <th>Abertura</th>
                        <th>Fechamento</th>
                        <th>Regras</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                <%
                    List<Empresa> listaEmpresas = (List<Empresa>) request.getAttribute("listaEmpresas");
                    if (listaEmpresas != null && !listaEmpresas.isEmpty()) {
                        for (Empresa empresa : listaEmpresas) {
                %>
                    <tr>
                        <td>
                            <form method="get"><input type="hidden" name="acao" value="update"><input type="hidden" name="pk" value="<%= empresa.getId() %>"><button type="submit" class="detalhes"><img src="${pageContext.request.contextPath}/assets/crud/img/mais-detalhes.png" alt=""></button></form>
                        </td>
                        <td><%= empresa.getId() %></td>
                        <td><%= empresa.getNome() %></td>
                        <!--- <td> empresa.getSenha() </td> --->
                        <td><%= empresa.getEmail() %></td>
                        <td><%= empresa.getCep() %></td>
                        <td><%= empresa.getCnpj() %></td>
                        <td><%= empresa.getTelefone() %></td>
                        <td><%= empresa.getPorte() %></td>
                        <!--- <td> empresa.getSegmento() </td> --->
                        <td><%= empresa.getHorarioAbertura() %></td>
                        <td><%= empresa.getHorarioFechamento() %></td>
                        <td>...</td>
                        <td>
                            <form method="get"><input type="hidden" name="acao" value="delete"><input type="hidden" name="pk" value="<%= empresa.getId() %>"><button type="submit" class="detalhes"><img src="${pageContext.request.contextPath}/assets/crud/img/deletar-kronos.png" alt=""></button></form>
                        </td>
                    </tr>
                <%
                    }
                } else {
                %>
                    <tr>
                        <td colspan="6">Nenhuma empresa encontrada.</td>
                    </tr>
                <% } %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>
