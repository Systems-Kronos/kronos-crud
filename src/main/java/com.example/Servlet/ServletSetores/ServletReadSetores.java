package com.example.Servlet.ServletSetores;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.Model.Setor;
import com.example.Model.Empresa;
import com.example.dao.SetorDAO;
import com.example.dao.EmpresaDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
 * Servlet focado em LER (Read) Setores.
 * Serve como o "painel" principal (listagem com filtros) e também como uma API JSON
 * para buscar dados de um único setor (usado pelos modais).
 */
@WebServlet("/setores-crud")
public class ServletReadSetores extends HttpServlet {

    /*
     * Processa requisições GET.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Instancia DAO dentro do método para thread-safety
        SetorDAO dao = new SetorDAO();
        String pk = request.getParameter("pk");

        if (pk != null && !pk.isEmpty()) {

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            try {
                int id = Integer.parseInt(pk);
                Setor setor = dao.read(id); // Pode lançar SQLException

                if (setor != null) {
                    // Constrói a resposta JSON (usando helper 'escapeJson')
                    String json = "{"
                            + "\"id\":\"" + id + "\","
                            + "\"nome\":\"" + escapeJson(setor.getNome()) + "\","
                            + "\"qtnFuncionarios\":\"" + setor.getQntFuncionarios() + "\","
                            + "\"turnos\":\"" + escapeJson(setor.getTurnos()) + "\","
                            + "\"descricao\":\"" + escapeJson(setor.getDescricao()) + "\","
                            + "\"idEmpresa\":\"" + setor.getIdEmpresa() + "\""
                            + "}";

                    response.getWriter().write(json);
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404
                    response.getWriter().write("{\"erro\":\"Setor ID " + pk + " não encontrado.\"}");
                }
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
                response.getWriter().write("{\"erro\":\"PK inválida: " + pk + "\"}");
            } catch (SQLException e) { // <-- CORREÇÃO: Específico
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500
                response.getWriter().write("{\"erro\":\"Erro de banco de dados: " + e.getMessage() + "\"}");
            } catch (Exception e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500
                response.getWriter().write("{\"erro\":\"Erro inesperado: " + e.getMessage() + "\"}");
            }

        } else {

            List<Setor> listaSetores = new ArrayList<>(); // Inicia vazia
            List<Empresa> listaEmpresas = new ArrayList<>();
            String erro = null;

            // Coleta de parâmetros de filtro/ordenação
            String nomePesquisa = request.getParameter("pesquisa");
            String ordem = request.getParameter("ordem");

            String direction = ("decrescente".equalsIgnoreCase(ordem)) ? "DESC" : "ASC";
            String orderBy = "id"; // Default

            try {
                // 1. Carrega a lista principal de setores (filtrada)
                listaSetores = dao.read(nomePesquisa, orderBy, direction); // Pode lançar SQLException

                // 2. Carrega a lista de TODAS as empresas (para os <select> dos modais)
                EmpresaDAO empresaDAO = new EmpresaDAO(); // Assume que EmpresaDAO existe
                listaEmpresas = empresaDAO.read(); // Pode lançar SQLException

            } catch (SQLException e) {
                e.printStackTrace();
                erro = "Erro ao buscar dados do banco: " + e.getMessage();
                // As listas permanecerão vazias, o que é seguro para o JSP
            } catch (Exception e) { // Captura outros erros (ex: EmpresaDAO não encontrado)
                e.printStackTrace();
                erro = "Erro inesperado ao carregar dados: " + e.getMessage();
            }

            // Define os atributos para o JSP
            request.setAttribute("listaSetores", listaSetores);
            request.setAttribute("listaEmpresas", listaEmpresas);

            if (erro != null) {
                request.setAttribute("erro", erro);
            }

            // Encaminha para a página JSP
            request.getRequestDispatcher("/WEB-INF/pages/setores.jsp").forward(request, response);
        }
    }

    /*
     * Helper simples para escapar aspas duplas em JSON.
     */
    private String escapeJson(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("\"", "\\\"");
    }
}