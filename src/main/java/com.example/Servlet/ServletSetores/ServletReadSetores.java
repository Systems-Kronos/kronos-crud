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

        // Instancia DAO
        SetorDAO dao = new SetorDAO();

        // Pega todos os setores do banco
        List<Setor> listaSetores = dao.read();

        // Passa para o JSP
        request.setAttribute("listaSetores", listaSetores);

        // Encaminha para o JSP dentro do WEB-INF
        request.getRequestDispatcher("/WEB-INF/setores.jsp").forward(request, response);
    }
}
