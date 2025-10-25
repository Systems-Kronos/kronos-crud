package com.example.Servlet.ServletSetores;

import com.example.dao.SetorDAO;
import com.example.Model.Setor;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/setores-delete")
public class ServletDeleteSetores extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        SetorDAO dao = new SetorDAO();

        // Carrega lista completa
        List<Setor> listaSetores = dao.read();
        request.setAttribute("listaSetores", listaSetores);

        // Pega parâmetros
        String acao = request.getParameter("acao");
        String idParam = request.getParameter("pk");

        if ("delete".equals(acao) && idParam != null) {
            try {
                int id = Integer.parseInt(idParam);
                Setor setorModal = dao.read(id);

                if (setorModal != null) {
                    request.setAttribute("setorModal", setorModal);
                    request.setAttribute("acao", "delete");
                    request.setAttribute("pk", id);
                } else {
                    request.setAttribute("erro", "Setor não encontrado.");
                }

            } catch (NumberFormatException e) {
                request.setAttribute("erro", "ID inválido para exclusão.");
            }
        }

        request.getRequestDispatcher("/WEB-INF/pages/setores.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        SetorDAO dao = new SetorDAO();
        String idParam = request.getParameter("pk");

        if (idParam != null && !idParam.isEmpty()) {
            try {
                int id = Integer.parseInt(idParam);
                int resultado = dao.delete(id);

                if (resultado > 0) {
                    System.out.println("Setor ID " + id + " deletado com sucesso.");
                    response.sendRedirect(request.getContextPath() + "/setores-crud");
                    return;
                } else {
                    request.setAttribute("erro", "Erro ao deletar setor ID: " + id);
                }

            } catch (NumberFormatException e) {
                request.setAttribute("erro", "ID inválido.");
            }
        } else {
            request.setAttribute("erro", "Nenhum ID informado para exclusão.");
        }

        // Recarrega lista e mantém modal aberto se falhar
        List<Setor> listaSetores = dao.read();
        request.setAttribute("listaSetores", listaSetores);
        request.setAttribute("acao", "delete");
        request.getRequestDispatcher("/WEB-INF/pages/setores.jsp").forward(request, response);
    }
}
