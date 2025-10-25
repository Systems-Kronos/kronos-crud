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

@WebServlet("/habilidades-delete")
public class ServletDeleteHabilidade extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HabilidadesDAO dao = new HabilidadesDAO();
        List<Habilidades> listaHabilidades = dao.read();
        request.setAttribute("listaHabilidades", listaHabilidades);

        String acao = request.getParameter("acao");
        String idParam = request.getParameter("pk");

        if ("delete".equals(acao) && idParam != null) {
            try {
                int id = Integer.parseInt(idParam);
                Habilidades habilidade = dao.read(id);

                if (habilidade != null) {
                    request.setAttribute("habilidadeModal", habilidade);
                    request.setAttribute("deleteAberto", true);
                    request.setAttribute("pk", id);
                } else {
                    request.setAttribute("erro", "Habilidade não encontrada.");
                }
            } catch (NumberFormatException e) {
                request.setAttribute("erro", "ID inválido.");
            }
        }

        request.getRequestDispatcher("/WEB-INF/pages/habilidades.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("pk");
        HabilidadesDAO dao = new HabilidadesDAO();

        if (idParam != null && !idParam.isEmpty()) {
            try {
                int id = Integer.parseInt(idParam);
                int resultado = dao.delete(id);

                if (resultado > 0) {
                    response.sendRedirect(request.getContextPath() + "/habilidades-crud");
                    return;
                } else {
                    request.setAttribute("erro", "Erro ao deletar habilidade.");
                }
            } catch (NumberFormatException e) {
                request.setAttribute("erro", "ID inválido para exclusão.");
            }
        }

        // Se der erro, recarrega lista e mantém modal aberto
        List<Habilidades> listaHabilidades = dao.read();
        request.setAttribute("listaHabilidades", listaHabilidades);
        request.setAttribute("deleteAberto", true);
        request.getRequestDispatcher("/WEB-INF/pages/habilidades.jsp").forward(request, response);
    }
}
