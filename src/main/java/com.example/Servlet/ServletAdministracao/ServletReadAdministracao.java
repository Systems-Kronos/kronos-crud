
package com.example.Servlet.ServletAdministracao;

import com.example.dao.AdministracaoDAO;
import com.example.Model.Administracao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin-crud")
public class ServletReadAdministracao extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Instancia DAO
        AdministracaoDAO dao = new AdministracaoDAO();

        // Pega todos os administradores do banco
        List<Administracao> listaAdmins = dao.read();

        // Passa para o JSP
        request.setAttribute("listaAdmins", listaAdmins);

        // Encaminha para JSP dentro do WEB-INF
        request.getRequestDispatcher("/WEB-INF/administrador.jsp").forward(request, response);
    }
}
