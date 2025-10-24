
<%@ page import="com.example.Model.Empresa" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Cinzel:wght@400;700;900&family=Montserrat:wght@300;400;500;700;900&family=Crete+Round:wght@400;700&display=swap" rel="stylesheet">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <script src="<%= request.getContextPath() %>/assets/crud/script/script.js" defer></script>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/crud/style/dados.css">
    <title>CRUD - Empresas</title>
</head>

<body>
    <div class="meuPlaceholder"></div>
    <header>
        <h1>KRONOS</h1>
        <nav>
            <ul>
                <li><a href="<%= request.getContextPath() %>/admin-crud"><img src="<%= request.getContextPath() %>/assets/crud/img/img-crud-administrador.png" alt="">Administrador</a></li>
                <li><a href="<%= request.getContextPath() %>/empresas-crud" class="ativo"><img src="<%= request.getContextPath() %>/assets/crud/img/img-crud-empresas.png" alt="">Empresas</a></li>
                <li><a href="<%= request.getContextPath() %>/planos-crud"><img src="<%= request.getContextPath() %>/assets/crud/img/img-crud-planos.png" alt="">Planos</a></li>
                <li><a href="<%= request.getContextPath() %>/habilidades-crud"><img src="<%= request.getContextPath() %>/assets/crud/img/img-crud-habilidades.png" alt="">Habilidades</a></li>
                <li><a href="<%= request.getContextPath() %>/setores-crud"><img src="<%= request.getContextPath() %>/assets/crud/img/img-crud-setores.png" alt="">Setores</a></li>
                <li><a href="<%= request.getContextPath() %>/usuarios-crud"><img src="<%= request.getContextPath() %>/assets/crud/img/img-crud-usuario.png" alt="">Usuário</a></li>
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
                        <label class="opcaoFiltro"><input type="radio" name="ordem" value="crescente">Crescente</label>
                        <label class="opcaoFiltro"><input type="radio" name="ordem" value="decrescente">Decrescente</label>
                        <button type="reset">Limpar filtros</button>
                        <button type="submit">Aplicar</button>
                    </div>
                </details>
            </form>

            <!-- CREATE -->

            <section class="create">
                <dialog id="create">
                    <h2>Cadastrar empresa</h2>
                    <form action="${pageContext.request.contextPath}/empresa-create" method="post">
                        <div class="campos">
                            <div>
                                <div class="campo">
                                    <label for="nomeCreate">Nome</label>
                                    <input type="text" name="nome" id="nomeCreate" autocomplete="off" required>
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
                                    <label for="cnpjCreate">CNPJ</label>
                                    <input type="text" name="cnpj" id="cnpjCreate" inputmode="numeric" autocomplete="off" required>
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
                    <div class="carregamento"></div>
                    <h2>Editar empresa</h2>
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
                                    <label for="emailUpdate">E-mail</label>
                                    <input type="email" name="email" id="emailUpdate" autocomplete="off" required>
                                </div>
                                <div class="campo">
                                    <label for="cepUpdate">CEP</label>
                                    <input type="text" name="cep" id="cepUpdate" inputmode="numeric" autocomplete="off" pattern="\d{5}-?\d{3}" required>
                                </div>
                                <div class="campo">
                                    <label for="cnpjUpdate">CNPJ</label>
                                    <input type="text" name="cnpj" id="cnpjUpdate" inputmode="numeric" autocomplete="off" required>
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
                            <button type="button" class="cancelar acaoModal" data-acao="fechar" data-modal="update">Cancelar</button>
                            <button type="submit" class="confirmar">Confirmar alterações</button>
                        </menu>
                    </form>
                </dialog>
            </section>

            <section class="delete">
                <dialog id="delete">
                    <div class="carregamento"></div>
                    <h2>Excluir empresa</h2>
                    <form action="" method="post">
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
                                    <label for="emailDelete">E-mail</label>
                                    <input type="email" name="email" id="emailDelete" autocomplete="off" disabled>
                                </div>
                                <div class="campo">
                                    <label for="cepDelete">CEP</label>
                                    <input type="text" name="cep" id="cepDelete" inputmode="numeric" autocomplete="off" pattern="\d{5}-?\d{3}" disabled>
                                </div>
                                <div class="campo">
                                    <label for="cnpjDelete">CNPJ</label>
                                    <input type="text" name="cnpj" id="cnpjDelete" inputmode="numeric" autocomplete="off" disabled>
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
                            <button type="button" class="acaoModal" data-acao="fechar" data-modal="delete" value="false">Cancelar</button>
                            <button type="submit" value="true">Confirmar exclusão</button>
                        </menu>
                    </form>
                </dialog>
            </section>
        </div>

        <div class="tabelaScroll">
            <table class="tabelaEmpresas">
                <thead>
                    <tr>
                        <th>Ver</th>
                        <th>Excluir</th>
                        <th>ID</th>
                        <th>Nome</th>
                        <th>E-mail</th>
                        <th>CEP</th>
                        <th>CNPJ</th>
                        <th>Telefone</th>
                        <th>Porte</th>
                        <th>Abertura</th>
                        <th>Fechamento</th>
                        <th>Regras</th>
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
                            <button type="button" class="detalhes acaoModal" data-acao="abrir" data-modal="update" data-pk="<%= empresa.getId() %>" data-caminho='{"base":"${pageContext.request.contextPath}","tabela":"empresas-crud"}'><img src="${pageContext.request.contextPath}/assets/crud/img/mais-detalhes.png" alt=""></button>
                        </td>
                        <td>
                            <button type="button" class="detalhes acaoModal" data-acao="abrir" data-modal="delete" data-pk="<%= empresa.getId() %>" data-caminho='{"base":"${pageContext.request.contextPath}","tabela":"empresas-crud"}'><img src="${pageContext.request.contextPath}/assets/crud/img/deletar-kronos.png" alt=""></button>
                        </td>
                        <td><%= empresa.getId() %></td>
                        <td><%= empresa.getNome() %></td>
                        <td><%= empresa.getEmail() %></td>
                        <td><%= empresa.getCep() %></td>
                        <td><%= empresa.getCnpj() %></td>
                        <td><%= empresa.getTelefone() %></td>
                        <td><%= empresa.getPorte() %></td>
                        <td><%= empresa.getHorarioAbertura() %></td>
                        <td><%= empresa.getHorarioFechamento() %></td>
                        <td>...</td>
                    </tr>
                <%
                    }
                } else {
                %>
                    <tr>
                        <td colspan="12">Nenhuma empresa encontrada.</td>
                    </tr>
                <% } %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>