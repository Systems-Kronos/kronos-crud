package com.example.Servlet.ServletPlanos;

import com.example.Model.Plano;
import com.example.dao.PlanoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/planos-crud")
public class ServletReadPlanos extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Pega o valor do campo de pesquisa
        String pesquisa = request.getParameter("pesquisa");

        // Pega a ordem (crescente ou decrescente)
        String ordem = request.getParameter("ordem"); // "crescente" ou "decrescente"

        // Define direção padrão
        String direction = "ASC";
        if (ordem != null && ordem.equalsIgnoreCase("decrescente")) {
            direction = "DESC";
        }

        // Cria DAO e busca planos com filtro e ordenação
        PlanoDAO dao = new PlanoDAO();
        List<Plano> listaPlanos = dao.read(pesquisa, "nome", direction);

        // Atribui ao request para o JSP acessar
        request.setAttribute("listaPlanos", listaPlanos);

        // Encaminha para o JSP dentro do WEB-INF
        request.getRequestDispatcher("/WEB-INF/planos.jsp").forward(request, response);
    }
}
