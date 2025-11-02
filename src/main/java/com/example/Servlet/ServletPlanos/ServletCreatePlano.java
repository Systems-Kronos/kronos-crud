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
 * Servlet focado SOMENTE em CRIAR (Create) um novo Plano.
 * Segue o mesmo padrão técnico do ServletCreateHabilidade.
 */
@WebServlet("/plano-create")
public class ServletCreatePlano extends HttpServlet {

    /*
     * Processa a criação de um novo Plano.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // 1. Coleta de parâmetros
        String nome = request.getParameter("nome");
        String custoStr = request.getParameter("custo");
        String descricao = request.getParameter("descricao");
        String maxFuncionariosStr = request.getParameter("maxFuncionarios");

        PlanoDAO dao = new PlanoDAO();
        boolean success = false;
        String erro = null;

        try {
            // 2. Conversão e validação (Model)
            float custo = Float.parseFloat(custoStr);
            int maxFuncionarios = Integer.parseInt(maxFuncionariosStr);

            // O Model pode lançar IllegalArgumentException ou NullPointerException
            Plano novoPlano = new Plano(nome, custo, descricao, maxFuncionarios);

            // 3. Persistência (DAO)
            success = dao.create(novoPlano);

            if (success) {
                // 4. SUCESSO
                System.out.println("Plano criado com sucesso!");
                response.sendRedirect(request.getContextPath() + "/planos-crud");
                return; // Encerra após redirect
            } else {
                erro = "Erro ao cadastrar plano (DAO retornou falso).";
            }

        } catch (NumberFormatException e) {
            erro = "Erro: valores numéricos inválidos. Verifique o custo e a quantidade máxima de funcionários.";

        } catch (IllegalArgumentException | NullPointerException e) {
            // Erros de validação do Model
            erro = "Erro de validação: " + e.getMessage();

        } catch (SQLException e) {
            // Erros de banco de dados (DAO)
            e.printStackTrace();
            if (e.getMessage().contains("Duplicate entry") || e.getMessage().contains("UNIQUE constraint failed")) {
                erro = "Erro: Já existe um plano com este nome ('" + nome + "').";
            } else {
                erro = "Erro de banco de dados ao criar plano: " + e.getMessage();
            }

        } catch (Exception e) {
            // Qualquer outro erro inesperado
            e.printStackTrace();
            erro = "Erro inesperado ao criar plano: " + e.getMessage();
        }

        // 5. Caminho de Falha (Forward)
        System.err.println("Falha na criação do plano. Fazendo forward. Erro: " + erro);

        // Define atributos de erro e repopulação
        request.setAttribute("erro", erro);
        request.setAttribute("nome_previo", nome);
        request.setAttribute("custo_previo", custoStr);
        request.setAttribute("descricao_previo", descricao);
        request.setAttribute("maxFuncionarios_previo", maxFuncionariosStr);

        // Recarrega lista de planos (para o JSP)
        List<Plano> listaPlanos = new ArrayList<>();
        try {
            // Trata SQLException do dao.read()
            listaPlanos = dao.read();
        } catch (SQLException e) {
            e.printStackTrace(); // Loga o erro de leitura
            request.setAttribute("erro", erro + " | ERRO ADICIONAL: Falha ao recarregar a lista de planos.");
        }
        request.setAttribute("listaPlanos", listaPlanos);

        // Avisa o JSP para reabrir o modal de CREATE
        request.setAttribute("abrirModal", "create");

        // Encaminha para o JSP de planos
        request.getRequestDispatcher("/WEB-INF/pages/planos.jsp").forward(request, response);
    }
}
