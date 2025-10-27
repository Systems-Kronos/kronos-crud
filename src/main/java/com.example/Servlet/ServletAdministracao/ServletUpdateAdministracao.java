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

@WebServlet("/admin-update")
public class ServletUpdateAdministracao extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        AdministracaoDAO dao = new AdministracaoDAO();
        List<Administracao> listaAdmins = dao.read();
        request.setAttribute("listaAdmins", listaAdmins);


        String idParam = request.getParameter("id");
        Administracao adminModal = null;

        try {
            int id = Integer.parseInt(idParam);
            adminModal = dao.read(id);
            if (adminModal != null) {
                request.setAttribute("adminModal", adminModal);
                request.setAttribute("abrirModal", "update");
            } else {
                request.setAttribute("erro", "Admin ID " + id + " não encontrado (doGet).");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("erro", "ID inválido fornecido (doGet).");
            System.err.println("ID inválido ('id') em admin doGet: " + idParam); // Updated message
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro ao buscar dados admin (doGet).");
        }
        // Ensure path is correct
        request.getRequestDispatcher("/WEB-INF/pages/administrador.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        AdministracaoDAO dao = new AdministracaoDAO();
        int id = 0;
        String nome = null;
        String email = null;
        String novaSenha = null;
        boolean sucessoNaOperacao = false;

        try {
            // Integer.parseInt pode lançar NumberFormatException (que é um IllegalArgumentException)
            id = Integer.parseInt(request.getParameter("id"));
            nome = request.getParameter("nome");
            email = request.getParameter("email");
            novaSenha = request.getParameter("senha");

            Administracao adminParaAtualizar = dao.read(id);
            if (adminParaAtualizar == null) {
                throw new Exception("Administrador ID " + id + " não encontrado para atualizar.");
            }

            // Setters podem lançar IllegalArgumentException, NullPointerException, IllegalStateException
            adminParaAtualizar.setNome(nome);
            adminParaAtualizar.setEmail(email);

            if (novaSenha != null && !novaSenha.trim().isEmpty()) {
                adminParaAtualizar.setSenha(novaSenha); // Lembre-se de criptografar
            }

            int resultado = dao.update(adminParaAtualizar);

            if (resultado > 0) {
                sucessoNaOperacao = true;
            } else {
                request.setAttribute("erro", "Não foi possível atualizar o administrador (ID: " + id + ").");
            }

        } catch (IllegalArgumentException | NullPointerException | IllegalStateException e) {
            // --- CORREÇÃO AQUI ---
            // NumberFormatException FOI REMOVIDO da lista.
            // IllegalArgumentException já o captura.
            request.setAttribute("erro", "Erro de validação ou formato inválido: " + e.getMessage());
            request.setAttribute("nome_previo", nome); // Usa as variáveis locais
            request.setAttribute("email_previo", email);

        } catch (Exception e) { // Outros erros
            e.printStackTrace();
            request.setAttribute("erro", "Erro inesperado ao atualizar: " + e.getMessage());
        }

        // --- Fluxo de Resposta ---
        if (sucessoNaOperacao) {
            response.sendRedirect(request.getContextPath() + "/admin-crud");
        } else {
            System.err.println("Falha ao atualizar admin ID " + id + ". Fazendo forward.");
            List<Administracao> listaAdmins = dao.read();
            request.setAttribute("listaAdmins", listaAdmins);
            if (id > 0 && request.getAttribute("adminModal") == null) {
                try { request.setAttribute("adminModal", dao.read(id)); } catch (Exception readEx) { /* Ignora */ }
            }
            request.setAttribute("abrirModal", "update");
            request.getRequestDispatcher("/WEB-INF/pages/administrador.jsp").forward(request, response);
        }
    }
}