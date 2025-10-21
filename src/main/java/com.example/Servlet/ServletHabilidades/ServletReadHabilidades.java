package com.example.Servlet.ServletHabilidades;

import com.example.Model.Habilidades;
import com.example.dao.HabilidadesDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/habilidades-crud")
public class ServletReadHabilidades extends HttpServlet {

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

        // Cria DAO e busca habilidades com filtro e ordenação
        HabilidadesDAO dao = new HabilidadesDAO();
        List<Habilidades> listaHabilidades = dao.read(pesquisa, "nome", direction);

        // Atribui ao request para o JSP acessar
        request.setAttribute("listaHabilidades", listaHabilidades);

        // Encaminha para o JSP dentro do WEB-INF
        request.getRequestDispatcher("/WEB-INF/habilidades.jsp").forward(request, response);
    }
}
