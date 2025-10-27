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
import java.util.ArrayList;

@WebServlet("/setores-delete")
public class ServletDeleteSetores extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        SetorDAO dao = new SetorDAO();
        List<Setor> listaSetores = null;
        String erro = null;

        try {
            listaSetores = dao.read();
            if (listaSetores == null) listaSetores = new ArrayList<>();
        } catch(Exception e){
            e.printStackTrace();
            erro = "Erro ao carregar lista de setores.";
            listaSetores = new ArrayList<>();
        }
        request.setAttribute("listaSetores", listaSetores);

        String idParam = request.getParameter("id");
        Setor setorModal = null;

        try {
            int id = Integer.parseInt(idParam);
            setorModal = dao.read(id);
            if (setorModal != null) {
                request.setAttribute("setorModal", setorModal);
                request.setAttribute("abrirModal", "delete");
            } else {
                if (erro == null) erro = "Setor ID " + id + " não encontrado.";
            }
        } catch (NumberFormatException e) {
            erro = "ID inválido fornecido.";
            System.err.println("ID inválido ('id') delete setor (doGet): " + idParam);
        } catch (Exception e) {
            e.printStackTrace();
            if (erro == null) erro = "Erro ao buscar dados do setor.";
        }

        if (erro != null) {
            request.setAttribute("erro", erro);
        }
        request.getRequestDispatcher("/WEB-INF/pages/setores.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        SetorDAO dao = new SetorDAO();
        int id = 0;
        boolean success = false;

        try {
            String idParam = request.getParameter("id");
            id = Integer.parseInt(idParam);
            int resultado = dao.delete(id);
            if (resultado > 0) {
                success = true;
            } else {
                request.setAttribute("erro", "Não foi possível deletar o setor (ID: " + id + "). Verifique dependências.");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("erro", "ID inválido fornecido.");
            System.err.println("ID inválido ('id') delete setor doPost: " + request.getParameter("id"));
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro inesperado: " + e.getMessage());
        }

        if (success) {
            response.sendRedirect(request.getContextPath() + "/setores-crud");
        } else {
            System.err.println("Falha delete setor ID " + id + ". Forwarding.");
            List<Setor> listaSetores = null;
            try { listaSetores = dao.read(); } catch (Exception readEx){ listaSetores = new ArrayList<>(); }
            request.setAttribute("listaSetores", listaSetores);
            if (id > 0 && request.getAttribute("setorModal") == null) {
                try { request.setAttribute("setorModal", dao.read(id)); } catch (Exception readEx) { /* Ignora */ }
            }
            request.setAttribute("abrirModal", "delete");
            request.getRequestDispatcher("/WEB-INF/pages/setores.jsp").forward(request, response);
        }
    }
}