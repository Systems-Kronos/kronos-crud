package com.example.Servlet.ServletPlanos;

import com.example.dao.PlanoDAO;
import com.example.Model.Plano;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/delete-plano")
public class ServletDeletePlano extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PlanoDAO dao = new PlanoDAO();

        // Carrega lista de planos
        List<Plano> listaPlanos = dao.read();
        request.setAttribute("listaPlanos", listaPlanos);

        // Captura parâmetros
        String acao = request.getParameter("acao");
        String idParam = request.getParameter("pk");

        // Se a ação for delete e tiver ID
        if ("delete".equals(acao) && idParam != null && !idParam.isEmpty()) {
            try {
                int id = Integer.parseInt(idParam);
                Plano planoModal = dao.read(id);

                if (planoModal != null) {
                    request.setAttribute("planoModal", planoModal);
                    request.setAttribute("pk", id);
                    request.setAttribute("acao", "delete");

                    // ⚡ Força abertura automática do modal
                    request.setAttribute("deleteAberto", true);

                } else {
                    request.setAttribute("erro", "Plano não encontrado.");
                }

            } catch (NumberFormatException e) {
                request.setAttribute("erro", "ID inválido para exclusão.");
            }
        }

        // Encaminha para o JSP
        request.getRequestDispatcher("/WEB-INF/pages/planos.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PlanoDAO dao = new PlanoDAO();
        String idParam = request.getParameter("pk");

        if (idParam != null && !idParam.isEmpty()) {
            try {
                int id = Integer.parseInt(idParam);
                int resultado = dao.delete(id);

                if (resultado > 0) {
                    System.out.println("Plano ID " + id + " deletado com sucesso.");
                    response.sendRedirect(request.getContextPath() + "/planos-crud");
                    return;
                } else {
                    request.setAttribute("erro", "Erro ao deletar plano ID: " + id);
                }

            } catch (NumberFormatException e) {
                request.setAttribute("erro", "ID inválido.");
            }
        } else {
            request.setAttribute("erro", "Nenhum ID informado para exclusão.");
        }

        // Recarrega lista e mantém modal aberto se falhar
        List<Plano> listaPlanos = dao.read();
        request.setAttribute("listaPlanos", listaPlanos);
        request.setAttribute("acao", "delete");
        request.setAttribute("deleteAberto", true);
        request.getRequestDispatcher("/WEB-INF/pages/planos.jsp").forward(request, response);
    }
}
