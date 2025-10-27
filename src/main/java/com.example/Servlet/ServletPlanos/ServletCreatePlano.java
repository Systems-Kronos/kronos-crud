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

@WebServlet("/plano-create") // Action do form Create
public class ServletCreatePlano extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // Pegar parâmetros
        String nome = request.getParameter("nome");
        String custoStr = request.getParameter("custo"); // JSP usa 'custo'
        String maxFuncionariosStr = request.getParameter("maxFuncionarios");
        String descricao = request.getParameter("descricao");

        PlanoDAO dao = new PlanoDAO();
        boolean success = false;

        try {
            // Converter números
            float custo = Float.parseFloat(custoStr);
            int maxFuncionarios = Integer.parseInt(maxFuncionariosStr);

            // Criar objeto (validações do Model ocorrem aqui)
            Plano novoPlano = new Plano(nome, custo, descricao, maxFuncionarios);

            // Inserir no banco
            success = dao.create(novoPlano);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/planos-crud"); // Redirect sucesso
                return; // Encerra
            } else {
                request.setAttribute("erro", "Erro ao cadastrar plano no banco.");
            }

        } catch (IllegalArgumentException | NullPointerException | IllegalStateException  e) {
            // Erro de validação ou formato
            request.setAttribute("erro", "Erro: " + e.getMessage());
            // Guarda dados para repopular
            request.setAttribute("nome_previo", nome);
            request.setAttribute("custo_previo", custoStr);
            request.setAttribute("maxFuncionarios_previo", maxFuncionariosStr);
            request.setAttribute("descricao_previo", descricao);

        } catch (Exception e) { // Outros erros
            e.printStackTrace();
            request.setAttribute("erro", "Erro inesperado: " + e.getMessage());
        }

        // --- Se deu erro ---
        if (!success) {
            System.err.println("Falha na criação do plano. Forwarding.");
            List<Plano> listaPlanos = null;
            try { listaPlanos = dao.read(); } catch (Exception readEx){ listaPlanos = new ArrayList<>(); }
            request.setAttribute("listaPlanos", listaPlanos); // Recarrega lista
            request.setAttribute("abrirModal", "create"); // Avisa para reabrir
            request.getRequestDispatcher("/WEB-INF/pages/planos.jsp").forward(request, response); // Forward JSP
        }
    }
}