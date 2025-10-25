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
public class ServletReadPlano extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Instancia DAO
        PlanoDAO dao = new PlanoDAO();

        // Pega todos os planos do banco
        List<Plano> listaPlanos = dao.read();

        // Passa para o JSP
        request.setAttribute("listaPlanos", listaPlanos);

        // Encaminha para o JSP dentro do WEB-INF
        request.getRequestDispatcher("/WEB-INF/pages/planos.jsp").forward(request, response);
    }
}
