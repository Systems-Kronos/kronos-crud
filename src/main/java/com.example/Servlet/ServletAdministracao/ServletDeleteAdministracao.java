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
 * Servlet focado em DELETAR (Delete) um Administrador.
 * Usa doGet para carregar o modal de confirmação e doPost para executar a exclusão.
 */
@WebServlet("/admin-delete")
public class ServletDeleteAdministracao extends HttpServlet {

    /*
     * Prepara a página para a exclusão.
     * Carrega a lista completa e, se um ID for fornecido,
     * busca o item específico para preencher o modal de confirmação.
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

            // 2. Pega o ID da URL para carregar o modal
            String idParam = request.getParameter("id");
            Administracao adminModal = null;

            if (idParam != null && !idParam.isEmpty()) {
                int id = Integer.parseInt(idParam); // Pode lançar NumberFormatException
                adminModal = dao.read(id); // Busca admin (Pode lançar SQLException)

                if (adminModal != null) {
                    request.setAttribute("adminModal", adminModal); // Envia objeto para o JSP
                    request.setAttribute("abrirModal", "delete"); // Avisa o JSP para abrir o modal
                } else {
                    erro = "Admin ID " + id + " não encontrado (doGet).";
                }
            }
            // Se idParam for nulo, apenas carrega a página sem modal

        } catch (SQLException e) {
            e.printStackTrace();
            erro = "Erro de banco de dados ao carregar dados: " + e.getMessage();

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

    /**
     * Executa a exclusão após a confirmação no modal.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        AdministracaoDAO dao = new AdministracaoDAO();
        int id = 0;
        boolean success = false;
        String erro = null; // Armazena a mensagem de erro

        try {
            // Pega o ID do campo oculto do formulário modal
            String idParam = request.getParameter("id");
            id = Integer.parseInt(idParam);

            // Executa a deleção
            int resultado = dao.delete(id); // Pode lançar SQLException

            if (resultado > 0) {
                success = true;
            } else {
                // Falha no DAO (ex: ID não existe mais)
                erro = "Não foi possível deletar o administrador (ID: " + id + "). O registro pode já ter sido removido.";
            }

        } catch (NumberFormatException e) {
            erro = "ID inválido fornecido para exclusão.";
            System.err.println("ID inválido ('id') em admin doPost: " + request.getParameter("id"));

            // Captura específica para erros de SQL
        } catch (SQLException e) {
            e.printStackTrace();
            // Mensagem amigável para restrição de Foreign Key (FK)
            if (e.getMessage() != null && (e.getMessage().contains("violates foreign key constraint") || e.getMessage().contains("FOREIGN KEY constraint failed"))) {
                erro = "Não é possível excluir este administrador (ID: " + id + "), pois ele está sendo referenciado em outra parte do sistema.";
            } else {
                erro = "Erro de banco de dados ao excluir: " + e.getMessage();
            }

        } catch (Exception e) { // Captura outros erros inesperados
            e.printStackTrace();
            erro = "Erro inesperado ao processar a exclusão: " + e.getMessage();
        }

        // Fluxo de Resposta
        if (success) {
            // SUCESSO: Redireciona (PRG - Post-Redirect-Get) para a listagem
            System.out.println("Administrador ID " + id + " deletado com sucesso.");
            response.sendRedirect(request.getContextPath() + "/admin-crud");
            return; // Encerra a execução
        }

        // Caminho de Falha (Se 'success' == false ou se uma Exceção foi capturada)
        System.err.println("Falha ao deletar admin ID " + id + ". Fazendo forward. Erro: " + erro);
        request.setAttribute("erro", erro);

        // Recarrega dados necessários para o JSP
        List<Administracao> listaAdmins;
        try {
            // Trata SQLException ao recarregar a lista de fundo
            listaAdmins = dao.read();
        } catch (SQLException e) {
            e.printStackTrace();
            listaAdmins = new ArrayList<>(); // Usa uma lista vazia para não quebrar o JSP
            // Concatena o erro da LEITURA com o erro original da EXCLUSÃO
            request.setAttribute("erro", erro + " | ERRO ADICIONAL: Falha ao recarregar a lista.");
        }
        request.setAttribute("listaAdmins", listaAdmins);

        // Tenta recarregar modal com dados (se ID for válido)
        if (id > 0) {
            try {
                // Trata SQLException ao recarregar dados do modal
                request.setAttribute("adminModal", dao.read(id));
            } catch (Exception readEx) {
                // Não é crítico se isso falhar, mas logamos o erro
                System.err.println("Falha ao recarregar dados do modal de delete: " + readEx.getMessage());
            }
        }

        // Avisa o JSP para reabrir o modal de DELETE
        request.setAttribute("abrirModal", "delete");
        // Encaminha (forward) com erro e dados prévios
        request.getRequestDispatcher("/WEB-INF/pages/administrador.jsp").forward(request, response);
    }
}