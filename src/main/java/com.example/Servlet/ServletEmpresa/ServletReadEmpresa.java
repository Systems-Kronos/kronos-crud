package com.example.Servlet.ServletEmpresa;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.Model.Empresa;
import com.example.Model.Plano;
import com.example.dao.EmpresaDAO;
import com.example.dao.PlanoDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet focado em LER (Read) Empresas.
 * Serve como o "painel" principal (listagem com filtros) e também como uma API JSON
 * para buscar dados de uma única empresa (usado pelos modais).
 */
@WebServlet("/empresas-crud")
public class ServletReadEmpresa extends HttpServlet {

    /*
     * Processa requisições get.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Instancia DAO dentro do método para thread-safety
        EmpresaDAO dao = new EmpresaDAO();
        String pk = request.getParameter("pk");

        if (pk != null && !pk.isEmpty()) {

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            try {
                int id = Integer.parseInt(pk);
                Empresa empresa = dao.read(id); // Pode lançar SQLException

                if (empresa != null) {
                    // Constrói a resposta JSON
                    String json = "{"
                            + "\"id\":\"" + pk + "\","
                            + "\"nome\":\"" + escapeJson(empresa.getNome()) + "\","
                            + "\"email\":\"" + escapeJson(empresa.getEmail()) + "\","
                            + "\"cep\":\"" + escapeJson(empresa.getCep()) + "\","
                            + "\"cnpj\":\"" + escapeJson(empresa.getCnpj()) + "\","
                            + "\"telefone\":\"" + escapeJson(empresa.getTelefone()) + "\","
                            + "\"porte\":\"" + escapeJson(empresa.getPorte()) + "\","
                            + "\"horaAbertura\":\"" + empresa.getHorarioAbertura() + "\","
                            + "\"horaFechamento\":\"" + empresa.getHorarioFechamento() + "\","
                            + "\"idPlano\":\"" + empresa.getIdPlano() + "\","
                            + "\"regrasNegocios\":\"" + escapeJson(empresa.getRegraDeNegocios()) + "\""
                            + "}";

                    response.getWriter().write(json);
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404
                    response.getWriter().write("{\"erro\":\"Empresa ID " + pk + " não encontrada.\"}");
                }
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
                response.getWriter().write("{\"erro\":\"PK inválida: " + pk + "\"}");
            } catch (SQLException e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500
                response.getWriter().write("{\"erro\":\"Erro de banco de dados: " + e.getMessage() + "\"}");
            } catch (Exception e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500
                response.getWriter().write("{\"erro\":\"Erro inesperado: " + e.getMessage() + "\"}");
            }

        } else {

            List<Empresa> listaEmpresas = new ArrayList<>(); // Inicia vazia
            List<Plano> listaPlanos = new ArrayList<>();
            String erro = null;

            // Coleta de parâmetros de filtro/ordenação
            String nomePesquisa = request.getParameter("pesquisa");
            String ordem = request.getParameter("ordem");

            String direction = ("decrescente".equalsIgnoreCase(ordem)) ? "DESC" : "ASC";
            String orderBy = "id"; // Default

            try {
                // 1. Carrega a lista principal de empresas (filtrada)
                listaEmpresas = dao.read(nomePesquisa, orderBy, direction); // Pode lançar SQLException

                // 2. Carrega a lista de TODOS os planos (para os <select> dos modais)
                PlanoDAO planoDAO = new PlanoDAO();
                listaPlanos = planoDAO.read();

            } catch (SQLException e) {
                e.printStackTrace();
                erro = "Erro ao buscar dados do banco: " + e.getMessage();
                // As listas permanecerão vazias, o que é seguro para o JSP
            } catch (Exception e) { // Captura outros erros
                e.printStackTrace();
                erro = "Erro inesperado ao carregar dados: " + e.getMessage();
            }

            // Define os atributos para o JSP
            request.setAttribute("listaEmpresas", listaEmpresas);
            request.setAttribute("listaPlanos", listaPlanos);

            if (erro != null) {
                request.setAttribute("erro", erro);
            }

            // Encaminha para a página JSP
            request.getRequestDispatcher("/WEB-INF/pages/empresas.jsp").forward(request, response);
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