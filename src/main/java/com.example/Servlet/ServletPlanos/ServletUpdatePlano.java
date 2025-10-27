package com.example.Servlet.ServletPlanos; // Verify package

import com.example.dao.PlanoDAO;     // Verify import
import com.example.Model.Plano;      // Verify import
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

@WebServlet("/planos-update")
public class ServletUpdatePlano extends HttpServlet {

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
            erro = "Erro ao carregar lista de planos.";
            listaPlanos = new ArrayList<>();
        }
        request.setAttribute("listaPlanos", listaPlanos);

        String idParam = request.getParameter("id"); // Use "id"
        Plano planoModal = null;

        try {
            int id = Integer.parseInt(idParam);
            planoModal = dao.read(id);
            if (planoModal != null) {
                request.setAttribute("planoModal", planoModal);
                request.setAttribute("abrirModal", "update");
            } else {
                if (erro == null) erro = "Plano ID " + id + " não encontrado.";
            }
        } catch (NumberFormatException e) {
            erro = "ID inválido fornecido.";
            System.err.println("ID inválido ('id') update plano (doGet): " + idParam);
        } catch (Exception e) {
            e.printStackTrace();
            if (erro == null) erro = "Erro ao buscar dados do plano.";
        }

        if (erro != null) {
            request.setAttribute("erro", erro);
        }
        request.getRequestDispatcher("/WEB-INF/pages/planos.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        PlanoDAO dao = new PlanoDAO();
        int id = 0;
        boolean success = false;

        String idParam = request.getParameter("id"); // Get hidden ID
        String nome = request.getParameter("nome");
        String custoStr = request.getParameter("custo"); // Matches JSP name="custo"
        String maxFuncionariosStr = request.getParameter("maxFuncionarios");
        String descricao = request.getParameter("descricao");

        try {
            id = Integer.parseInt(idParam);
            float custo = Float.parseFloat(custoStr);
            int maxFuncionarios = Integer.parseInt(maxFuncionariosStr);

            Plano planoParaAtualizar = dao.read(id);
            if (planoParaAtualizar == null) {
                throw new Exception("Plano ID " + id + " não encontrado.");
            }

            planoParaAtualizar.setNome(nome);
            planoParaAtualizar.setCusto(custo); // Model uses setCusto
            planoParaAtualizar.setDescricao(descricao);
            planoParaAtualizar.setMaxFuncionarios(maxFuncionarios);

            int resultado = dao.update(planoParaAtualizar);

            if (resultado > 0) {
                success = true;
            } else {
                request.setAttribute("erro", "Não foi possível atualizar (ID: " + id + ").");
            }

        } catch (IllegalArgumentException | NullPointerException | IllegalStateException e) {
            request.setAttribute("erro", "Erro: " + e.getMessage());
            request.setAttribute("nome_previo", nome);
            request.setAttribute("custo_previo", custoStr); // Keep String value for repopulation
            request.setAttribute("maxFuncionarios_previo", maxFuncionariosStr);
            request.setAttribute("descricao_previo", descricao);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro inesperado: " + e.getMessage());
        }

        if (success) {
            response.sendRedirect(request.getContextPath() + "/planos-crud"); // Redirect to plan list
        } else {
            System.err.println("Falha update plano ID " + id + ". Forwarding.");
            List<Plano> listaPlanos = null;
            try { listaPlanos = dao.read(); } catch (Exception readEx){ listaPlanos = new ArrayList<>(); }
            request.setAttribute("listaPlanos", listaPlanos);
            if (id > 0 && request.getAttribute("planoModal") == null) {
                try { request.setAttribute("planoModal", dao.read(id)); } catch (Exception readEx) { /* Ignore */ }
            }
            request.setAttribute("abrirModal", "update");
            request.getRequestDispatcher("/WEB-INF/pages/planos.jsp").forward(request, response); // Forward to plan JSP
        }
    }
}