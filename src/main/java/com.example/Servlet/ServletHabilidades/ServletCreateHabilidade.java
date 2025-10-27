package com.example.Servlet.ServletHabilidades;

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

@WebServlet("/habilidade-create")
public class ServletCreateHabilidade extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String nome = request.getParameter("nome");
        String tag = request.getParameter("tag");
        String descricao = request.getParameter("descricao");

        HabilidadesDAO dao = new HabilidadesDAO();
        boolean success = false;

        try {
            // Assumes Habilidades constructor/setters validate and throw exceptions
            Habilidades novaHabilidade = new Habilidades(nome, tag, descricao);
            success = dao.create(novaHabilidade);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/habilidades-crud");
                return;
            } else {
                request.setAttribute("erro", "Erro ao cadastrar habilidade.");
            }

        } catch (IllegalArgumentException | NullPointerException e) {
            request.setAttribute("erro", "Erro: " + e.getMessage());
            request.setAttribute("nome_previo", nome);
            request.setAttribute("tag_previo", tag);
            request.setAttribute("descricao_previo", descricao);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro inesperado: " + e.getMessage());
        }

        if (!success) {
            System.err.println("Falha na criação da habilidade. Forwarding.");
            List<Habilidades> listaHabilidades = null;
            try {
                listaHabilidades = dao.read();
                if (listaHabilidades == null) listaHabilidades = new ArrayList<>();
            } catch (Exception readEx) {
                listaHabilidades = new ArrayList<>();
            }
            request.setAttribute("listaHabilidades", listaHabilidades);
            request.setAttribute("abrirModal", "create");
            request.getRequestDispatcher("/WEB-INF/pages/habilidades.jsp").forward(request, response);
        }
    }
}