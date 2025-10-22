package com.example.Servlet.ServletHabilidades;

import com.example.dao.HabilidadesDAO;
import com.example.Model.Habilidades;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/habilidade-create")
public class ServletCreateHabilidades extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Receber os parâmetros do formulário
        String nome = request.getParameter("nome");
        String tag = request.getParameter("tag");
        String descricao = request.getParameter("descricao");

        // 2. Instanciar o DAO
        HabilidadesDAO dao = new HabilidadesDAO();

        try {
            // 3. Criar o objeto Habilidades (as validações do Model serão lançadas se houver erro)
            Habilidades novaHabilidade = new Habilidades(nome, tag, descricao);

            // 4. Inserir no banco de dados
            boolean sucesso = dao.create(novaHabilidade);

            if (sucesso) {
                System.out.println("Habilidade criada com sucesso!");
            } else {
                request.setAttribute("erro", "Erro ao cadastrar habilidade no banco de dados.");
            }

        } catch (IllegalArgumentException | NullPointerException e) {
            // Captura erros de validação do Model
            request.setAttribute("erro", "Erro de validação: " + e.getMessage());
            request.setAttribute("nome_previo", nome);
            request.setAttribute("tag_previa", tag);
            request.setAttribute("descricao_previa", descricao);
        }

        // 5. Redirecionar para o CRUD de habilidades
        response.sendRedirect(request.getContextPath() + "/habilidades-crud");
    }
}
