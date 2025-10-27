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

/**
 * Servlet focado SOMENTE em CRIAR (Create) um novo Administrador.
 */
@WebServlet("/admin-create")
public class ServletCreateAdministracao extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // Pegar parâmetros do formulário
        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha"); // Lembre-se de CRIPTOGRAFAR!

        AdministracaoDAO dao = new AdministracaoDAO();
        boolean success = false; // Flag para controlar redirect vs forward

        try {
            // Criar objeto (dispara validações do Model)
            // Seu Model lança IllegalArgumentException ou NullPointerException
            Administracao novoAdmin = new Administracao(nome, email, senha);

            // Inserir no banco
            // !!! IMPORTANTE: CRIPTOGRAFE a senha ANTES de chamar dao.create !!!
            // Exemplo: String senhaHash = BCrypt.hashpw(senha, BCrypt.gensalt());
            //          novoAdmin.setSenha(senhaHash); // Ou passe o hash no construtor
            success = dao.create(novoAdmin);

            if (success) {
                System.out.println("Administrador criado com sucesso!");
                // SUCESSO: Redireciona para a lista (PRG)
                response.sendRedirect(request.getContextPath() + "/admin-crud"); // URL da listagem
                return; // IMPORTANTE: Encerra aqui após redirect
            } else {
                // Falha no DAO (ex: email duplicado se for UNIQUE no DB)
                request.setAttribute("erro", "Erro ao cadastrar administrador. Verifique se o e-mail já existe.");
            }

        } catch (IllegalArgumentException | NullPointerException e) {
            // Captura erros de VALIDAÇÃO do Model
            request.setAttribute("erro", "Erro de validação: " + e.getMessage());
            // Guarda dados para repopular (exceto senha)
            request.setAttribute("nome_previo", nome);
            request.setAttribute("email_previo", email);

        } catch (Exception e) { // Captura outros erros inesperados
            e.printStackTrace();
            request.setAttribute("erro", "Erro inesperado ao criar administrador: " + e.getMessage());
        }

        // --- PLANO B (Se deu erro no try OU o dao.create falhou) ---
        // Se 'success' for false, faz forward de volta para o JSP com erro

        System.err.println("Falha na criação do admin. Fazendo forward para o JSP.");

        // Recarrega lista para a tabela de fundo do JSP
        List<Administracao> listaAdmins = dao.read();
        request.setAttribute("listaAdmins", listaAdmins);

        // Avisa o JSP para reabrir o modal de CREATE
        request.setAttribute("abrirModal", "create");

        // Encaminha (forward) com erro e dados prévios
        request.getRequestDispatcher("/WEB-INF/pages/administrador.jsp").forward(request, response);
    }
}