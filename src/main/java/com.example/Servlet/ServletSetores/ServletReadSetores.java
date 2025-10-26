package com.example.Servlet.ServletSetores;

import com.example.Model.Plano;
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

    // Instancia DAO
    private SetorDAO dao = new SetorDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pk = request.getParameter("pk");

        if (pk != null && !pk.isEmpty()) {

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            try {
                Setor setor = dao.read(Integer.parseInt(pk));

                if (setor != null) {

                    String json = "{"
                            + "\"id\":\"" + pk + "\","
                            + "\"nome\":\"" + setor.getNome() + "\","
                            + "\"qtnFuncionarios\":\"" + setor.getQntFuncionarios() + "\","
                            + "\"turnos\":\"" + setor.getTurnos() + "\","
                            + "\"descricao\":\"" + setor.getDescricao() + "\","
                            + "\"idEmpresa\":\"" + setor.getIdEmpresa() + "\""
                            + "}";

                    response.getWriter().write(json);
                }
            } catch (NumberFormatException e) {
                response.getWriter().write("{\"erro\":\"PK inválida\"}");
            } catch (Exception e) {
                response.getWriter().write("{\"erro\":\"" + e.getMessage() + "\"}");
            }

        } else {
            // Pega todos os setores do banco
            List<Setor> listaSetores = dao.read();

            // Passa para o JSP
            request.setAttribute("listaSetores", listaSetores);

            // Encaminha para o JSP dentro do WEB-INF
            request.getRequestDispatcher("/WEB-INF/pages/setores.jsp").forward(request, response);
        }
    }
}
