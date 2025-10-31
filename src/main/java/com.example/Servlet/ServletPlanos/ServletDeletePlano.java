package com.example.Servlet.ServletPlanos;

import com.example.dao.PlanoDAO;
import com.example.Model.Plano;
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
 * Servlet focado em DELETAR (Delete) um Plano.
 * Usa doGet para carregar o modal de confirmação e doPost para executar a exclusão.
 * Segue o padrão robusto de tratamento de exceções.
 */
@WebServlet("/planos-delete")
public class ServletDeletePlano extends HttpServlet {

    /*
     * Prepara a tela para a exclusão.
     * Carrega a lista completa e, se um ID for fornecido, busca o item específico
     * para preencher o modal de confirmação.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PlanoDAO dao = new PlanoDAO();
        List<Plano> listaPlanos = new ArrayList<>();
        String erro = null;

        String idParam = request.getParameter("id");

        // Bloco try-catch unificado para todas as leituras de banco
        try {
            // 1. Busca a lista completa (para exibir na tabela de fundo)
            listaPlanos = dao.read(); // Pode lançar SQLException

            // 2. Carrega o plano específico para o modal (se houver ID)
            Plano planoModal = null;
            if (idParam != null && !idParam.isEmpty()) {
                int id = Integer.parseInt(idParam); // Pode lançar NumberFormatException
                planoModal = dao.read(id); // Pode lançar SQLException

                if (planoModal != null) {
                    request.setAttribute("planoModal", planoModal);
                    request.setAttribute("abrirModal", "delete");
                } else {
                    // Se a lista já carregou com erro, mantemos o erro anterior
                    if (erro == null) erro = "Plano ID " + id + " não encontrado.";
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            erro = "Erro de banco de dados ao carregar dados: " + e.getMessage();
            listaPlanos = new ArrayList<>();

        } catch (NumberFormatException e) {
            erro = "ID inválido fornecido (doGet): " + idParam;
            System.err.println("ID inválido ('id') delete plano (doGet): " + idParam);

        } catch (Exception e) { // Outros erros inesperados
            e.printStackTrace();
            erro = "Erro inesperado ao buscar dados: " + e.getMessage();
        }

        // 3. Define atributos e encaminha para o JSP
        if (erro != null) {
            request.setAttribute("erro", erro);
        }
        request.setAttribute("listaPlanos", listaPlanos);
        request.getRequestDispatcher("/WEB-INF/pages/planos.jsp").forward(request, response);
    }

    /*
     * Executa a exclusão após confirmação no modal.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PlanoDAO dao = new PlanoDAO();
        int id = 0;
        boolean success = false;
        String erro = null;
        String idParam = request.getParameter("id"); // Pega o ID para o catch

        try {
            // 1. Recupera o ID enviado pelo formulário modal
            id = Integer.parseInt(idParam);

            // 2. Executa a deleção via DAO
            int resultado = dao.delete(id); // pode lançar SQLException
            if (resultado > 0) {
                success = true;
            } else {
                erro = "Não foi possível deletar o plano (ID: " + id + "). O registro pode já ter sido removido.";
            }

        } catch (NumberFormatException e) {
            erro = "ID inválido fornecido para exclusão.";
            System.err.println("ID inválido ('id') delete plano (doPost): " + idParam);

        } catch (SQLException e) {
            e.printStackTrace();
            // Mensagem amigável para restrição de FK (se ocorrer)
            if (e.getMessage().contains("violates foreign key constraint") ||
                    e.getMessage().contains("FOREIGN KEY constraint failed")) {
                erro = "Não é possível excluir este plano (ID: " + id + "), pois está sendo referenciado por outros registros (ex: Empresas).";
            } else {
                erro = "Erro de banco de dados ao excluir: " + e.getMessage();
            }

        } catch (Exception e) { // Outros erros inesperados
            e.printStackTrace();
            erro = "Erro inesperado ao processar a exclusão: " + e.getMessage();
        }

        // 3. Fluxo de sucesso
        if (success) {
            System.out.println("Plano ID " + id + " deletado com sucesso.");
            response.sendRedirect(request.getContextPath() + "/planos-crud");
            return;
        }

        // 4. Caminho de Falha (forward) — recarrega dados e reabre modal
        System.err.println("Falha ao deletar plano ID " + id + ". Forwarding. Erro: " + erro);
        request.setAttribute("erro", erro);

        // Recarrega lista de planos
        List<Plano> listaPlanos = new ArrayList<>();
        try {
            // Trata SQLException
            listaPlanos = dao.read();
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("erro", erro + " | ERRO ADICIONAL: Falha ao recarregar a lista de planos.");
        }
        request.setAttribute("listaPlanos", listaPlanos);

        // Tenta recarregar dados do modal (se possível)
        if (id > 0) {
            try {
                request.setAttribute("planoModal", dao.read(id));
            } catch (Exception readEx) {
                System.err.println("Falha ao recarregar dados do modal de delete: " + readEx.getMessage());
            }
        }

        request.setAttribute("abrirModal", "delete");
        request.getRequestDispatcher("/WEB-INF/pages/planos.jsp").forward(request, response);
    }
}