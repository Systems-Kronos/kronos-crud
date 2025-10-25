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

@WebServlet("/admin-delete")
public class ServletDeleteAdministracao extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        AdministracaoDAO dao = new AdministracaoDAO();

        // Sempre carrega lista para o READ
        List<Administracao> listaAdmins = dao.read();
        request.setAttribute("listaAdmins", listaAdmins);

        // Captura parâmetros da URL
        String acao = request.getParameter("acao");
        String idParam = request.getParameter("pk");

        if ("delete".equals(acao) && idParam != null) {
            try {
                int id = Integer.parseInt(idParam);
                Administracao adminModal = dao.read(id);

                if (adminModal != null) {
                    request.setAttribute("adminModal", adminModal);
                    request.setAttribute("acao", "delete");
                    request.setAttribute("pk", id);
                } else {
                    request.setAttribute("erro", "Administrador não encontrado.");
                }

            } catch (NumberFormatException e) {
                request.setAttribute("erro", "ID inválido para exclusão.");
            }
        }

        request.getRequestDispatcher("/WEB-INF/pages/administrador.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        AdministracaoDAO dao = new AdministracaoDAO();
        String idParam = request.getParameter("pk");

        if (idParam != null && !idParam.isEmpty()) {
            try {
                int id = Integer.parseInt(idParam);
                int resultado = dao.delete(id);

                if (resultado > 0) {
                    System.out.println("Administrador ID " + id + " deletado com sucesso.");
                    response.sendRedirect(request.getContextPath() + "/admin-crud");
                    return;
                } else {
                    request.setAttribute("erro", "Erro ao deletar administrador ID: " + id);
                }

            } catch (NumberFormatException e) {
                request.setAttribute("erro", "ID inválido.");
            }
        } else {
            request.setAttribute("erro", "Nenhum ID informado para exclusão.");
        }

        // Recarrega lista e mantém modal aberto se falhar
        List<Administracao> listaAdmins = dao.read();
        request.setAttribute("listaAdmins", listaAdmins);
        request.setAttribute("acao", "delete");
        request.getRequestDispatcher("/WEB-INF/pages/administrador.jsp").forward(request, response);
    }
}
