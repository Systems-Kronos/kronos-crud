package com.example.Servlet.ServletAdministracao;

import com.example.dao.AdministracaoDAO;
import com.example.Model.Administracao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin-create")
public class ServletCreateAdministracao extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Receber os parâmetros do formulário
        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");

        // 2. Instanciar o DAO
        AdministracaoDAO dao = new AdministracaoDAO();

        try {
            // 3. Criar o objeto Administracao. As validações de Model (como campos obrigatórios e formato)
            // serão lançadas como exceções aqui, se falharem.
            Administracao novoAdmin = new Administracao(nome, email, senha);

            // 4. Inserir no banco de dados
            boolean sucesso = dao.create(novoAdmin);

            if (sucesso) {
                // Sucesso: Pode adicionar uma mensagem de sucesso no request, se desejar.
                System.out.println("Administrador criado com sucesso!");
            } else {
                // Falha na inserção no DAO (ex: erro de SQL)
                request.setAttribute("erro", "Erro ao cadastrar administrador no banco de dados.");
            }

        } catch (IllegalArgumentException | NullPointerException e) {
            // Captura exceções do Model (validação de dados)
            request.setAttribute("erro", "Erro de validação: " + e.getMessage());
            // Opcional: manter os dados preenchidos no formulário
            request.setAttribute("nome_previo", nome);
            request.setAttribute("email_previo", email);

            // Se houver erro de validação, encaminha de volta para o JSP para exibir o erro
            // Note que se você usar sendRedirect, a mensagem de erro será perdida,
            // então uma alternativa seria usar session ou reencaminhar o request.
        }

        // 5. Redirecionar de volta para a página de listagem (admin-crud) para exibir a lista atualizada
        // E limpar os parâmetros de pesquisa/ordem para garantir que a lista completa seja mostrada após o cadastro.
        response.sendRedirect(request.getContextPath() + "/admin-crud");
    }
}