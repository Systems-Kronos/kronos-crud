package com.example.Servlet.ServletHabilidades;

import com.example.dao.HabilidadesDAO;
import com.example.Model.Habilidades;
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
 * Servlet focado SOMENTE em CRIAR (Create) uma nova Habilidade.
 * Segue o padrão dos Servlets de Administracao.
 */
@WebServlet("/habilidade-create")
public class ServletCreateHabilidade extends HttpServlet {

    /*
     * Processa a criação de uma nova Habilidade.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // 1. Coleta de parâmetros
        String nome = request.getParameter("nome");
        String tag = request.getParameter("tag");
        String descricao = request.getParameter("descricao");

        HabilidadesDAO dao = new HabilidadesDAO();
        boolean success = false;
        String erro = null; // Armazena a mensagem de erro

        try {
            // 2. Validação (Model)
            // O construtor do Model lança IllegalArgumentException ou NullPointerException
            Habilidades novaHabilidade = new Habilidades(nome, tag, descricao);

            // 3. Persistência (DAO)
            // O DAO (corrigido) lança SQLException
            success = dao.create(novaHabilidade);

            if (success) {
                // 4. SUCESSO (PRG Pattern)
                System.out.println("Habilidade criada com sucesso!");
                response.sendRedirect(request.getContextPath() + "/habilidades-crud"); // URL da listagem
                return; // IMPORTANTE: Encerra aqui após redirect
            } else {
                // Falha no DAO (ex: create retornou false sem lançar exceção)
                erro = "Erro ao cadastrar habilidade.";
            }

        } catch (IllegalArgumentException | NullPointerException e) {
            // Captura erros de VALIDAÇÃO do Model
            erro = "Erro de validação: " + e.getMessage();

        } catch (SQLException e) { // Captura erro do DAO.create()
            e.printStackTrace();
            if (e.getMessage().contains("Duplicate entry") || e.getMessage().contains("UNIQUE constraint failed")) {
                erro = "Erro: Já existe uma habilidade com este nome ('" + nome + "').";
            } else {
                erro = "Erro de banco de dados ao criar habilidade: " + e.getMessage();
            }

        } catch (Exception e) { // Captura outros erros inesperados
            e.printStackTrace();
            erro = "Erro inesperado ao criar habilidade: " + e.getMessage();
        }

        // 5. CAMINHO DE FALHA (Forward)
        // O código só chega aqui se 'success' for false ou se uma exceção foi pega.
        System.err.println("Falha na criação da habilidade. Fazendo forward. Erro: " + erro);

        // Define os atributos de erro e de repopulação do formulário
        request.setAttribute("erro", erro);
        // Guarda dados para repopular o formulário
        request.setAttribute("nome_previo", nome);
        request.setAttribute("tag_previo", tag);
        request.setAttribute("descricao_previo", descricao);

        // Recarrega a lista de habilidades para exibir na tabela do JSP
        List<Habilidades> listaHabilidades = new ArrayList<>();
        try {
            // Trata SQLException do dao.read()
            listaHabilidades = dao.read();
        } catch (SQLException e) {
            e.printStackTrace(); // Loga o erro de leitura
            // Concatena o erro da LEITURA com o erro original da CRIAÇÃO
            request.setAttribute("erro", erro + " | ERRO ADICIONAL: Falha ao recarregar a lista de habilidades.");
        }
        request.setAttribute("listaHabilidades", listaHabilidades);

        // Avisa o JSP para reabrir o modal de CREATE
        request.setAttribute("abrirModal", "create");

        // Encaminha (forward) com erro e dados prévios
        request.getRequestDispatcher("/WEB-INF/pages/habilidades.jsp").forward(request, response);
    }
}