package com.example.Servlet.ServletAdministracao;

import com.example.dao.AdministracaoDAO;
import com.example.Model.Administracao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet focado em CRIAR (Create) um novo Administrador.
 */
@WebServlet("/admin-create")
public class ServletCreateAdministracao extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Define o encoding para UTF-8 ANTES de ler parâmetros, para evitar problemas com acentos
        request.setCharacterEncoding("UTF-8");

        // Pegar parâmetros do formulário
        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");

        AdministracaoDAO dao = new AdministracaoDAO();
        boolean success = false; // Flag para controlar redirect (sucesso) vs forward (falha)
        String erro = null; // Variável para armazenar a mensagem de erro

        try {
            // Criar objeto (dispara validações do Model)
            // O Model deve lançar IllegalArgumentException ou NullPointerException se dados forem inválidos
            Administracao novoAdmin = new Administracao(nome, email, senha);

            // Inserir no banco
            success = dao.create(novoAdmin);

            if (success) {
                System.out.println("Administrador criado com sucesso!");
                // Redireciona para a lista (Padrão Post-Redirect-Get - PRG)
                // Isso evita o reenvio do formulário ao atualizar a página
                response.sendRedirect(request.getContextPath() + "/admin-crud");
                return; // Encerra a execução do método após um redirect
            } else {
                // Falha no DAO (ex: create retornou false sem lançar exceção)
                erro = "Erro ao cadastrar administrador. Verifique se o e-mail já existe.";
            }

        } catch (IllegalArgumentException | NullPointerException e) {
            // Captura erros de validação do Model (ex: campos nulos, email inválido)
            erro = "Erro de validação: " + e.getMessage();

        } catch (SQLException e) { // Captura específica para erros de SQL
            e.printStackTrace();
            // Tenta dar uma mensagem amigável para violação de constraint (e-mail duplicado)
            if (e.getMessage() != null && (e.getMessage().contains("Duplicate entry") || e.getMessage().contains("UNIQUE constraint failed"))) {
                erro = "Erro: O e-mail informado ('" + email + "') já está cadastrado.";
            } else {
                erro = "Erro de banco de dados ao criar: " + e.getMessage();
            }

        } catch (Exception e) { // Captura qualquer outro erro inesperado
            e.printStackTrace();
            erro = "Erro inesperado ao criar administrador: " + e.getMessage();
        }


        // Caminho de Falha (Se 'success' == false ou se uma Exceção foi capturada)
        // Se a criação falhou, não faz redirect, mas sim um forward
        // para a mesma página (JSP), exibe a mensagem de erro.

        System.err.println("Falha na criação do admin. Fazendo forward para o JSP. Erro: " + erro);

        // Define os atributos de erro e de repopulação do formulário
        request.setAttribute("erro", erro);
        // Guarda dados para repopular o formulário (user-friendly), exceto a senha
        request.setAttribute("nome_previo", nome);
        request.setAttribute("email_previo", email);

        // Recarrega a lista de administradores para exibir na tabela do JSP
        List<Administracao> listaAdmins;
        try {
            listaAdmins = dao.read();
        } catch (SQLException e) {
            e.printStackTrace(); // Loga o erro de leitura
            listaAdmins = new ArrayList<>(); // Usa uma lista vazia para não quebrar o JSP

            // Concatena o erro da leitura com o erro original da criação
            request.setAttribute("erro", erro + " | ERRO ADICIONAL: Falha ao recarregar a lista de administradores.");
        }
        request.setAttribute("listaAdmins", listaAdmins);

        // Avisa o JSP para reabrir o modal de create
        // Isso permite que o usuário veja o erro e os dados que preencheu
        request.setAttribute("abrirModal", "create");

        // Encaminha (forward) com erro e dados prévios
        request.getRequestDispatcher("/WEB-INF/pages/administrador.jsp").forward(request, response);
    }
}