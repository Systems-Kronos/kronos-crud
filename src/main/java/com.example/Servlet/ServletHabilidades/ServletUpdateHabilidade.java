package com.example.Servlet.ServletHabilidade;

import com.example.dao.HabilidadesDAO;
import com.example.Model.Habilidades;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

@WebServlet("/habilidade-update")
public class ServletUpdateHabilidade extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HabilidadesDAO dao = new HabilidadesDAO();
        List<Habilidades> listaHabilidades = null;
        String erro = null;

        try {
            listaHabilidades = dao.read();
            if (listaHabilidades == null) listaHabilidades = new ArrayList<>();
        } catch(Exception e){
            e.printStackTrace();
            erro = "Erro ao carregar lista.";
            listaHabilidades = new ArrayList<>();
        }
        request.setAttribute("listaHabilidades", listaHabilidades);

        String idParam = request.getParameter("id");
        Habilidades habilidadeModal = null;

        try {
            int id = Integer.parseInt(idParam);
            habilidadeModal = dao.read(id);
            if (habilidadeModal != null) {
                request.setAttribute("habilidadeModal", habilidadeModal);
                request.setAttribute("abrirModal", "update");
            } else {
                if (erro == null) erro = "Habilidade ID " + id + " não encontrada.";
            }
        } catch (NumberFormatException e) {
            erro = "ID inválido.";
            System.err.println("ID inválido ('id') update habilidade (doGet): " + idParam);
        } catch (Exception e) {
            e.printStackTrace();
            if (erro == null) erro = "Erro ao buscar dados.";
        }

        if (erro != null) {
            request.setAttribute("erro", erro);
        }
        request.getRequestDispatcher("/WEB-INF/pages/habilidades.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HabilidadesDAO dao = new HabilidadesDAO();
        int id = 0;
        boolean success = false;

        String idParam = request.getParameter("id");
        String nome = request.getParameter("nome");
        String tag = request.getParameter("tag");
        String descricao = request.getParameter("descricao");

        try {
            id = Integer.parseInt(idParam);

            Habilidades habilidadeParaAtualizar = dao.read(id);
            if (habilidadeParaAtualizar == null) {
                throw new Exception("Habilidade ID " + id + " não encontrada.");
            }

            habilidadeParaAtualizar.setNome(nome);
            habilidadeParaAtualizar.setTag(tag);
            habilidadeParaAtualizar.setDescricao(descricao);

            int resultado = dao.update(habilidadeParaAtualizar);

            if (resultado > 0) {
                success = true;
            } else {
                request.setAttribute("erro", "Não foi possível atualizar (ID: " + id + ").");
            }

        } catch (IllegalArgumentException | NullPointerException | IllegalStateException  e) {
            request.setAttribute("erro", "Erro: " + e.getMessage());
            request.setAttribute("nome_previo", nome);
            request.setAttribute("tag_previo", tag);
            request.setAttribute("descricao_previo", descricao);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro inesperado: " + e.getMessage());
        }

        if (success) {
            response.sendRedirect(request.getContextPath() + "/habilidades-crud");
        } else {
            System.err.println("Falha update habilidade ID " + id + ". Forwarding.");
            List<Habilidades> listaHabilidades = null;
            try { listaHabilidades = dao.read(); } catch (Exception readEx){ listaHabilidades = new ArrayList<>(); }
            request.setAttribute("listaHabilidades", listaHabilidades);
            if (id > 0 && request.getAttribute("habilidadeModal") == null) {
                try { request.setAttribute("habilidadeModal", dao.read(id)); } catch (Exception readEx) { /* Ignora */ }
            }
            request.setAttribute("abrirModal", "update");
            request.getRequestDispatcher("/WEB-INF/pages/habilidades.jsp").forward(request, response);
        }
    }
}