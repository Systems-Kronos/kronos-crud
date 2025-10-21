package com.example.Servlet.ServletSetores;

import com.example.Model.Setor;
import com.example.dao.SetorDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/setores-crud")
public class ServletReadSetores extends HttpServlet {

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

        // Cria DAO e busca setores com filtro e ordenação
        SetorDAO dao = new SetorDAO();
        List<Setor> listaSetores = dao.read(pesquisa, "nome", direction);

        // Atribui ao request para o JSP acessar
        request.setAttribute("listaSetores", listaSetores);

        // Encaminha para o JSP dentro do WEB-INF
        request.getRequestDispatcher("/WEB-INF/setores.jsp").forward(request, response);
    }
}
