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
 * Servlet focado em ATUALIZAR (Update) um Plano.
 * Usa doGet para carregar o modal com dados existentes e doPost para executar a atualização.
 * Segue o padrão robusto de tratamento de exceções.
 */
@WebServlet("/plano-update")
public class ServletUpdatePlano extends HttpServlet {

    /*
     * Prepara a página para a edição.
     * Carrega a lista completa de planos (tabela) e o plano específico (modal).
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
                    request.setAttribute("abrirModal", "update");
                } else {
                    // Se a lista já carregou com erro, mantemos o erro anterior
                    if (erro == null) erro = "Plano ID " + id + " não encontrado (doGet).";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            erro = "Erro de banco de dados ao carregar dados: " + e.getMessage();
            listaPlanos = new ArrayList<>(); // Garante que a lista fique vazia após falha
        } catch (NumberFormatException e) {
            erro = "ID inválido fornecido (doGet): " + idParam;
            System.err.println("ID inválido ('id') update plano (doGet): " + idParam);
        } catch (Exception e) {
            e.printStackTrace();
            erro = "Erro inesperado ao carregar dados (doGet): " + e.getMessage();
        }

        // Define atributos e encaminha para o JSP
        if (erro != null) {
            request.setAttribute("erro", erro);
        }
        request.setAttribute("listaPlanos", listaPlanos);
        request.getRequestDispatcher("/WEB-INF/pages/planos.jsp").forward(request, response);
    }

    /*
     * Executa a atualização após o envio do formulário (modal).
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        PlanoDAO dao = new PlanoDAO();
        int id = 0;
        boolean success = false;
        String erro = null;

        // Parâmetros recebidos (coletados para repopular em caso de erro)
        String idParam = request.getParameter("id");
        String nome = request.getParameter("nome");
        String custoStr = request.getParameter("custo");
        String maxFuncionariosStr = request.getParameter("maxFuncionarios");
        String descricao = request.getParameter("descricao");

        try {
            // 1. Validação e conversão
            id = Integer.parseInt(idParam); // Pode lançar NumberFormatException
            float custo = Float.parseFloat(custoStr); // Pode lançar NumberFormatException
            int maxFuncionarios = Integer.parseInt(maxFuncionariosStr); // Pode lançar NumberFormatException

            // 2. Busca objeto existente
            Plano planoParaAtualizar = dao.read(id); // Pode lançar SQLException
            if (planoParaAtualizar == null) {
                throw new Exception("Plano ID " + id + " não encontrado para atualização.");
            }

            // 3. Aplica alterações (Model valida e lança exceções)
            planoParaAtualizar.setNome(nome);
            planoParaAtualizar.setCusto(custo);
            planoParaAtualizar.setMaxFuncionarios(maxFuncionarios);
            planoParaAtualizar.setDescricao(descricao);

            // 4. Atualiza no banco
            int resultado = dao.update(planoParaAtualizar); // Pode lançar SQLException
            if (resultado > 0) {
                success = true;
            } else {
                erro = "Não foi possível atualizar o plano (ID: " + id + "). O registro pode não existir mais.";
            }

        } catch (IllegalArgumentException | NullPointerException e) {
            // NumberFormatException (de int/float) e erros do Model
            erro = "Erro de validação ou formato inválido: " + e.getMessage();

        } catch (SQLException e) {
            e.printStackTrace();
            if (e.getMessage().contains("Duplicate entry") || e.getMessage().contains("UNIQUE constraint failed")) {
                erro = "Erro: Já existe um plano com este nome ('" + nome + "').";
            } else {
                erro = "Erro de banco de dados ao atualizar: " + e.getMessage();
            }

        } catch (Exception e) {
            e.printStackTrace();
            erro = "Erro inesperado ao atualizar: " + e.getMessage();
        }

        // 5. RESPOSTA
        if (success) {
            System.out.println("Plano ID " + id + " atualizado com sucesso.");
            response.sendRedirect(request.getContextPath() + "/planos-crud");
            return;
        }

        // 6. CAMINHO DE FALHA (Forward) — repopula formulário e recarrega lista
        System.err.println("Falha ao atualizar plano ID " + id + ". Fazendo forward. Erro: " + erro);
        request.setAttribute("erro", erro);

        // Repopulação
        request.setAttribute("nome_previo", nome);
        request.setAttribute("custo_previo", custoStr);
        request.setAttribute("maxFuncionarios_previo", maxFuncionariosStr);
        request.setAttribute("descricao_previo", descricao);

        // Recarrega lista de planos
        List<Plano> listaPlanos = new ArrayList<>();
        try {
            listaPlanos = dao.read();
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("erro", erro + " | ERRO ADICIONAL: Falha ao recarregar lista.");
        }
        request.setAttribute("listaPlanos", listaPlanos);

        // Tenta recarregar dados do modal (se o ID for válido)
        if (id > 0) {
            try {
                request.setAttribute("planoModal", dao.read(id));
            } catch (Exception readEx) {
                System.err.println("Falha ao recarregar dados do modal de update: " + readEx.getMessage());
            }
        }

        request.setAttribute("abrirModal", "update");
        request.getRequestDispatcher("/WEB-INF/pages/planos.jsp").forward(request, response);
    }
}