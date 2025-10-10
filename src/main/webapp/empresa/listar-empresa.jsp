<%--
  Created by IntelliJ IDEA.
  User: matheusueno-ieg
  Date: 09/10/2025
  Time: 22:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.*, com.example.dao.AdministracaoDAO, com.example.Model.Administracao" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Lista de Administração</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 40px;
            background-color: #f5f6fa;
        }
        h1 {
            text-align: center;
            color: #2f3640;
        }
        table {
            width: 80%;
            margin: 0 auto;
            border-collapse: collapse;
            background: white;
            box-shadow: 0 2px 6px rgba(0,0,0,0.1);
        }
        th, td {
            padding: 12px;
            border: 1px solid #dcdde1;
            text-align: left;
        }
        th {
            background-color: #40739e;
            color: white;
        }
        tr:nth-child(even) {
            background-color: #f1f2f6;
        }
        .no-data {
            text-align: center;
            color: #718093;
            padding: 20px;
        }
    </style>
</head>
<body>

<h1>Lista de Administradores</h1>
<a href="administracao-inserir.html">Inserir novos admins</a>


<%
    AdministracaoDAO dao = new AdministracaoDAO();
    List<Administracao> lista = dao.read();
%>

<% if (lista == null || lista.isEmpty()) { %>
<div class="no-data">Nenhum administrador encontrado.</div>
<% } else { %>
<table>
    <thead>
    <tr>
        <th>ID</th>
        <th>Nome</th>
        <th>E-mail</th>
        <th>Código de Acesso</th>
    </tr>
    </thead>
    <tbody>
    <% for (Administracao admin : lista) { %>
    <tr>
        <td><%= admin.getId() %></td>
        <td><%= admin.getNome() %></td>
        <td><%= admin.getEmail() %></td>
        <td><%= admin.getCodigoAcesso() %></td>
    </tr>
    <% } %>
    </tbody>
</table>
<% } %>

</body>
</html>
