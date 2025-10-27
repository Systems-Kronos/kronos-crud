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

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        AdministracaoDAO dao = new AdministracaoDAO();

        // Busca a lista completa para a tabela de fundo
        List<Administracao> listaAdmins = dao.read();
        request.setAttribute("listaAdmins", listaAdmins);

        // Pega o ID da URL
        String idParam = request.getParameter("id"); // Usando "id"
        Administracao adminModal = null;

        try {
            int id = Integer.parseInt(idParam);
            adminModal = dao.read(id); // Busca admin específico

            if (adminModal != null) {
                request.setAttribute("adminModal", adminModal); // Envia objeto para o JSP
                request.setAttribute("abrirModal", "delete"); // Avisa o JSP para abrir o modal
            } else {
                request.setAttribute("erro", "Admin ID " + id + " não encontrado (doGet).");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("erro", "ID inválido fornecido (doGet).");
            System.err.println("ID inválido ('id') em admin doGet: " + idParam);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro ao buscar dados admin (doGet).");
        }
        // Encaminha para o JSP de Admin
        request.getRequestDispatcher("/WEB-INF/pages/administrador.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        AdministracaoDAO dao = new AdministracaoDAO();
        int id = 0;
        boolean success = false;

        try {
            // Pega o ID do campo oculto do formulário modal
            String idParam = request.getParameter("id");
            id = Integer.parseInt(idParam);

            // Executa a deleção
            int resultado = dao.delete(id);

            if (resultado > 0) {
                success = true;
            } else {
                // Falha no DAO (ex: ID não existe mais, restrição de FK)
                request.setAttribute("erro", "Não foi possível deletar o administrador (ID: " + id + "). Verifique dependências.");
            }

        } catch (NumberFormatException e) {
            request.setAttribute("erro", "ID inválido fornecido para exclusão.");
            System.err.println("ID inválido ('id') em admin doPost: " + request.getParameter("id"));
        } catch (Exception e) { // Captura outros erros
            e.printStackTrace();
            request.setAttribute("erro", "Erro inesperado ao processar a exclusão: " + e.getMessage());
        }

        // --- Fluxo de Resposta ---
        if (success) {
            // SUCESSO: Redireciona (PRG)
            response.sendRedirect(request.getContextPath() + "/admin-crud"); // URL da listagem
        } else {
            // FALHA: Faz forward com erro
            System.err.println("Falha ao deletar admin ID " + id + ". Fazendo forward.");

            // Recarrega dados necessários para o JSP
            List<Administracao> listaAdmins = dao.read();
            request.setAttribute("listaAdmins", listaAdmins);
            // Tenta recarregar modal com dados (se ID for válido)
            if (id > 0 && request.getAttribute("adminModal") == null) {
                try { request.setAttribute("adminModal", dao.read(id)); } catch (Exception readEx) { /* Ignora */ }
            }

            request.setAttribute("abrirModal", "delete"); // Avisa para reabrir modal
            request.getRequestDispatcher("/WEB-INF/pages/administrador.jsp").forward(request, response); // Caminho JSP Admin
        }
    }
}