package com.example.Servlet.ServletAdministracao;

import com.example.Model.Administracao;
import com.example.dao.AdministracaoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin-crud")
public class ServletReadAdministracao extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Pega o valor do campo de pesquisa
        String pesquisa = request.getParameter("pesquisa");

        // Pega a ordem (crescente ou decrescente)
        String ordem = request.getParameter("ordem"); // "crescente" ou "decrescente"

        // Define a direção padrão
        String direction = "ASC";
        if (ordem != null && ordem.equalsIgnoreCase("decrescente")) {
            direction = "DESC";
        }

        // Cria DAO e busca administradores com filtro
        AdministracaoDAO dao = new AdministracaoDAO();
        List<Administracao> listaAdmins = dao.read(pesquisa, "nome", direction);

        // Atribui ao request
        request.setAttribute("listaAdmins", listaAdmins);

        // Encaminha para o JSP
        request.getRequestDispatcher("/WEB-INF/administrador.jsp").forward(request, response);
    }
}
