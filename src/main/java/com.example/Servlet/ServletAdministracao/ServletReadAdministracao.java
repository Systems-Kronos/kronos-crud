
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

        String pk = request.getParameter("pk");

        if (pk != null && !pk.isEmpty()) {

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            try {
                Administracao admin = dao.buscarPorId(Integer.parseInt(pk));

                if (admin != null) {
                    // JSON manual sem dependência externa
                    String json = "{"
                            + "\"id\":\"" + admin.getId() + "\","
                            + "\"nome\":\"" + admin.getNome() + "\","
                            + "\"email\":\"" + admin.getEmail() + "\","
                            + "\"senha\":\"" + admin.getSenha() + "\""
                            + "}";
                    response.getWriter().write(json);
                } else {
                    response.getWriter().write("{\"erro\":\"Admin não encontrado\"}");
                }

            } catch (NumberFormatException e) {
                response.getWriter().write("{\"erro\":\"PK inválida\"}");
            } catch (Exception e) {
                response.getWriter().write("{\"erro\":\"" + e.getMessage() + "\"}");
            }

        } else {

            // Pega todos os administradores do banco
            List<Administracao> listaAdmins = dao.read();

            // Passa para o JSP
            request.setAttribute("listaAdmins", listaAdmins);

            // Encaminha para JSP dentro do WEB-INF
            request.getRequestDispatcher("/WEB-INF/pages/administrador.jsp").forward(request, response);
        }
    }
}
