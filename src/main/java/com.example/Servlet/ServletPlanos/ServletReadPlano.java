package com.example.Servlet.ServletPlanos;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.Model.Plano;
import com.example.dao.PlanoDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet focado em LER (Read) Planos.
 * Serve como o "painel" principal (listagem) e também como uma API JSON
 * para buscar dados de um único plano (usado pelos modais).
 */
@WebServlet("/planos-crud")
public class ServletReadPlano extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PlanoDAO dao = new PlanoDAO();
        String pk = request.getParameter("pk");

        if (pk != null && !pk.isEmpty()) {
            // Usado para preencher dinamicamente os modais de Update e Delete.

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            try {
                int id = Integer.parseInt(pk);
                Plano plano = dao.read(id); // Pode lançar SQLException

                if (plano != null) {
                    // Construção manual de JSON (substituível por Gson em projetos maiores)
                    String json = "{"
                            + "\"id\":\"" + id + "\","
                            + "\"nome\":\"" + plano.getNome() + "\","
                            + "\"custo\":\"" + plano.getCusto() + "\","
                            + "\"maxFuncionarios\":\"" + plano.getMaxFuncionarios() + "\","
                            + "\"descricao\":\"" + plano.getDescricao().trim() + "\""
                            + "}";

                    response.getWriter().write(json);
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404
                    response.getWriter().write("{\"erro\":\"Plano ID " + id + " não encontrado.\"}");
                }

            } catch (NumberFormatException e) {
                // ID não era numérico
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
                response.getWriter().write("{\"erro\":\"PK inválida: " + pk + "\"}");
            } catch (SQLException e) {
                // Erro SQL específico
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500
                response.getWriter().write("{\"erro\":\"Erro de banco de dados: " + e.getMessage() + "\"}");
            } catch (Exception e) {
                // Qualquer outro erro inesperado
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"erro\":\"" + e.getMessage() + "\"}");
            }

        } else {
            // Exibe a lista completa (com filtros) no 'planos.jsp'.

            List<Plano> listaPlanos = new ArrayList<>();
            String erro = null;

            // Parâmetros opcionais de filtro e ordenação
            String nomePesquisa = request.getParameter("pesquisa");
            String ordem = request.getParameter("ordem"); // "crescente" ou "decrescente"

            // Configuração de ordenação padrão
            String orderBy = "id";
            String direction = ("decrescente".equalsIgnoreCase(ordem)) ? "DESC" : "ASC";

            try {
                listaPlanos = dao.read(nomePesquisa, orderBy, direction);
                // O DAO corrigido não retorna null, apenas lista vazia.

            } catch (SQLException e) {
                e.printStackTrace();
                erro = "Erro ao buscar lista de planos: " + e.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
                erro = "Erro inesperado ao carregar dados: " + e.getMessage();
            }

            request.setAttribute("listaPlanos", listaPlanos);

            if (erro != null) {
                request.setAttribute("erro", erro);
            }

            // Encaminha para o JSP
            request.getRequestDispatcher("/WEB-INF/pages/planos.jsp").forward(request, response);
        }
    }
}
