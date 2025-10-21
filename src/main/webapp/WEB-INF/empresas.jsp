
<%@ page import="com.example.Model.Empresa" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
    // Pega parâmetros da URL (usados pelos botões da tabela)
    String acaoURL = request.getParameter("acao");
    String idAdquirido = request.getParameter("pk");

    // Pega atributos enviados pelo Servlet (em caso de erro ou para abrir modal)
    String abrirModal = (String) request.getAttribute("abrirModal");
    String erro = (String) request.getAttribute("erro");
    Empresa empModal = (Empresa) request.getAttribute("empresaModal");

    // Pega dados prévios (em caso de erro no CREATE)
    String nome_previo = (String) request.getAttribute("nome_previo");
    String email_previo = (String) request.getAttribute("email_previo");
    String cep_previo = (String) request.getAttribute("cep_previo");
    String cnpj_previo = (String) request.getAttribute("cnpj_previo");
    String telefone_previo = (String) request.getAttribute("telefone_previo");
    String porte_previo = (String) request.getAttribute("porte_previo");
    String horaEntrada_previo = (String) request.getAttribute("horaEntrada_previo");
    String horaFechamento_previo = (String) request.getAttribute("horaFechamento_previo");
    String regrasNegocios_previo = (String) request.getAttribute("regrasNegocios_previo");

    // Determina qual modal deve abrir
    if (abrirModal == null) {
        abrirModal = acaoURL; // Usa o da URL se não veio do atributo
    }
    if (abrirModal == null) {
        abrirModal = ""; // Garante que não seja nulo
    }

    boolean updateAberto = "update".equals(abrirModal);
    boolean deleteAberto = "delete".equals(abrirModal);
    boolean createAberto = "create".equals(abrirModal);
%>

<%!
    // MÉTODOS AUXILIARES (Declaração JSP)
    String stringValue(String s) {
        return (s != null) ? s : "";
    }
    String timeValue(java.time.LocalTime t) {
        return (t != null) ? t.toString() : "";
    }
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <%-- Seus links <head> --%>
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
            <%-- Links usando getContextPath() --%>
            <li><a href="<%= request.getContextPath() %>/admin-crud"><img src="<%= request.getContextPath() %>/assets/crud/img/img-crud-administrador.png" alt="">Administrador</a></li>
            <li><a href="<%= request.getContextPath() %>/empresas-crud" class="ativo"><img src="<%= request.getContextPath() %>/assets/crud/img/img-crud-empresas.png" alt="">Empresas</a></li>
            <li><a href="<%= request.getContextPath() %>/planos-crud"><img src="<%= request.getContextPath() %>/assets/crud/img/img-crud-planos.png" alt="">Planos</a></li>
            <li><a href="<%= request.getContextPath() %>/habilidades-crud"><img src="<%= request.getContextPath() %>/assets/crud/img/img-crud-habilidades.png" alt="">Habilidades</a></li>
            <li><a href="<%= request.getContextPath() %>/setores-crud"><img src="<%= request.getContextPath() %>/assets/crud/img/img-crud-setores.png" alt="">Setores</a></li>
            <li><a href="<%= request.getContextPath() %>/usuarios-crud"><img src="<%= request.getContextPath() %>/assets/crud/img/img-crud-usuario.png" alt="">Usuário</a></li>
        </ul>
    </nav>
</header>

<%-- BLOCO DE ERRO --%>
<% if (erro != null && !erro.isEmpty()) { %>
<div style="background-color: #FFD2D2; border: 1px solid #D800 C; color: #A00; padding: 10px; margin: 10px 4%; border-radius: 5px; width: 90%;">
    <strong>ERRO:</strong> <%= erro %>
</div>
<% } %>

<div class="conteudoPrincipal">
    <div class="procurarCadastrar">
        <%-- Formulário de pesquisa --%>
        <form class="pesquisa">
            <input type="search" placeholder="Pesquisar" id="pesquisa" name="pesquisa" class="buscar">
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
            <dialog id="create" data-abrir="<%=createAberto%>">
                <h2>Cadastrar empresa</h2>
                <form action="<%= request.getContextPath() %>/empresa-create" method="post">
                    <div class="campos">
                        <div>
                            <div class="campo">
                                <label for="nomeCreate">Nome</label>
                                <input type="text" name="nome" id="nomeCreate" autocomplete="off" required
                                       value="<%= stringValue(nome_previo) %>">
                            </div>
                            <div class="campo">
                                <label for="emailCreate">E-mail</label>
                                <input type="email" name="email" id="emailCreate" autocomplete="off" required
                                       value="<%= stringValue(email_previo) %>">
                            </div>
                            <div class="campo">
                                <label for="cepCreate">CEP</label>
                                <input type="text" name="cep" id="cepCreate" inputmode="numeric" autocomplete="off" pattern="\d{5}-?\d{3}" required
                                       value="<%= stringValue(cep_previo) %>">
                            </div>
                            <div class="campo">
                                <label for="cnjpCreate">CNPJ</label>
                                <input type="text" name="cnpj" id="cnjpCreate" inputmode="numeric" autocomplete="off" required
                                       value="<%= stringValue(cnpj_previo) %>">
                            </div>
                            <div class="campo">
                                <label for="telefoneCreate">Telefone</label>
                                <input type="tel" name="telefone" id="telefoneCreate" autocomplete="off" required
                                       value="<%= stringValue(telefone_previo) %>">
                            </div>
                        </div>
                        <div>
                            <div class="campo">
                                <label for="porteCreate">Porte</label>
                                <input type="text" name="porte" id="porteCreate" autocomplete="off" required
                                       value="<%= stringValue(porte_previo) %>">
                            </div>
                            <div class="campo">
                                <label for="horaEntradaCreate">Horário de abertura</label>
                                <input type="time" name="horaEntrada" id="horaEntradaCreate" required
                                       value="<%= stringValue(horaEntrada_previo) %>">
                            </div>
                            <div class="campo">
                                <label for="horaFechamentoCreate">Horário de fechamento</label>
                                <input type="time" name="horaFechamento" id="horaFechamentoCreate" required
                                       value="<%= stringValue(horaFechamento_previo) %>">
                            </div>
                            <div class="campo">
                                <label for="regrasNegociosCreate">Regras de Negócios</label>
                                <textarea type="text" name="regrasNegocios" id="regrasNegociosCreate" required><%= stringValue(regrasNegocios_previo) %></textarea>
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

        <%-- COMENTÁRIO RESTAURADO --%>
        <section class="update">
            <dialog id="update" data-abrir="<%=updateAberto%>">
                <h2>Editar empresa</h2>
                <form action="<%= request.getContextPath() %>/empresas-update" method="post">
                    <input type="hidden" name="pk" value="<%= (empModal != null) ? empModal.getId() : "" %>">
                    <div class="campos">
                        <div>
                            <div class="campo">
                                <label for="nomeUpdate">Nome</label>
                                <input type="text" name="nome" id="nomeUpdate" autocomplete="off" required
                                       value="<%= (empModal != null) ? stringValue(empModal.getNome()) : "" %>">
                            </div>
                            <div class="campo">
                                <label for="emailUpdate">E-mail</label>
                                <input type="email" name="email" id="emailUpdate" autocomplete="off" required
                                       value="<%= (empModal != null) ? stringValue(empModal.getEmail()) : "" %>">
                            </div>
                            <div class="campo">
                                <label for="cepUpdate">CEP</label>
                                <input type="text" name="cep" id="cepUpdate" inputmode="numeric" autocomplete="off" pattern="\d{5}-?\d{3}" required
                                       value="<%= (empModal != null) ? stringValue(empModal.getCep()) : "" %>">
                            </div>
                            <div class="campo">
                                <label for="cnjpUpdate">CNPJ</label>
                                <input type="text" name="cnpj" id="cnjpUpdate" inputmode="numeric" autocomplete="off" required
                                       value="<%= (empModal != null) ? stringValue(empModal.getCnpj()) : "" %>">
                            </div>
                            <div class="campo">
                                <label for="telefoneUpdate">Telefone</label>
                                <input type="tel" name="telefone" id="telefoneUpdate" autocomplete="off" required
                                       value="<%= (empModal != null) ? stringValue(empModal.getTelefone()) : "" %>">
                            </div>
                        </div>
                        <div>
                            <div class="campo">
                                <label for="porteUpdate">Porte</label>
                                <input type="text" name="porte" id="porteUpdate" autocomplete="off" required
                                       value="<%= (empModal != null) ? stringValue(empModal.getPorte()) : "" %>">
                            </div>
                            <div class="campo">
                                <label for="horaEntradaUpdate">Horário de abertura</label>
                                <input type="time" name="horaEntrada" id="horaEntradaUpdate" required
                                       value="<%= (empModal != null) ? timeValue(empModal.getHorarioAbertura()) : "" %>">
                            </div>
                            <div class="campo">
                                <label for="horaFechamentoUpdate">Horário de fechamento</label>
                                <input type="time" name="horaFechamento" id="horaFechamentoUpdate" required
                                       value="<%= (empModal != null) ? timeValue(empModal.getHorarioFechamento()) : "" %>">
                            </div>
                            <div class="campo">
                                <label for="regrasNegociosUpdate">Regras de Negócios</label>
                                <textarea type="text" name="regrasNegocios" id="regrasNegociosUpdate" required><%= (empModal != null) ? stringValue(empModal.getRegraDeNegocios()) : "" %></textarea>
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
            <dialog id="delete" data-abrir="<%=deleteAberto%>">
                <h2>Excluir empresa</h2>
                <form action="<%= request.getContextPath() %>/empresas-delete" method="post">
                    <input type="hidden" name="pk" value="<%= (empModal != null) ? empModal.getId() : "" %>">
                    <div class="campos">
                        <div>
                            <div class="campo">
                                <label for="nomeDelete">Nome</label>
                                <input type="text" name="nome" id="nomeDelete" autocomplete="off" disabled
                                       value="<%= (empModal != null) ? stringValue(empModal.getNome()) : "" %>">
                            </div>
                            <div class="campo">
                                <label for="emailDelete">E-mail</label>
                                <input type="email" name="email" id="emailDelete" autocomplete="off" disabled
                                       value="<%= (empModal != null) ? stringValue(empModal.getEmail()) : "" %>">
                            </div>
                            <div class="campo">
                                <label for="cepDelete">CEP</label>
                                <input type="text" name="cep" id="cepDelete" inputmode="numeric" autocomplete="off" pattern="\d{5}-?\d{3}" disabled
                                       value="<%= (empModal != null) ? stringValue(empModal.getCep()) : "" %>">
                            </div>
                            <div class="campo">
                                <label for="cnjpDelete">CNPJ</label>
                                <input type="text" name="cnpj" id="cnjpDelete" inputmode="numeric" autocomplete="off" disabled
                                       value="<%= (empModal != null) ? stringValue(empModal.getCnpj()) : "" %>">
                            </div>
                            <div class="campo">
                                <label for="telefoneDelete">Telefone</label>
                                <input type="tel" name="telefone" id="telefoneDelete" autocomplete="off" disabled
                                       value="<%= (empModal != null) ? stringValue(empModal.getTelefone()) : "" %>">
                            </div>
                        </div>
                        <div>
                            <div class="campo">
                                <label for="porteDelete">Porte</label>
                                <input type="text" name="porte" id="porteDelete" autocomplete="off" disabled
                                       value="<%= (empModal != null) ? stringValue(empModal.getPorte()) : "" %>">
                            </div>
                            <div class="campo">
                                <label for="horaEntradaDelete">Horário de abertura</label>
                                <input type="time" name="horaEntrada" id="horaEntradaDelete" disabled
                                       value="<%= (empModal != null) ? timeValue(empModal.getHorarioAbertura()) : "" %>">
                            </div>
                            <div class="campo">
                                <label for="horaFechamentoDelete">Horário de fechamento</label>
                                <input type="time" name="horaFechamento" id="horaFechamentoDelete" disabled
                                       value="<%= (empModal != null) ? timeValue(empModal.getHorarioFechamento()) : "" %>">
                            </div>
                            <div class="campo">
                                <label for="regrasNegociosDelete">Regras de Negócios</label>
                                <textarea type="text" name="regrasNegocios" id="regrasNegociosDelete" disabled><%= (empModal != null) ? stringValue(empModal.getRegraDeNegocios()) : "" %></textarea>
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
        <table class="tabelaEmpresas">
            <thead>
            <tr>
                <th>Ver</th>
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
                    <form method="get" action="<%= request.getContextPath() %>/empresas-update">
                        <input type="hidden" name="acao" value="update">
                        <input type="hidden" name="pk" value="<%= empresa.getId() %>">
                        <button type="submit" class="detalhes"><img src="<%= request.getContextPath() %>/assets/crud/img/mais-detalhes.png" alt=""></button>
                    </form>
                </td>
                <td><%= empresa.getId() %></td>
                <td><%= stringValue(empresa.getNome()) %></td>
                <td><%= stringValue(empresa.getEmail()) %></td>
                <td><%= stringValue(empresa.getCep()) %></td>
                <td><%= stringValue(empresa.getCnpj()) %></td>
                <td><%= stringValue(empresa.getTelefone()) %></td>
                <td><%= stringValue(empresa.getPorte()) %></td>
                <td><%= timeValue(empresa.getHorarioAbertura()) %></td>
                <td><%= timeValue(empresa.getHorarioFechamento()) %></td>
                <td>...</td>
                <td>
                    <form method="get" action="<%= request.getContextPath() %>/empresas-delete">
                        <input type="hidden" name="acao" value="delete">
                        <input type="hidden" name="pk" value="<%= empresa.getId() %>">
                        <button type="submit" class="detalhes"><img src="<%= request.getContextPath() %>/assets/crud/img/deletar-kronos.png" alt=""></button>
                    </form>
                </td>
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