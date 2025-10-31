package com.example.Servlet.ServletHabilidades;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.Model.Habilidades;
import com.example.dao.HabilidadesDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet focado em ATUALIZAR (Update) uma Habilidade.
 * Usa doGet para carregar o modal com dados existentes e doPost para executar a atualização.
 * Segue o padrão dos Servlets de Administracao.
 */
@WebServlet("/habilidade-update")
public class ServletUpdateHabilidade extends HttpServlet {

    /*
     * Prepara a página para a edição.
     * Carrega a lista completa e, com o ID fornecido,
     * busca o item específico para preencher o modal de edição.
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

            // 2. Pega o ID da URL para carregar o modal de edição
            String idParam = request.getParameter("id");
            Habilidades habilidadeModal = null;

            if (idParam != null && !idParam.isEmpty()) {
                int id = Integer.parseInt(idParam); // Pode lançar NumberFormatException
                habilidadeModal = dao.read(id); // Pode lançar SQLException

                if (habilidadeModal != null) {
                    request.setAttribute("habilidadeModal", habilidadeModal); // Envia objeto para o JSP
                    request.setAttribute("abrirModal", "update"); // Avisa o JSP para abrir o modal
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
            System.err.println("ID inválido ('id') update habilidade (doGet): " + request.getParameter("id"));
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
     * Executa a atualização após o envio do formulário (modal).
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HabilidadesDAO dao = new HabilidadesDAO();
        int id = 0;
        boolean success = false;
        String erro = null;

        // Coleta de parâmetros (declarados fora para usar no catch)
        String idParam = request.getParameter("id");
        String nome = request.getParameter("nome");
        String tag = request.getParameter("tag");
        String descricao = request.getParameter("descricao");

        try {
            // 1. Conversão e Validação de ID
            id = Integer.parseInt(idParam); // Pode lançar NumberFormatException

            // 2. Busca o objeto original no banco
            Habilidades habilidadeParaAtualizar = dao.read(id); // Pode lançar SQLException
            if (habilidadeParaAtualizar == null) {
                // Erro: tentando atualizar algo que não existe mais
                throw new Exception("Habilidade ID " + id + " não encontrada para atualizar.");
            }

            // 3. Aplica as mudanças (Model pode disparar validações)
            habilidadeParaAtualizar.setNome(nome);
            habilidadeParaAtualizar.setTag(tag);
            habilidadeParaAtualizar.setDescricao(descricao);

            // 4. Persiste no banco
            int resultado = dao.update(habilidadeParaAtualizar); // Pode lançar SQLException

            if (resultado > 0) {
                success = true;
            } else {
                erro = "Não foi possível atualizar a habilidade (ID: " + id + ").";
            }

            // Captura erros de validação do Model ou de conversão de ID
        } catch (IllegalArgumentException | NullPointerException e) {
            // NumberFormatException é uma subclasse de IllegalArgumentException
            erro = "Erro de validação ou formato inválido: " + e.getMessage();

        } catch (SQLException e) {
            e.printStackTrace();
            // Tenta dar uma mensagem amigável para nome duplicado (UNIQUE constraint)
            if (e.getMessage().contains("Duplicate entry") || e.getMessage().contains("UNIQUE constraint failed")) {
                erro = "Erro: Já existe uma habilidade com este nome ('" + nome + "').";
            } else {
                erro = "Erro de banco de dados ao atualizar: " + e.getMessage();
            }

        } catch (Exception e) { // Outros erros inesperados
            e.printStackTrace();
            erro = "Erro inesperado ao atualizar: " + e.getMessage();
        }

        // Fluxo de Resposta
        if (success) {
            // Redireciona (PRG) para a listagem
            System.out.println("Habilidade ID " + id + " atualizada com sucesso.");
            response.sendRedirect(request.getContextPath() + "/habilidades-crud");
            return; // Encerra a execução
        }

        // Caminho de Falha (Se 'success' == false ou se uma Exceção foi capturada)
        System.err.println("Falha ao atualizar habilidade ID " + id + ". Fazendo forward. Erro: " + erro);

        // Repopula o formulário em TODOS os casos de erro
        request.setAttribute("erro", erro);
        request.setAttribute("nome_previo", nome);
        request.setAttribute("tag_previo", tag);
        request.setAttribute("descricao_previo", descricao);

        // Recarrega a lista de fundo
        List<Habilidades> listaHabilidades = new ArrayList<>();
        try {
            // Trata SQLException ao recarregar a lista
            listaHabilidades = dao.read();
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("erro", erro + " | ERRO ADICIONAL: Falha ao recarregar a lista.");
        }
        request.setAttribute("listaHabilidades", listaHabilidades);

        // Tenta recarregar o objeto do modal para o JSP saber o ID que estava sendo editado
        if (id > 0 && request.getAttribute("habilidadeModal") == null) {
            try {
                // Trata exceção ao recarregar dados do modal
                request.setAttribute("habilidadeModal", dao.read(id));
            } catch (Exception readEx) {
                System.err.println("Falha ao recarregar dados do modal de update: " + readEx.getMessage());
            }
        }

        request.setAttribute("abrirModal", "update"); // Avisa para reabrir modal
        request.getRequestDispatcher("/WEB-INF/pages/habilidades.jsp").forward(request, response);
    }
}