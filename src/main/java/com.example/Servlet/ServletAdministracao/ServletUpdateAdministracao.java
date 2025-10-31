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
 * Servlet focado em ATUALIZAR (Update) um Administrador.
 * Usa doGet para carregar o modal com dados existentes e doPost para executar a atualização.
 */
@WebServlet("/admin-update")
public class ServletUpdateAdministracao extends HttpServlet {

    /*
     * Prepara a página para a edição.
     * Carrega a lista completa e, com o ID fornecido,
     * busca o item específico para preencher o modal de edição.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        AdministracaoDAO dao = new AdministracaoDAO();
        List<Administracao> listaAdmins = new ArrayList<>(); // Inicia vazia por segurança
        String erro = null;

        try {
            // 1. Busca a lista completa para a tabela de fundo
            listaAdmins = dao.read(); // Pode lançar SQLException

            // 2. Pega o ID da URL para carregar o modal de edição
            String idParam = request.getParameter("id");
            Administracao adminModal = null;

            if (idParam != null && !idParam.isEmpty()) {
                int id = Integer.parseInt(idParam); // Pode lançar NumberFormatException
                adminModal = dao.read(id); // Pode lançar SQLException

                if (adminModal != null) {
                    request.setAttribute("adminModal", adminModal); // Envia objeto para o JSP
                    request.setAttribute("abrirModal", "update"); // Avisa o JSP para abrir o modal
                } else {
                    erro = "Admin ID " + id + " não encontrado (doGet).";
                }
            }
            // Se idParam for nulo, apenas carrega a página sem modal

        } catch (SQLException e) {
            e.printStackTrace();
            erro = "Erro de banco de dados ao carregar dados para edição: " + e.getMessage();

        } catch (NumberFormatException e) {
            erro = "ID inválido fornecido (doGet): " + request.getParameter("id");
            System.err.println("ID inválido ('id') em admin doGet: " + request.getParameter("id"));

        } catch (Exception e) { // Outros erros inesperados
            e.printStackTrace();
            erro = "Erro inesperado ao buscar dados (doGet): " + e.getMessage();
        }

        // Encaminhamento para o JSP
        if (erro != null) {
            request.setAttribute("erro", erro);
        }
        request.setAttribute("listaAdmins", listaAdmins); // Envia a lista (mesmo que vazia)
        request.getRequestDispatcher("/WEB-INF/pages/administrador.jsp").forward(request, response);
    }

    /*
     * Executa a atualização após o envio do formulário (modal).
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Define o encoding para UTF-8 ANTES de ler parâmetros
        request.setCharacterEncoding("UTF-8");

        AdministracaoDAO dao = new AdministracaoDAO();
        int id = 0;
        String nome = null; // Declarar fora do try para usar no catch/finally
        String email = null; // Declarar fora do try para usar no catch/finally
        boolean sucessoNaOperacao = false;
        String erro = null;

        try {
            // 1. Coletar e converter parâmetros
            String idParam = request.getParameter("id");
            id = Integer.parseInt(idParam); // Pode lançar NumberFormatException
            nome = request.getParameter("nome");
            email = request.getParameter("email");
            String novaSenha = request.getParameter("senha"); // Senha pode ser opcional

            // 2. Buscar o objeto original no banco
            Administracao adminParaAtualizar = dao.read(id); // Pode lançar SQLException
            if (adminParaAtualizar == null) {
                // Erro: tentando atualizar um admin que não existe mais
                throw new Exception("Administrador ID " + id + " não encontrado para atualizar.");
            }

            // 3. Aplicar as mudanças (Model pode disparar validações)
            adminParaAtualizar.setNome(nome);
            adminParaAtualizar.setEmail(email);

            // 4. Tratar atualização de senha (opcional)
            if (novaSenha != null && !novaSenha.trim().isEmpty()) {
                adminParaAtualizar.setSenha(novaSenha);
            }

            // 5. Persistir no banco
            int resultado = dao.update(adminParaAtualizar); // Pode lançar SQLException

            if (resultado > 0) {
                sucessoNaOperacao = true;
            } else {
                erro = "Não foi possível atualizar o administrador (ID: " + id + ").";
            }

            // Captura erros de validação do Model ou de conversão de ID
        } catch (IllegalArgumentException | NullPointerException e) {
            // NumberFormatException é uma subclasse de IllegalArgumentException
            erro = "Erro de validação ou formato inválido: " + e.getMessage();

            // Captura erros de BANCO (ex: e-mail duplicado, falha na conexão)
        } catch (SQLException e) {
            e.printStackTrace();
            // Tenta dar uma mensagem amigável para e-mail duplicado (UNIQUE constraint)
            if (e.getMessage() != null && (e.getMessage().contains("Duplicate entry") || e.getMessage().contains("UNIQUE constraint failed"))) {
                erro = "Erro: O e-mail informado ('" + email + "') já está cadastrado para outro usuário.";
            } else {
                erro = "Erro de banco de dados ao atualizar: " + e.getMessage();
            }

        } catch (Exception e) { // Outros erros inesperados
            e.printStackTrace();
            erro = "Erro inesperado ao atualizar: " + e.getMessage();
        }

        // Fluxo de Resposta
        if (sucessoNaOperacao) {
            // Redireciona (PRG - Post-Redirect-Get) para a listagem
            System.out.println("Admin ID " + id + " atualizado com sucesso.");
            response.sendRedirect(request.getContextPath() + "/admin-crud");
            return; // Encerra a execução
        }

        // Caminho de Falha (Se 'sucessoNaOperacao' == false ou se uma Exceção foi capturada)
        System.err.println("Falha ao atualizar admin ID " + id + ". Fazendo forward. Erro: " + erro);
        request.setAttribute("erro", erro);

        // Guarda dados para repopular o modal (user-friendly)
        request.setAttribute("nome_previo", nome);
        request.setAttribute("email_previo", email);

        // Recarrega a lista de fundo
        List<Administracao> listaAdmins;
        try {
            // Trata SQLException ao recarregar a lista
            listaAdmins = dao.read();
        } catch (SQLException e) {
            e.printStackTrace();
            listaAdmins = new ArrayList<>(); // Lista vazia
            request.setAttribute("erro", erro + " | ERRO ADICIONAL: Falha ao recarregar a lista.");
        }
        request.setAttribute("listaAdmins", listaAdmins);

        // Tenta recarregar o objeto do modal para o JSP saber o ID que estava sendo editado
        if (id > 0 && request.getAttribute("adminModal") == null) {
            try {
                // Trata SQLException ao recarregar dados do modal
                request.setAttribute("adminModal", dao.read(id));
            } catch (Exception readEx) {
                // Se falhar, o JSP deve ser capaz de lidar com adminModal nulo
                System.err.println("Falha ao recarregar dados do modal de update: " + readEx.getMessage());
            }
        }

        request.setAttribute("abrirModal", "update"); // Avisa para reabrir modal
        request.getRequestDispatcher("/WEB-INF/pages/administrador.jsp").forward(request, response);
    }
}