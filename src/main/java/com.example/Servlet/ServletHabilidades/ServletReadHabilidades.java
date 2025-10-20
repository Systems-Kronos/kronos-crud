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

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Instancia DAO
        HabilidadesDAO dao = new HabilidadesDAO();

        // Pega todas as habilidades do banco
        List<Habilidades> listaHabilidades = dao.read();

        // Passa para o JSP
        request.setAttribute("listaHabilidades", listaHabilidades);

        // Encaminha para o JSP dentro do WEB-INF
        request.getRequestDispatcher("/WEB-INF/habilidades.jsp").forward(request, response);
    }
}
