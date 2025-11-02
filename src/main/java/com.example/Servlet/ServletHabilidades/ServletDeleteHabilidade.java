package com.example.Servlet.ServletHabilidades;

import com.example.Model.Habilidades;
import com.example.dao.HabilidadesDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

/**
 * Servlet focado em DELETAR (Delete) uma Habilidade.
 * Usa doGet para carregar o modal de confirmação e doPost para executar a exclusão.
 * Segue o padrão dos Servlets de Administracao.
 */
@WebServlet("/habilidades-delete")
public class ServletDeleteHabilidade extends HttpServlet {

    /*
     * Prepara a página para a exclusão.
     * Carrega a lista completa e, se um ID for fornecido,
     * busca o item específico para preencher o modal de confirmação.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HabilidadesDAO dao = new HabilidadesDAO();
        List<Habilidades> listaHabilidades = new ArrayList<>(); // Inicia vazia por segurança
        String erro = null;

        try {
            // 1. Busca a lista completa para a tabela de fundo
            listaHabilidades = dao.read(); // Pode lançar SQLException

            // 2. Pega o ID da URL para carregar o modal
            String idParam = request.getParameter("id");
            Habilidades habilidadeModal = null;

            if (idParam != null && !idParam.isEmpty()) {
                int id = Integer.parseInt(idParam); // Pode lançar NumberFormatException
                habilidadeModal = dao.read(id); // Busca item (Pode lançar SQLException)

                if (habilidadeModal != null) {
                    request.setAttribute("habilidadeModal", habilidadeModal); // Envia objeto para o JSP
                    request.setAttribute("abrirModal", "delete"); // Avisa o JSP para abrir o modal
                } else {
                    erro = "Habilidade ID " + id + " não encontrada (doGet).";
                }
            }
            // Se idParam for nulo, apenas carrega a página sem modal

        } catch (SQLException e) {
            e.printStackTrace();
            erro = "Erro de banco de dados ao carregar dados: " + e.getMessage();
        } catch (NumberFormatException e) {
            erro = "ID inválido fornecido (doGet): " + request.getParameter("id");
            System.err.println("ID inválido ('id') delete habilidade (doGet): " + request.getParameter("id"));
        } catch (Exception e) { // Outros erros inesperados
            e.printStackTrace();
            erro = "Erro inesperado ao buscar dados (doGet): " + e.getMessage();
        }

        // Encaminhamento para o JSP
        if (erro != null) {
            request.setAttribute("erro", erro);
        }
        request.setAttribute("listaHabilidades", listaHabilidades); // Envia a lista (mesmo que vazia)
        request.getRequestDispatcher("/WEB-INF/pages/habilidades.jsp").forward(request, response);
    }

    /*
     * Executa a exclusão após a confirmação no modal.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HabilidadesDAO dao = new HabilidadesDAO();
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
                erro = "Não foi possível deletar a habilidade (ID: " + id + ").";
            }

        } catch (NumberFormatException e) {
            erro = "ID inválido fornecido para exclusão.";
            System.err.println("ID inválido ('id') delete habilidade doPost: " + request.getParameter("id"));

        } catch (SQLException e) {
            e.printStackTrace();
            // Mensagem amigável para restrição de Foreign Key (FK)
            if (e.getMessage().contains("violates foreign key constraint") || e.getMessage().contains("FOREIGN KEY constraint failed")) {
                erro = "Não é possível excluir esta habilidade (ID: " + id + "), pois está sendo referenciada em outra parte do sistema.";
            } else {
                erro = "Erro de banco de dados ao excluir: " + e.getMessage();
            }

        } catch (Exception e) { // Captura outros erros inesperados
            e.printStackTrace();
            erro = "Erro inesperado ao processar a exclusão: " + e.getMessage();
        }

        // Fluxo de Resposta
        if (success) {
            // Redireciona (PRG - Post-Redirect-Get) para a listagem
            System.out.println("Habilidade ID " + id + " deletada com sucesso.");
            response.sendRedirect(request.getContextPath() + "/habilidades-crud");
            return; // Encerra a execução
        }

        // Caminho de Falha
        System.err.println("Falha ao deletar habilidade ID " + id + ". Fazendo forward. Erro: " + erro);
        request.setAttribute("erro", erro);

        // Recarrega dados necessários para o JSP
        List<Habilidades> listaHabilidades = new ArrayList<>();
        try {
            // Trata SQLException ao recarregar a lista de fundo
            listaHabilidades = dao.read();
        } catch (SQLException e) {
            e.printStackTrace();
            // Concatena o erro da leitura com o erro original da EXCLUSÃO
            request.setAttribute("erro", erro + " | ERRO ADICIONAL: Falha ao recarregar a lista.");
        }
        request.setAttribute("listaHabilidades", listaHabilidades);

        // Tenta recarregar modal com dados (se ID for válido)
        if (id > 0) {
            try {
                // Trata exceção ao recarregar dados do modal
                request.setAttribute("habilidadeModal", dao.read(id));
            } catch (Exception readEx) {
                System.err.println("Falha ao recarregar dados do modal de delete: " + readEx.getMessage());
            }
        }

        // Avisa o JSP para reabrir o modal de DELETE
        request.setAttribute("abrirModal", "delete");
        // Encaminha (forward) com erro e dados prévios
        request.getRequestDispatcher("/WEB-INF/pages/habilidades.jsp").forward(request, response);
    }
}