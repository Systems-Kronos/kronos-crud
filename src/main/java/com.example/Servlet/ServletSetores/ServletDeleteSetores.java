package com.example.Servlet.ServletSetores;

import com.example.dao.SetorDAO;
import com.example.dao.EmpresaDAO;
import com.example.Model.Setor;
import com.example.Model.Empresa;
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
 * Servlet focado em DELETAR (Delete) um Setor.
 * Usa doGet para carregar o modal de confirmação e doPost para executar a exclusão.
 * Segue o padrão robusto de tratamento de exceções.
 */
@WebServlet("/setores-delete")
public class ServletDeleteSetores extends HttpServlet {

    /*
     * Prepara a página para a exclusão.
     * Carrega a lista completa de setores (tabela), o setor específico (modal)
     * e a lista completa de empresas (dropdowns).
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        SetorDAO dao = new SetorDAO();
        EmpresaDAO empresaDAO = new EmpresaDAO(); // DAO para Empresas
        List<Setor> listaSetores = new ArrayList<>(); // Inicia vazia por segurança
        List<Empresa> listaEmpresas = new ArrayList<>(); // Lista para dropdowns
        String erro = null;
        String idParam = request.getParameter("id");

        // Bloco try-catch unificado para todas as leituras de banco
        try {
            // 1. Busca a lista completa de setores para a tabela de fundo
            listaSetores = dao.read(); // Pode lançar SQLException

            // 2. Busca a lista completa de empresas para os modais
            listaEmpresas = empresaDAO.read(); // Pode lançar SQLException

            Setor setorModal = null;

            if (idParam != null && !idParam.isEmpty()) {
                int id = Integer.parseInt(idParam); // Pode lançar NumberFormatException
                setorModal = dao.read(id); // Pode lançar SQLException

                if (setorModal != null) {
                    request.setAttribute("setorModal", setorModal); // Envia objeto para o JSP
                    request.setAttribute("abrirModal", "delete"); // Avisa o JSP para abrir o modal
                } else {
                    erro = "Setor ID " + id + " não encontrado (doGet).";
                }
            }
            // Se idParam for nulo, apenas carrega a página sem modal

        } catch (SQLException e) {
            e.printStackTrace();
            erro = "Erro de banco de dados ao carregar dados: " + e.getMessage();
        } catch (NumberFormatException e) {
            erro = "ID inválido fornecido (doGet): " + request.getParameter("id");
            System.err.println("ID inválido ('id') delete setor (doGet): " + idParam);
        } catch (Exception e) { // Outros erros inesperados
            e.printStackTrace();
            erro = "Erro inesperado ao buscar dados (doGet): " + e.getMessage();
        }

        // Encaminhamento para o JSP
        if (erro != null) {
            request.setAttribute("erro", erro);
        }
        request.setAttribute("listaSetores", listaSetores);
        request.setAttribute("listaEmpresas", listaEmpresas);

        request.getRequestDispatcher("/WEB-INF/pages/setores.jsp").forward(request, response);
    }

    /*
     * Executa a exclusão do Setor após a confirmação no modal.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        SetorDAO dao = new SetorDAO();
        int id = 0;
        boolean success = false;
        String erro = null; // Armazena a mensagem de erro

        try {
            // Pega o ID do campo oculto do formulário modal
            String idParam = request.getParameter("id");
            id = Integer.parseInt(idParam);

            // Executa a deleção
            int resultado = dao.delete(id); // Pode lançar SQLException (ex: FK)

            if (resultado > 0) {
                success = true;
            } else {
                // Falha no DAO (ex: ID não existe mais)
                erro = "Não foi possível deletar o setor (ID: " + id + "). O registro pode já ter sido removido.";
            }

        } catch (NumberFormatException e) {
            erro = "ID inválido fornecido para exclusão.";
            System.err.println("ID inválido ('id') delete setor doPost: " + request.getParameter("id"));

        } catch (SQLException e) {
            e.printStackTrace();
            // Mensagem amigável para restrição de Foreign Key (FK)
            // Ex: Se o setor ainda possui Usuários
            if (e.getMessage().contains("violates foreign key constraint") || e.getMessage().contains("FOREIGN KEY constraint failed")) {
                erro = "Não é possível excluir este setor (ID: " + id + "), pois ele possui registros associados (ex: usuários). Remova as dependências primeiro.";
            } else {
                erro = "Erro de banco de dados ao excluir setor: " + e.getMessage();
            }

        } catch (Exception e) { // Captura outros erros inesperados
            e.printStackTrace();
            erro = "Erro inesperado ao processar a exclusão: " + e.getMessage();
        }

        // Fluxo de Resposta
        if (success) {
            // SUCESSO: Redireciona (PRG) para a listagem
            System.out.println("Setor ID " + id + " deletado com sucesso.");
            response.sendRedirect(request.getContextPath() + "/setores-crud");
            return; // Encerra a execução
        }

        // Caminho de Falha (Forward)
        System.err.println("Falha ao deletar setor ID " + id + ". Fazendo forward. Erro: " + erro);
        request.setAttribute("erro", erro);

        // Recarrega dados necessários para o JSP
        List<Setor> listaSetores = new ArrayList<>();
        try {
            // Trata SQLException ao recarregar a lista de fundo
            listaSetores = dao.read();
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("erro", erro + " | ERRO ADICIONAL: Falha ao recarregar a lista de setores.");
        }
        request.setAttribute("listaSetores", listaSetores);

        // Recarrega lista de Empresas (necessária para os modais Create/Update)
        try {
            EmpresaDAO empresaDAO = new EmpresaDAO();
            request.setAttribute("listaEmpresas", empresaDAO.read());
        } catch (Exception empresaEx) {
            empresaEx.printStackTrace();
            request.setAttribute("erro", erro + " | ERRO ADICIONAL: Falha ao recarregar lista de empresas.");
        }

        // Tenta recarregar modal com dados (se ID for válido)
        if (id > 0) {
            try {
                // Trata exceção ao recarregar dados do modal
                request.setAttribute("setorModal", dao.read(id));
            } catch (Exception readEx) {
                System.err.println("Falha ao recarregar dados do modal de delete: " + readEx.getMessage());
            }
        }

        request.setAttribute("abrirModal", "delete"); // Avisa para reabrir modal
        request.getRequestDispatcher("/WEB-INF/pages/setores.jsp").forward(request, response);
    }
}