
package com.example.Servlet;

import com.example.Model.Administracao;
import com.example.dao.AdministracaoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/login-crud")
public class ServletLogin extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String senha = request.getParameter("senha");

        AdministracaoDAO dao = new AdministracaoDAO();
        Administracao admin = dao.read(email, senha);

        if (admin != null) {
            // Redireciona para o Servlet que carrega a lista de administradores
            response.sendRedirect(request.getContextPath() + "/admin-crud");
        } else {
            request.setAttribute("erro", "Email ou senha incorretos");
            request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
        }

    }
}
