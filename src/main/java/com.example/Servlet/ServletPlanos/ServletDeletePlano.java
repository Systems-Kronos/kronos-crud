package com.example.Servlet.ServletPlanos; // Verifique o pacote

import com.example.dao.PlanoDAO;     // Verifique o import
import com.example.Model.Plano;      // Verifique o import
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

@WebServlet("/planos-delete") // URL para delete
public class ServletDeletePlano extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PlanoDAO dao = new PlanoDAO();
        List<Plano> listaPlanos = null;
        String erro = null;

        try {
            listaPlanos = dao.read();
            if (listaPlanos == null) listaPlanos = new ArrayList<>();
        } catch(Exception e){
            e.printStackTrace();
            erro = "Erro ao carregar lista.";
            listaPlanos = new ArrayList<>();
        }
        request.setAttribute("listaPlanos", listaPlanos);

        String idParam = request.getParameter("id"); // Usando "id"
        Plano planoModal = null;

        try {
            int id = Integer.parseInt(idParam);
            planoModal = dao.read(id);
            if (planoModal != null) {
                request.setAttribute("planoModal", planoModal);
                request.setAttribute("abrirModal", "delete"); // Usa "abrirModal"
            } else {
                if (erro == null) erro = "Plano ID " + id + " não encontrado.";
            }
        } catch (NumberFormatException e) {
            erro = "ID inválido.";
            System.err.println("ID inválido ('id') delete plano (doGet): " + idParam);
        } catch (Exception e) {
            e.printStackTrace();
            if (erro == null) erro = "Erro ao buscar dados.";
        }

        if (erro != null) {
            request.setAttribute("erro", erro);
        }
        // Caminho JSP correto
        request.getRequestDispatcher("/WEB-INF/pages/planos.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PlanoDAO dao = new PlanoDAO();
        int id = 0;
        boolean success = false;

        try {
            String idParam = request.getParameter("id"); // Usando "id"
            id = Integer.parseInt(idParam);
            int resultado = dao.delete(id);
            if (resultado > 0) {
                success = true;
            } else {
                request.setAttribute("erro", "Não foi possível deletar (ID: " + id + ").");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("erro", "ID inválido.");
            System.err.println("ID inválido ('id') delete plano doPost: " + request.getParameter("id"));
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro inesperado: " + e.getMessage());
        }

        if (success) {
            response.sendRedirect(request.getContextPath() + "/planos-crud"); // Redirect lista
        } else {
            // FALHA: Forward com erro
            System.err.println("Falha delete plano ID " + id + ". Forwarding.");
            List<Plano> listaPlanos = null;
            try { listaPlanos = dao.read(); } catch (Exception readEx){ listaPlanos = new ArrayList<>(); }
            request.setAttribute("listaPlanos", listaPlanos); // Recarrega lista
            // Tenta recarregar modal
            if (id > 0 && request.getAttribute("planoModal") == null) {
                try { request.setAttribute("planoModal", dao.read(id)); } catch (Exception readEx) { /* Ignora */ }
            }
            request.setAttribute("abrirModal", "delete"); // Avisa para reabrir
            // Caminho JSP correto
            request.getRequestDispatcher("/WEB-INF/pages/planos.jsp").forward(request, response);
        }
    }
}