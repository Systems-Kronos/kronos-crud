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
 * Servlet focado em LER (Read) Habilidades.
 * Serve como o "painel" principal (listagem) e também como uma API JSON
 * para buscar dados de uma única habilidade (usado pelos modais).
 */
@WebServlet("/habilidades-crud")
public class ServletReadHabilidades extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Instancia o DAO dentro do método
        HabilidadesDAO dao = new HabilidadesDAO();
        String pk = request.getParameter("pk");

        if (pk != null && !pk.isEmpty()) {
            // 1. Requisição JSON
            // Usado para preencher os modais de Update e Delete dinamicamente.

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            try {
                int id = Integer.parseInt(pk);
                Habilidades habilidade = dao.read(id); // Pode lançar SQLException

                if (habilidade != null) {
                    String json = "{"
                            + "\"id\":\"" + id + "\","
                            + "\"nome\":\"" + habilidade.getNome() + "\","
                            + "\"tag\":\"" + habilidade.getTag() + "\","
                            + "\"descricao\":\"" + habilidade.getDescricao().trim() + "\""
                            + "}";

                    response.getWriter().write(json);
                } else {
                    // Habilidade não encontrada (ID válido, mas não existe)
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND); // Erro 404
                    response.getWriter().write("{\"erro\":\"Habilidade ID " + id + " não encontrada.\"}");
                }
            } catch (NumberFormatException e) {
                // ID não era um número
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // Erro 400
                response.getWriter().write("{\"erro\":\"PK inválida: " + pk + "\"}");
            } catch (SQLException e) {
                // Erro de banco de dados
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Erro 500
                response.getWriter().write("{\"erro\":\"Erro de banco de dados: " + e.getMessage() + "\"}");
            } catch (Exception e) {
                // Outro erro inesperado
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Erro 500
                response.getWriter().write("{\"erro\":\"" + e.getMessage() + "\"}");
            }

        } else {
            // Carregamento da Página (Forward JSP)
            // Carrega a lista completa (com filtros) para exibir no 'habilidades.jsp'.

            List<Habilidades> listaHabilidades = new ArrayList<>(); // Inicia vazia
            String erro = null;

            // Coleta de parâmetros de filtro/ordenação
            String nomePesquisa = request.getParameter("pesquisa");
            String ordem = request.getParameter("ordem"); // "crescente" ou "decrescente"

            // Lógica de ordenação (o DAO já valida isso, mas definimos o default aqui)
            String orderBy = "id"; // Default
            String direction = ("decrescente".equalsIgnoreCase(ordem)) ? "DESC" : "ASC";

            try {
                // Usa o método do DAO que aceita filtros/ordenação
                // O DAO (corrigido) lança SQLException
                listaHabilidades = dao.read(nomePesquisa, orderBy, direction);

            } catch (SQLException e) {
                e.printStackTrace();
                erro = "Erro ao buscar lista de habilidades: " + e.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
                erro = "Erro inesperado ao carregar dados: " + e.getMessage();
            }

            request.setAttribute("listaHabilidades", listaHabilidades);

            if (erro != null) {
                request.setAttribute("erro", erro);
            }

            // Encaminha para a página JSP
            request.getRequestDispatcher("/WEB-INF/pages/habilidades.jsp").forward(request, response);
        }
    }
}