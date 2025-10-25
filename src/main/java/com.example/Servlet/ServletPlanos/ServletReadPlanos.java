package com.example.Servlet.ServletPlanos;

import java.io.IOException;
import java.util.List;

import com.example.Model.Plano;
import com.example.dao.PlanoDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/planos-crud")
public class ServletReadPlanos extends HttpServlet {
    
    // Instancia DAO
    private PlanoDAO dao = new PlanoDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pk = request.getParameter("pk");

        if (pk != null && !pk.isEmpty()) {

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            try {
                Plano plano = dao.read(Integer.parseInt(pk));

                if (plano != null) {

                    String json = "{"
                            + "\"id\":\"" + pk + "\","
                            + "\"nome\":\"" + plano.getNome() + "\","
                            + "\"custo\":\"" + plano.getCusto() + "\","
                            + "\"maxFuncionarios\":\"" + plano.getMaxFuncionarios() + "\","
                            + "\"descricao\":\"" + plano.getDescricao() + "\""
                            + "}";

                    response.getWriter().write(json);
                }
            } catch (NumberFormatException e) {
                response.getWriter().write("{\"erro\":\"PK inválida\"}");
            } catch (Exception e) {
                response.getWriter().write("{\"erro\":\"" + e.getMessage() + "\"}");
            }

        } else {

            // Pega todos os planos do banco
            List<Plano> listaPlanos = dao.read();

            // Passa para o JSP
            request.setAttribute("listaPlanos", listaPlanos);

            // Encaminha para o JSP dentro do WEB-INF
            request.getRequestDispatcher("/WEB-INF/pages/planos.jsp").forward(request, response);
        }
    }
}
