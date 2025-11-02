package com.example.Servlet.ServletLogin;

import com.example.Model.Administracao;
import com.example.dao.AdministracaoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet responsável por controlar o fluxo de Login (autenticação).
 * doGet: Apenas exibe a página de login.
 * doPost: Processa a tentativa de autenticação.
 */
@WebServlet("/login-crud")
public class ServletLogin extends HttpServlet {

    /*
     * Exibe a página de login (login.jsp).
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
    }

    /*
     * Processa a tentativa de autenticação (email e senha) vinda do formulário.
     * Trata SQLException e cria HttpSession.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String senha = request.getParameter("senha");

        AdministracaoDAO dao = new AdministracaoDAO();
        String erro = null;
        boolean loginSuccess = false;
        Administracao admin = null;

        try {
            admin = dao.read(email, senha);

            if (admin != null) {
                // SUCESSO: Login válido
                loginSuccess = true;

                HttpSession session = request.getSession(); // Obtém ou cria uma sessão
                session.setAttribute("adminLogado", admin); // Armazena o objeto na sessão
                session.setMaxInactiveInterval(30 * 60); // Define timeout (ex: 30 min)

            } else {
                erro = "Email ou senha incorretos";
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Loga o erro no console do servidor
            erro = "Erro de banco de dados. Tente novamente mais tarde.";

        } catch (Exception e) {
            // Captura outros erros inesperados
            e.printStackTrace();
            erro = "Erro inesperado ao processar o login: " + e.getMessage();
        }

        // Fluxo de Resposta
        if (loginSuccess) {
            // Redireciona (PRG) para o painel de admin
            System.out.println("Login (INSEGURO) bem-sucedido para: " + email);
            response.sendRedirect(request.getContextPath() + "/admin-crud");
        } else {
            // Faz forward de volta para o JSP de login com a mensagem de erro
            System.err.println("Falha no login para: " + email + ". Erro: " + erro);
            request.setAttribute("erro", erro);
            request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
        }
    }
}