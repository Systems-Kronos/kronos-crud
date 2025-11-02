package com.example.Servlet.ServletUsuario;

import com.example.dao.UsuarioDAO;
import com.example.Model.Usuario;
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
 * Servlet focado em DELETAR (Delete) um Usuário.
 * Lida com a remoção de associações de habilidades antes da exclusão.
 */
@WebServlet("/usuario-delete")
public class ServletDeleteUsuario extends HttpServlet {

    /*
     * Prepara a página para a exclusão do Usuário (Carrega lista e modal).
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UsuarioDAO dao = new UsuarioDAO();
        List<Usuario> listaUsuarios = new ArrayList<>();
        String erro = null;

        // Bloco try-catch unificado
        try {
            // 1. Busca a lista completa para a tabela de fundo
            listaUsuarios = dao.read(); // Pode lançar SQLException

            // 2. Pega o ID da URL para carregar o modal
            String idParam = request.getParameter("id");
            if (idParam != null && !idParam.isEmpty()) {
                int id = Integer.parseInt(idParam);
                Usuario usuarioModal = dao.read(id);

                if (usuarioModal != null) {
                    request.setAttribute("usuarioModal", usuarioModal);
                    request.setAttribute("abrirModal", "delete");
                } else {
                    erro = "Usuário ID " + id + " não encontrado (doGet).";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            erro = "Erro de banco de dados ao carregar dados: " + e.getMessage();
        } catch (NumberFormatException e) {
            erro = "ID inválido fornecido (doGet): " + request.getParameter("id");
        } catch (Exception e) {
            e.printStackTrace();
            erro = "Erro inesperado ao buscar dados (doGet): " + e.getMessage();
        }

        // Encaminhamento para o JSP
        if (erro != null) request.setAttribute("erro", erro);
        request.setAttribute("listaUsuarios", listaUsuarios);
        request.getRequestDispatcher("/WEB-INF/pages/usuario.jsp").forward(request, response);
    }

    /*
     * Executa a exclusão do Usuário.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UsuarioDAO dao = new UsuarioDAO();
        int id = 0;
        boolean success = false;
        String erro = null;

        try {
            String idParam = request.getParameter("id");
            id = Integer.parseInt(idParam);

            // 1. Remove associações de habilidades
            try {
                int habilidadesRemovidas = dao.removeAllHabilidadesFromUsuario(id);
                System.out.println("Removidas " + habilidadesRemovidas + " associações de habilidades para o usuário ID: " + id);
            } catch (SQLException eHab) {
                // Se falhar aqui, não tentamos deletar o usuário
                throw new SQLException("Falha ao remover associações de habilidades: " + eHab.getMessage(), eHab);
            }

            // 2. Executa a deleção do usuário
            int resultado = dao.delete(id); // Pode lançar SQLException

            if (resultado > 0) {
                success = true;
            } else {
                erro = "Não foi possível deletar o usuário (ID: " + id + "). O registro pode já ter sido removido.";
            }

        } catch (NumberFormatException e) {
            erro = "ID inválido fornecido para exclusão.";
        } catch (SQLException e) {
            e.printStackTrace();
            // Erro de FK (ex: usuário é supervisor de alguém)
            if (e.getMessage().contains("violates foreign key constraint")) {
                erro = "Não é possível excluir este usuário (ID: " + id + "), pois ele ainda é referenciado em outra parte do sistema (ex: como supervisor).";
            } else {
                erro = "Erro de banco de dados ao excluir usuário: " + e.getMessage();
            }
        } catch (Exception e) {
            e.printStackTrace();
            erro = "Erro inesperado ao processar a exclusão: " + e.getMessage();
        }

        // Fluxo de Resposta
        if (success) {
            // SUCESSO: Redireciona
            System.out.println("Usuário ID " + id + " deletado com sucesso.");
            response.sendRedirect(request.getContextPath() + "/usuarios-crud");
            return;
        }

        // Caminho de Falha (Forward)
        System.err.println("Falha ao deletar usuário ID " + id + ". Fazendo forward. Erro: " + erro);
        request.setAttribute("erro", erro);

        // Recarrega dados necessários para o JSP
        List<Usuario> listaUsuarios = new ArrayList<>();
        try {
            listaUsuarios = dao.read(); // Recarrega lista com habilidades
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("erro", erro + " | ERRO ADICIONAL: Falha ao recarregar a lista.");
        }
        request.setAttribute("listaUsuarios", listaUsuarios);

        // Tenta recarregar modal com dados (sem habilidades)
        if (id > 0) {
            try {
                request.setAttribute("usuarioModal", dao.read(id));
            } catch (Exception readEx) {
                System.err.println("Falha ao recarregar dados do modal de delete: " + readEx.getMessage());
            }
        }

        request.setAttribute("abrirModal", "delete");
        request.getRequestDispatcher("/WEB-INF/pages/usuario.jsp").forward(request, response);
    }
}