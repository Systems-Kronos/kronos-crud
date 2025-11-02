package com.example.Servlet.ServletEmpresa;

import com.example.Model.Usuario;
import com.example.dao.EmpresaDAO;
import com.example.dao.PlanoDAO;
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
 * Servlet focado em DELETAR (Delete) uma Empresa.
 * Usa doGet para carregar o modal de confirmação e doPost para executar a exclusão.
 * Segue o padrão robusto de tratamento de exceções.
 */
@WebServlet("/empresas-delete")
public class ServletDeleteEmpresa extends HttpServlet {

    /*
     * Prepara a página para a exclusão.
     * Carrega a lista completa e, se um ID for fornecido,
     * busca o item específico para preencher o modal de confirmação.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        EmpresaDAO dao = new EmpresaDAO();
        List<Empresa> listaEmpresas = new ArrayList<>(); // Inicia vazia por segurança
        String erro = null;

        try {
            // 1. Busca a lista completa para a tabela de fundo
            listaEmpresas = dao.read(); // Pode lançar SQLException

            // 2. Pega o ID da URL para carregar o modal
            String idParam = request.getParameter("id");
            Usuario usuarioModal = null;

            if (idParam != null && !idParam.isEmpty()) {
                int id = Integer.parseInt(idParam); // Pode lançar NumberFormatException
                Empresa empresaModal = dao.read(id); // Pode lançar SQLException

                if (empresaModal != null) {
                    request.setAttribute("empresaModal", empresaModal); // Envia objeto para o JSP
                    request.setAttribute("abrirModal", "delete"); // Avisa o JSP para abrir o modal
                } else {
                    erro = "Empresa com ID " + id + " não encontrada (doGet).";
                }
            }
            // Se idParam for nulo, apenas carrega a página sem modal

        } catch (SQLException e) {
            e.printStackTrace();
            erro = "Erro de banco de dados ao carregar dados: " + e.getMessage();
        } catch (NumberFormatException e) {
            erro = "ID inválido fornecido (doGet): " + request.getParameter("id");
            System.err.println("ID inválido ('id') delete empresa (doGet): " + request.getParameter("id"));
        } catch (Exception e) { // Outros erros inesperados
            e.printStackTrace();
            erro = "Erro inesperado ao buscar dados (doGet): " + e.getMessage();
        }

        // Encaminhamento para o JSP
        if (erro != null) {
            request.setAttribute("erro", erro);
        }
        request.setAttribute("listaEmpresas", listaEmpresas);

        // Recarrega lista de Planos
        try {
            PlanoDAO planoDAO = new PlanoDAO();
            request.setAttribute("listaPlanos", planoDAO.read());
        } catch (Exception planoEx) {
            planoEx.printStackTrace();
            request.setAttribute("erro", (erro != null ? erro : "") + " | ERRO ADICIONAL: Falha ao recarregar lista de planos.");
        }

        request.getRequestDispatcher("/WEB-INF/pages/empresas.jsp").forward(request, response);
    }

    /*
     * Executa a exclusão da Empresa após a confirmação no modal.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        EmpresaDAO dao = new EmpresaDAO();
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
                erro = "Não foi possível deletar a empresa (ID: " + id + "). O registro pode já ter sido removido.";
            }

        } catch (NumberFormatException e) {
            erro = "ID inválido fornecido para exclusão.";
            System.err.println("ID inválido ('id') delete empresa doPost: " + request.getParameter("id"));

        } catch (SQLException e) {
            e.printStackTrace();
            // Mensagem amigável para restrição de Foreign Key (FK)
            // Ex: Se a empresa ainda possui Setores cadastrados
            if (e.getMessage().contains("violates foreign key constraint") || e.getMessage().contains("FOREIGN KEY constraint failed")) {
                erro = "Não é possível excluir esta empresa (ID: " + id + "), pois ela possui registros associados (ex: setores). Remova as dependências primeiro.";
            } else {
                erro = "Erro de banco de dados ao excluir empresa: " + e.getMessage();
            }

        } catch (Exception e) { // Captura outros erros inesperados
            e.printStackTrace();
            erro = "Erro inesperado ao processar a exclusão: " + e.getMessage();
        }

        // Fluxo de Resposta
        if (success) {
            // SUCESSO: Redireciona (PRG) para a listagem
            System.out.println("Empresa ID " + id + " deletada com sucesso.");
            response.sendRedirect(request.getContextPath() + "/empresas-crud");
            return; // Encerra a execução
        }

        // Caminho de Falha (Forward)
        System.err.println("Falha ao deletar empresa ID " + id + ". Fazendo forward. Erro: " + erro);
        request.setAttribute("erro", erro);

        // Recarrega dados necessários para o JSP
        List<Empresa> listaEmpresas = new ArrayList<>();
        try {
            // Trata SQLException ao recarregar a lista de fundo
            listaEmpresas = dao.read();
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("erro", erro + " | ERRO ADICIONAL: Falha ao recarregar a lista.");
        }
        request.setAttribute("listaEmpresas", listaEmpresas);

        // Recarrega lista de Planos (necessária para os modais Create/Update)
        try {
            PlanoDAO planoDAO = new PlanoDAO();
            request.setAttribute("listaPlanos", planoDAO.read());
        } catch (Exception planoEx) {
            planoEx.printStackTrace();
            request.setAttribute("erro", erro + " | ERRO ADICIONAL: Falha ao recarregar lista de planos.");
        }

        // Tenta recarregar modal com dados (se ID for válido)
        if (id > 0) {
            try {
                request.setAttribute("empresaModal", dao.read(id));
            } catch (Exception readEx) {
                System.err.println("Falha ao recarregar dados do modal de delete: " + readEx.getMessage());
            }
        }

        request.setAttribute("abrirModal", "delete"); // Avisa para reabrir modal
        request.getRequestDispatcher("/WEB-INF/pages/empresas.jsp").forward(request, response);
    }
}