package com.example.Servlet.ServletHabilidades;

import java.io.IOException;
import java.util.List;

import com.example.Model.Habilidades;
import com.example.dao.HabilidadesDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/habilidades-crud")
public class ServletReadHabilidades extends HttpServlet {
    
    // Instancia DAO
    private HabilidadesDAO dao = new HabilidadesDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pk = request.getParameter("pk");

        if (pk != null && !pk.isEmpty()) {

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            try {
                Habilidades habilidade = dao.read(Integer.parseInt(pk));

                if (habilidade != null) {

                    String json = "{"
                            + "\"id\":\"" + pk + "\","
                            + "\"nome\":\"" + habilidade.getNome() + "\","
                            + "\"tag\":\"" + habilidade.getTag() + "\","
                            + "\"descricao\":\"" + habilidade.getDescricao() + "\""
                            + "}";

                    response.getWriter().write(json);
                }
            } catch (NumberFormatException e) {
                response.getWriter().write("{\"erro\":\"PK inválida\"}");
            } catch (Exception e) {
                response.getWriter().write("{\"erro\":\"" + e.getMessage() + "\"}");
            }

        } else {

            // Pega todas as habilidades do banco
            List<Habilidades> listaHabilidades = dao.read();

            // Passa para o JSP
            request.setAttribute("listaHabilidades", listaHabilidades);

            // Encaminha para o JSP dentro do WEB-INF
            request.getRequestDispatcher("/WEB-INF/pages/habilidades.jsp").forward(request, response);
        }
    }
}
