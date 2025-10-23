package com.example.Servlet.ServletPlanos;

import com.example.Model.Plano;
import com.example.dao.PlanoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/create-plano")
public class ServletCreatePlano extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Configura o encoding pra aceitar acentuação
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            // Pega os parâmetros do formulário
            String nome = request.getParameter("nome");
            String descricao = request.getParameter("descricao");
            float custo = Float.parseFloat(request.getParameter("preco"));
            int maxFuncionarios = Integer.parseInt(request.getParameter("maxFuncionarios"));

            // Cria o objeto Plano
            Plano plano = new Plano(nome, custo, descricao, maxFuncionarios);

            // Chama o DAO
            PlanoDAO dao = new PlanoDAO();
            boolean sucesso = dao.create(plano);

            // Redireciona de volta à listagem de planos
            if (sucesso) {
                response.sendRedirect(request.getContextPath() + "/planos-crud");
            } else {
                response.sendRedirect(request.getContextPath() + "/erro.jsp");
            }

        } catch (Exception e) {
            System.err.println("Erro ao criar plano: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/erro.jsp");
        }
    }
}
