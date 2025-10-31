package com.example.Servlet.ServletAdministracao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.Model.Administracao;
import com.example.dao.AdministracaoDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet focado em LER (Read) Administradores.
 * Serve como o "painel" principal (listagem com filtros) e também como uma API JSON
 * para buscar dados de um único administrador (usado pelos modais).
 */
@WebServlet("/admin-crud")
public class ServletReadAdministracao extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Instancia o DAO dentro do método para ser thread-safe
        AdministracaoDAO dao = new AdministracaoDAO();
        String pk = request.getParameter("pk"); // Primary Key (ID) para busca individual

        if (pk != null && !pk.isEmpty()) {

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            try {
                int id = Integer.parseInt(pk);
                Administracao admin = dao.read(id); // Pode lançar SQLException

                if (admin != null) {
                    // Constrói a resposta JSON manualmente
                    String json = "{"
                            + "\"id\":\"" + id + "\","
                            + "\"nome\":\"" + escapeJson(admin.getNome()) + "\","
                            + "\"email\":\"" + escapeJson(admin.getEmail()) + "\","
                            + "\"senha\":\"" + admin.getSenha() + "\""
                            + "}";

                    response.getWriter().write(json);
                } else {
                    // Admin não encontrado (ID válido, mas não existe)
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND); // Erro 404
                    response.getWriter().write("{\"erro\":\"Administrador ID " + id + " não encontrado.\"}");
                }

            } catch (NumberFormatException e) {
                // ID não era um número
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // Erro 400
                response.getWriter().write("{\"erro\":\"PK inválida: " + pk + "\"}");

            } catch (SQLException e) { // <-- CORREÇÃO: Tratamento específico
                // Erro de banco de dados
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Erro 500
                response.getWriter().write("{\"erro\":\"Erro de banco de dados: " + e.getMessage() + "\"}");

            } catch (Exception e) {
                // Outro erro inesperado
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Erro 500
                response.getWriter().write("{\"erro\":\"Erro inesperado: " + e.getMessage() + "\"}");
            }

        } else {
            // Carrega a lista completa (com filtros) para exibir no 'administrador.jsp'.
            List<Administracao> listaAdmins = new ArrayList<>(); // Inicia vazia
            String erro = null;

            // Coleta de parâmetros de filtro/ordenação
            String nomePesquisa = request.getParameter("pesquisa");
            String ordem = request.getParameter("ordem"); // "crescente" ou "decrescente"

            // Lógica de ordenação
            String orderBy = "id"; // Default
            String direction = ("decrescente".equalsIgnoreCase(ordem)) ? "DESC" : "ASC";

            try {
                // Usa o método do DAO que aceita filtros/ordenação
                listaAdmins = dao.read(nomePesquisa, orderBy, direction);

            } catch (SQLException e) {
                e.printStackTrace();
                erro = "Erro ao buscar lista de administradores: " + e.getMessage();
                // A listaAdmins permanecerá vazia, o que é correto para o JSP.
            } catch (Exception e) {
                e.printStackTrace();
                erro = "Erro inesperado ao carregar dados: " + e.getMessage();
                // A listaAdmins permanecerá vazia.
            }

            // Define os atributos para o JSP
            request.setAttribute("listaAdmins", listaAdmins);
            if (erro != null) {
                request.setAttribute("erro", erro);
            }

            // Encaminha para a página JSP
            request.getRequestDispatcher("/WEB-INF/pages/administrador.jsp").forward(request, response);
        }
    }

    /*
     * Helper method to escape double quotes in JSON strings.
     * A proper JSON library (Gson/Jackson) handles this automatically.
     */
    private String escapeJson(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("\"", "\\\"");
    }
}