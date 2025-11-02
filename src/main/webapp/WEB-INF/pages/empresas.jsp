<%@ page import="com.example.Model.Empresa" %>
<%@ page import="com.example.Model.Plano" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%-- Bloco de Processamento de Atributos (para erros e repopulação) --%>
<%
    /* --- Processamento de Atributos (Empresa) --- */

    String erro = (String) request.getAttribute("erro");
    String modalAberto = (String) request.getAttribute("abrirModal");
    Empresa empresaModal = (Empresa) request.getAttribute("empresaModal");

    List<Plano> listaPlanos = (List<Plano>) request.getAttribute("listaPlanos");
    if (listaPlanos == null) {
        listaPlanos = new ArrayList<>();
    }

    // Variáveis para o modal CREATE (em caso de erro)
    String createNome = ""; String createCep = ""; String createCnpj = "";
    String createEmail = ""; String createTelefone = ""; String createPorte = "";
    String createHoraAbertura = ""; String createHoraFechamento = "";
    String createRegras = ""; String createIdPlano = "";

    if ("create".equals(modalAberto)) {
        createNome = request.getAttribute("nome_previo") != null ? (String)request.getAttribute("nome_previo") : "";
        createCep = request.getAttribute("cep_previo") != null ? (String)request.getAttribute("cep_previo") : "";
        createCnpj = request.getAttribute("cnpj_previo") != null ? (String)request.getAttribute("cnpj_previo") : "";
        createEmail = request.getAttribute("email_previo") != null ? (String)request.getAttribute("email_previo") : "";
        createTelefone = request.getAttribute("telefone_previo") != null ? (String)request.getAttribute("telefone_previo") : "";
        createPorte = request.getAttribute("porte_previo") != null ? (String)request.getAttribute("porte_previo") : "";
        createHoraAbertura = request.getAttribute("horaAbertura_previo") != null ? (String)request.getAttribute("horaAbertura_previo") : "";
        createHoraFechamento = request.getAttribute("horaFechamento_previo") != null ? (String)request.getAttribute("horaFechamento_previo") : "";
        createRegras = request.getAttribute("regrasNegocios_previo") != null ? (String)request.getAttribute("regrasNegocios_previo") : "";
        createIdPlano = request.getAttribute("plano_previo") != null ? (String)request.getAttribute("plano_previo") : "";
    }

    // Variáveis para o modal UPDATE (para GET ou erro de POST)
    String updateID = ""; String updateNome = ""; String updateCep = ""; String updateCnpj = "";
    String updateEmail = ""; String updateTelefone = ""; String updatePorte = "";
    String updateHoraAbertura = ""; String updateHoraFechamento = "";
    String updateRegras = ""; String updateIdPlano = "";

    if (empresaModal != null) {
        updateID = String.valueOf(empresaModal.getId());
        updateNome = empresaModal.getNome();
        updateCep = empresaModal.getCep();
        updateCnpj = empresaModal.getCnpj();
        updateEmail = empresaModal.getEmail();
        updateTelefone = empresaModal.getTelefone();
        updatePorte = empresaModal.getPorte();
        updateHoraAbertura = empresaModal.getHorarioAbertura().toString();
        updateHoraFechamento = empresaModal.getHorarioFechamento().toString();
        updateRegras = empresaModal.getRegraDeNegocios();
        updateIdPlano = String.valueOf(empresaModal.getIdPlano());
    }
    if ("update".equals(modalAberto) && request.getAttribute("nome_previo") != null) {
        updateNome = (String) request.getAttribute("nome_previo");
        updateCep = (String) request.getAttribute("cep_previo");
        updateCnpj = (String) request.getAttribute("cnpj_previo");
        updateEmail = (String) request.getAttribute("email_previo");
        updateTelefone = (String) request.getAttribute("telefone_previo");
        updatePorte = (String) request.getAttribute("porte_previo");
        updateHoraAbertura = (String) request.getAttribute("horaAbertura_previo");
        updateHoraFechamento = (String) request.getAttribute("horaFechamento_previo");
        updateRegras = (String) request.getAttribute("regrasNegocios_previo");
        updateIdPlano = (String) request.getAttribute("plano_previo");
    }

    // Variáveis para o modal DELETE (para GET)
    String deleteID = ""; String deleteNome = ""; String deleteCep = ""; String deleteCnpj = "";
    String deleteEmail = ""; String deleteTelefone = ""; String deletePorte = "";
    String deleteHoraAbertura = ""; String deleteHoraFechamento = "";
    String deleteRegras = ""; String deleteIdPlano = "";

    if (empresaModal != null && "delete".equals(modalAberto)) {
        deleteID = String.valueOf(empresaModal.getId());
        deleteNome = empresaModal.getNome();
        deleteCep = empresaModal.getCep();
        deleteCnpj = empresaModal.getCnpj();
        deleteEmail = empresaModal.getEmail();
        deleteTelefone = empresaModal.getTelefone();
        deletePorte = empresaModal.getPorte();
        deleteHoraAbertura = empresaModal.getHorarioAbertura().toString();
        deleteHoraFechamento = empresaModal.getHorarioFechamento().toString();
        deleteRegras = empresaModal.getRegraDeNegocios();
        deleteIdPlano = String.valueOf(empresaModal.getIdPlano());
    }
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
    <title>Empresas - Kronos CRUD</title>
</head>

<%-- Passa o modal a ser aberto (em caso de erro) para o script.js --%>
<body data-modal-para-abrir="<%= modalAberto != null ? modalAberto : "" %>">
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
        <a href="${pageContext.request.contextPath}/landingpage/index.html" class="sairCrud">Voltar para Landing Page</a>
    </header>
    <div class="navegacaoContexto">
        <a href="${pageContext.request.contextPath}/admin-crud">Administrador</a>
        <a href="${pageContext.request.contextPath}/empresas-crud" class="ativo">Empresas</a>
        <a href="${pageContext.request.contextPath}/planos-crud">Planos</a>
        <a href="${pageContext.request.contextPath}/habilidades-crud">Habilidades</a>
        <a href="${pageContext.request.contextPath}/setores-crud">Setores</a>
        <a href="${pageContext.request.contextPath}/usuarios-crud">Usuário</a>
    </div>

    <div class="conteudoPrincipal">

        <%-- Bloco de Exibição de Erro (Geral e Parcial) --%>
        <%
            String erroParcial = (String) session.getAttribute("erro_parcial");
            if (erroParcial != null) {
        %>
        <div class="mensagem-aviso">
            <strong>Aviso:</strong> <%= erroParcial %>
        </div>
        <%
                session.removeAttribute("erro_parcial");
            }
        %>

        <div class="procurarCadastrar">

            <%-- ==== FILTRO DE PESQUISA ==== --%>
            <form class="pesquisa" method="get" action="${pageContext.request.contextPath}/empresas-crud">
                <input type="search" placeholder="Pesquisar" id="pesquisa" name="pesquisa" class="buscar" value="<%= request.getParameter("pesquisa") != null ? request.getParameter("pesquisa") : "" %>">
                <details class="filtros">
                    <summary>Filtros</summary>
                    <div class="conteudoFiltros">
                        <label class="opcaoFiltro"> <input type="radio" name="ordem" value="crescente" <%= "crescente".equals(request.getParameter("ordem")) ? "checked" : "" %>> Crescente </label>
                        <label class="opcaoFiltro"> <input type="radio" name="ordem" value="decrescente" <%= "decrescente".equals(request.getParameter("ordem")) ? "checked" : "" %>> Decrescente </label>
                        <button type="reset" id="botaoLimparFiltro" data-caminho='{"base":"${pageContext.request.contextPath}","tabela":"empresas-crud"}'>Limpar filtros</button>
                        <button type="submit">Aplicar</button>
                    </div>
                </details>
            </form>

            <%-- ==== CREATE ==== --%>
            <section class="create">
                <dialog id="create">
                    <h2>Cadastrar empresa</h2>
                    <% if ("create".equals(modalAberto) && erro != null && !erro.isEmpty()) { %>
                    <div class="mensagem-erro">
                        <strong>Erro:</strong> <%= erro %>
                    </div>
                    <% } %>
                    <form action="${pageContext.request.contextPath}/empresa-create" id="formCreate" method="post">
                        <div class="campos">
                            <div>
                                <div class="campo"> <label for="nomeCreate">Nome</label> <input type="text" name="nome" id="nomeCreate" autocomplete="off" required value="<%= createNome %>"> </div>
                                <div class="campo"> <label for="emailCreate">E-mail</label> <input type="email" name="email" id="emailCreate" autocomplete="off" required value="<%= createEmail %>"> </div>
                                <div class="campo"> <label for="cepCreate">CEP</label> <input type="text" name="cep" id="cepCreate" inputmode="numeric" autocomplete="off" pattern="\d{5}-?\d{3}" required value="<%= createCep %>"> </div>
                                <div class="campo"> <label for="cnpjCreate">CNPJ</label> <input type="text" name="cnpj" id="cnpjCreate" inputmode="numeric" autocomplete="off" required value="<%= createCnpj %>"> </div>
                                <div class="campo"> <label for="telefoneCreate">Telefone</label> <input type="tel" name="telefone" id="telefoneCreate" autocomplete="off" required value="<%= createTelefone %>"> </div>
                            </div>
                            <div>
                                <div class="campoLado">
                                    <div class="campo">
                                        <label for="porteCreate">Porte</label>
                                        <select type="text" name="porte" id="porteCreate" required>
                                            <option value="" <%= createPorte.isEmpty() ? "selected" : "" %> disabled>Selecionar</option>
                                            <option value="Pequeno" <%= "Pequeno".equals(createPorte) ? "selected" : "" %>>Pequeno</option>
                                            <option value="Médio" <%= "Médio".equals(createPorte) ? "selected" : "" %>>Médio</option>
                                            <option value="Grande" <%= "Grande".equals(createPorte) ? "selected" : "" %>>Grande</option>
                                        </select>
                                    </div>
                                    <div class="campo">
                                        <label for="planoCreate">Plano</label>
                                        <select name="plano" id="planoCreate" required>
                                            <option value="" <%= createIdPlano.isEmpty() ? "selected" : "" %> disabled>Selecionar</option>
                                            <%-- Dropdown dinâmico de Planos --%>
                                            <% for (Plano plano : listaPlanos) { %>
                                            <option value="<%= plano.getId() %>" <%= String.valueOf(plano.getId()).equals(createIdPlano) ? "selected" : "" %>>
                                                <%= plano.getNome() %>
                                            </option>
                                            <% } %>
                                        </select>
                                    </div>
                                </div>
                                <div class="campo"> <label for="horaAberturaCreate">Horário de abertura</label> <input type="time" name="horaAbertura" id="horaAberturaCreate" required value="<%= createHoraAbertura %>"> </div>
                                <div class="campo"> <label for="horaFechamentoCreate">Horário de fechamento</label> <input type="time" name="horaFechamento" id="horaFechamentoCreate" required value="<%= createHoraFechamento %>"> </div>
                                <div class="campo"> <label for="regrasNegociosCreate">Regras de Negócios</label> <textarea type="text" name="regrasNegocios" id="regrasNegociosCreate" required><%= createRegras %></textarea> </div>
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
                    <h2>Editar empresa</h2>
                    <% if ("update".equals(modalAberto) && erro != null && !erro.isEmpty()) { %>
                    <div class="mensagem-erro">
                        <strong>Erro:</strong> <%= erro %>
                    </div>
                    <% } %>
                    <form action="${pageContext.request.contextPath}/empresas-update" method="post">
                        <div class="idAtual"> <label for="idUpdate">ID:</label> <input type="number" name="id" id="idUpdate" readonly value="<%= updateID %>"> </div>
                        <div class="campos">
                            <div>
                                <div class="campo"> <label for="nomeUpdate">Nome</label> <input type="text" name="nome" id="nomeUpdate" autocomplete="off" required value="<%= updateNome %>"> </div>
                                <div class="campo"> <label for="emailUpdate">E-mail</label> <input type="email" name="email" id="emailUpdate" autocomplete="off" required value="<%= updateEmail %>"> </div>
                                <div class="campo"> <label for="cepUpdate">CEP</label> <input type="text" name="cep" id="cepUpdate" inputmode="numeric" autocomplete="off" pattern="\d{5}-?\d{3}" required value="<%= updateCep %>"> </div>
                                <div class="campo"> <label for="cnpjUpdate">CNPJ</label> <input type="text" name="cnpj" id="cnpjUpdate" inputmode="numeric" autocomplete="off" required value="<%= updateCnpj %>"> </div>
                                <div class="campo"> <label for="telefoneUpdate">Telefone</label> <input type="tel" name="telefone" id="telefoneUpdate" autocomplete="off" required value="<%= updateTelefone %>"> </div>
                            </div>
                            <div>
                                <div class="campoLado">
                                    <div class="campo">
                                        <label for="porteUpdate">Porte</label>
                                        <select type="text" name="porte" id="porteUpdate" required>
                                            <option value="" <%= updatePorte.isEmpty() ? "selected" : "" %> disabled>Selecionar</option>
                                            <option value="Pequeno" <%= "Pequeno".equals(updatePorte) ? "selected" : "" %>>Pequeno</option>
                                            <option value="Médio" <%= "Médio".equals(updatePorte) ? "selected" : "" %>>Médio</option>
                                            <option value="Grande" <%= "Grande".equals(updatePorte) ? "selected" : "" %>>Grande</option>
                                        </select>
                                    </div>
                                    <div class="campo">
                                        <label for="planoUpdate">Plano</label>
                                        <select name="plano" id="planoUpdate" required>
                                            <option value="" <%= updateIdPlano.isEmpty() ? "selected" : "" %> disabled>Selecionar</option>
                                            <%-- Dropdown dinâmico de Planos --%>
                                            <% for (Plano plano : listaPlanos) { %>
                                            <option value="<%= plano.getId() %>" <%= String.valueOf(plano.getId()).equals(updateIdPlano) ? "selected" : "" %>>
                                                <%= plano.getNome() %>
                                            </option>
                                            <% } %>
                                        </select>
                                    </div>
                                </div>
                                <div class="campo"> <label for="horaAberturaUpdate">Horário de abertura</label> <input type="time" name="horaAbertura" id="horaAberturaUpdate" required value="<%= updateHoraAbertura %>"> </div>
                                <div class="campo"> <label for="horaFechamentoUpdate">Horário de fechamento</label> <input type="time" name="horaFechamento" id="horaFechamentoUpdate" required value="<%= updateHoraFechamento %>"> </div>
                                <div class="campo"> <label for="regrasNegociosUpdate">Regras de Negócios</label> <textarea type="text" name="regrasNegocios" id="regrasNegociosUpdate" required><%= updateRegras %></textarea> </div>
                            </div>
                        </div>
                        <menu> <button type="button" class="cancelar acaoModal" data-acao="fechar" data-modal="update">Cancelar</button> <button type="submit" class="confirmar">Confirmar alterações</button> </menu>
                    </form>
                </dialog>
            </section>

            <%-- ==== DELETE ==== --%>
            <section class="delete">
                <dialog id="delete">
                    <h2>Excluir empresa</h2>
                    <% if ("delete".equals(modalAberto) && erro != null && !erro.isEmpty()) { %>
                    <div class="mensagem-erro">
                        <strong>Erro:</strong> <%= erro %>
                    </div>
                    <% } %>
                    <form action="${pageContext.request.contextPath}/empresas-delete" method="post">
                        <div class="idAtual"> <label for="idDelete">ID:</label> <input type="number" name="id" id="idDelete" readonly value="<%= deleteID %>"> </div>
                        <div class="campos">
                            <div>
                                <div class="campo"> <label for="nomeDelete">Nome</label> <input type="text" name="nome" id="nomeDelete" autocomplete="off" disabled value="<%= deleteNome %>"> </div>
                                <div class="campo"> <label for="emailDelete">E-mail</label> <input type="email" name="email" id="emailDelete" autocomplete="off" disabled value="<%= deleteEmail %>"> </div>
                                <div class="campo"> <label for="cepDelete">CEP</label> <input type="text" name="cep" id="cepDelete" inputmode="numeric" autocomplete="off" pattern="\d{5}-?\d{3}" disabled value="<%= deleteCep %>"> </div>
                                <div class="campo"> <label for="cnpjDelete">CNPJ</label> <input type="text" name="cnpj" id="cnpjDelete" inputmode="numeric" autocomplete="off" disabled value="<%= deleteCnpj %>"> </div>
                                <div class="campo"> <label for="telefoneDelete">Telefone</label> <input type="tel" name="telefone" id="telefoneDelete" autocomplete="off" disabled value="<%= deleteTelefone %>"> </div>
                            </div>
                            <div>
                                <div class="campoLado">
                                    <div class="campo">
                                        <label for="porteDelete">Porte</label>
                                        <select type="text" name="porte" id="porteDelete" disabled>
                                            <option value="" <%= deletePorte.isEmpty() ? "selected" : "" %> disabled>Selecionar</option>
                                            <option value="Pequeno" <%= "Pequeno".equals(deletePorte) ? "selected" : "" %>>Pequeno</option>
                                            <option value="Médio" <%= "Médio".equals(deletePorte) ? "selected" : "" %>>Médio</option>
                                            <option value="Grande" <%= "Grande".equals(deletePorte) ? "selected" : "" %>>Grande</option>
                                        </select>
                                    </div>
                                    <div class="campo">
                                        <label for="planoDelete">Plano</label>
                                        <select name="plano" id="planoDelete" disabled>
                                            <option value="" <%= deleteIdPlano.isEmpty() ? "selected" : "" %> disabled>Selecionar</option>
                                            <%-- Dropdown dinâmico de Planos --%>
                                            <% for (Plano plano : listaPlanos) { %>
                                            <option value="<%= plano.getId() %>" <%= String.valueOf(plano.getId()).equals(deleteIdPlano) ? "selected" : "" %>>
                                                <%= plano.getNome() %>
                                            </option>
                                            <% } %>
                                        </select>
                                    </div>
                                </div>
                                <div class="campo"> <label for="horaAberturaDelete">Horário de abertura</label> <input type="time" name="horaAbertura" id="horaAberturaDelete" disabled value="<%= deleteHoraAbertura %>"> </div>
                                <div class="campo"> <label for="horaFechamentoDelete">Horário de fechamento</label> <input type="time" name="horaFechamento" id="horaFechamentoDelete" disabled value="<%= deleteHoraFechamento %>"> </div>
                                <div class="campo"> <label for="regrasNegociosDelete">Regras de Negócios</label> <textarea type="text" name="regrasNegocios" id="regrasNegociosDelete" disabled><%= deleteRegras %></textarea> </div>
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
                <table class="tabelaEmpresas">
                    <thead>
                    <tr>
                        <th>Ver</th>
                        <th>Excluir</th>
                        <th>ID</th>
                        <th>Nome</th>
                        <th>E-mail</th>
                        <th>Porte</th>
                        <th>Plano</th>
                        <th>CEP</th>
                        <th>CNPJ</th>
                        <th>Telefone</th>
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

                                int idPlano = empresa.getIdPlano();
                                String nomePlano = "ID " + idPlano; // Default
                                for (Plano plano : listaPlanos) {
                                    if (plano.getId() == idPlano) {
                                        nomePlano = plano.getNome();
                                        break;
                                    }
                                }
                    %>
                    <tr>
                        <td> <button type="button" class="detalhes acaoModal" data-acao="abrir" data-modal="update" data-pk="<%= empresa.getId() %>" data-caminho='{"base":"${pageContext.request.contextPath}","tabela":"empresas-crud"}'><img src="${pageContext.request.contextPath}/assets/crud/img/mais-detalhes.png" alt="Ver/Editar"></button> </td>
                        <td> <button type="button" class="detalhes acaoModal" data-acao="abrir" data-modal="delete" data-pk="<%= empresa.getId() %>" data-caminho='{"base":"${pageContext.request.contextPath}","tabela":"empresas-crud"}'><img src="${pageContext.request.contextPath}/assets/crud/img/deletar-kronos.png" alt="Excluir"></button> </td>
                        <td><%= empresa.getId() %></td>
                        <td><%= empresa.getNome() %></td>
                        <td><%= empresa.getEmail() %></td>
                        <td><%= empresa.getPorte() %></td>
                        <td><%= nomePlano %></td>
                        <td><%= empresa.getCep() %></td>
                        <td><%= empresa.getCnpj() %></td>
                        <td><%= empresa.getTelefone() %></td>
                        <td><%= empresa.getHorarioAbertura() %></td>
                        <td><%= empresa.getHorarioFechamento() %></td>
                        <td><%= empresa.getRegraDeNegocios() %></td>
                    </tr>
                    <%
                        }
                    } else {
                    %>
                    <tr>
                        <td colspan="13">Nenhuma empresa encontrada.</td>
                    </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
        </main>
    </div>
</body>
</html>