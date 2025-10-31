package com.example.Servlet.ServletUsuario;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.Model.Habilidades;
import com.example.Model.Usuario;
import com.example.dao.HabilidadesDAO;
import com.example.dao.UsuarioDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet focado em LER (Read) Usuários.
 * Serve como o "painel" principal (listagem com filtros) e também como uma API JSON
 * para buscar dados de um único usuário (usado pelos modais).
 */
@WebServlet("/usuarios-crud")
public class ServletReadUsuario extends HttpServlet {

    /*
     * Processa requisições GET.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UsuarioDAO dao = new UsuarioDAO();
        String pk = request.getParameter("pk");

        if (pk != null && !pk.isEmpty()) {

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            try {
                int id = Integer.parseInt(pk);
                // Usa o dao.read(id) que NÃO carrega habilidades (mais eficiente p/ modal)
                Usuario usuario = dao.read(id); // Pode lançar SQLException

                if (usuario != null) {
                    // Busca os IDs das habilidades do usuário
                    List<Integer> idsHabilidades = dao.getHabilidadeIdsPorUsuario(id);
                    String habilidadesJson = idsHabilidades.toString();

                    // Constrói a resposta JSON
                    String json = "{"
                            + "\"id\":\"" + id + "\","
                            + "\"nome\":\"" + escapeJson(usuario.getNome()) + "\","
                            + "\"cpf\":\"" + escapeJson(usuario.getCpf()) + "\","
                            + "\"telefone\":\"" + escapeJson(usuario.getTelefone()) + "\","
                            + "\"senha\":\"" + usuario.getSenha() + "\","
                            + "\"genero\":\"" + (usuario.getGenero() != null ? usuario.getGenero() : "") + "\","
                            + "\"cargo\":\"" + escapeJson(usuario.getCargo()) + "\","
                            + "\"status\":\"" + escapeJson(usuario.getStatus()) + "\","
                            + "\"idSetor\":\"" + usuario.getIdSetor() + "\","
                            + "\"idSupervisor\":\"" + usuario.getIdSupervisor() + "\","
                            + "\"habilidadesDoUsuario\": " + habilidadesJson
                            + "}";
                    response.getWriter().write(json);
                } else {
                    // Usuário não encontrado
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404
                    response.getWriter().write("{\"erro\":\"Usuário ID " + id + " não encontrado.\"}");
                }
            } catch (NumberFormatException e) {
                // ID não era um número
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
                response.getWriter().write("{\"erro\":\"PK inválida: " + pk + "\"}");
            } catch (SQLException e) {
                // Erro de banco de dados
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500
                response.getWriter().write("{\"erro\":\"Erro de banco de dados: " + e.getMessage() + "\"}");
            } catch (Exception e) {
                // Outro erro inesperado
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500
                response.getWriter().write("{\"erro\":\"Erro inesperado: " + e.getMessage() + "\"}");
            }

        } else {
            List<Usuario> listaUsuarios = new ArrayList<>(); // Inicia vazia
            List<Habilidades> todasAsHabilidades = new ArrayList<>();
            String erro = null;

            try {
                // Coleta de parâmetros de filtro/ordenação
                String pesquisa = request.getParameter("pesquisa");
                String ordem = request.getParameter("ordem"); // "crescente" ou "decrescente"

                String direction = ("decrescente".equalsIgnoreCase(ordem)) ? "DESC" : "ASC";
                // Default 'id' para consistência (DAO fará whitelisting)
                String orderBy = "id";
                // Se o JSP enviar um 'orderBy' (ex: &orderBy=nome), o DAO irá usá-lo.

                // Usa o método do DAO que aceita filtros E carrega habilidades (JOINs)
                listaUsuarios = dao.read(pesquisa, orderBy, direction); // Pode lançar SQLException

                // Carrega a lista de TODAS as habilidades disponíveis
                HabilidadesDAO habilidadesDAO = new HabilidadesDAO();
                todasAsHabilidades = habilidadesDAO.read();

            } catch (SQLException e) {
                e.printStackTrace();
                erro = "Erro ao buscar lista de usuários: " + e.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
                erro = "Erro inesperado ao carregar dados: " + e.getMessage();
            }

            request.setAttribute("listaUsuarios", listaUsuarios);
            request.setAttribute("todasAsHabilidades", todasAsHabilidades);

            if (erro != null) {
                request.setAttribute("erro", erro);
            }

            // Encaminha para a página JSP
            request.getRequestDispatcher("/WEB-INF/pages/usuario.jsp").forward(request, response);
        }
    }

    /*
     * Helper simples para escapar aspas duplas em JSON.
     * Idealmente, use uma biblioteca (Gson/Jackson).
     */
    private String escapeJson(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("\"", "\\\"");
    }
}