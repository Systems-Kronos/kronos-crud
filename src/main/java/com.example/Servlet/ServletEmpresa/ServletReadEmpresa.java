package com.example.Servlet.ServletEmpresa;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List; // Para lista vazia

import com.example.Model.Empresa;
import com.example.dao.EmpresaDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet focado SOMENTE em LER (Read) a lista de Empresas.
 */
@WebServlet("/empresas-crud") // URL principal
public class ServletReadEmpresa extends HttpServlet {

    // Instanciando DAO
    private EmpresaDAO dao = new EmpresaDAO();


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pk = request.getParameter("pk");

        if (pk != null && !pk.isEmpty()) {

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            try {
                Empresa empresa = dao.read(Integer.parseInt(pk));

                if (empresa != null) {

                    String json = "{"
                            + "\"id\":\"" + pk + "\","
                            + "\"nome\":\"" + empresa.getNome() + "\","
                            + "\"email\":\"" + empresa.getEmail() + "\","
                            + "\"cep\":\"" + empresa.getCep() + "\","
                            + "\"cnpj\":\"" + empresa.getCnpj() + "\","
                            + "\"telefone\":\"" + empresa.getTelefone() + "\","
                            + "\"porte\":\"" + empresa.getPorte() + "\","
                            + "\"horaAbertura\":\"" + empresa.getHorarioAbertura() + "\","
                            + "\"horaFechamento\":\"" + empresa.getHorarioFechamento() + "\","
                            + "\"regrasNegocios\":\"" + empresa.getRegraDeNegocios().trim() + "\""
                            + "}";

                    response.getWriter().write(json);
                }
            } catch (NumberFormatException e) {
                response.getWriter().write("{\"erro\":\"PK inválida\"}");
            } catch (Exception e) {
                response.getWriter().write("{\"erro\":\"" + e.getMessage() + "\"}");
            }

        } else {

            List<Empresa> listaEmpresas = null;
            String erro = null;

            // --- Handle Search/Filter/Sort ---
            String nomePesquisa = request.getParameter("pesquisa");
            String ordem = request.getParameter("ordem"); // crescente ou decrescente

            // Determine orderBy column based on your logic if needed, default to ID
            String orderBy = "id"; // Default, adjust if your JSP sends a sort column
            String direction = ("decrescente".equalsIgnoreCase(ordem)) ? "DESC" : "ASC";

            try {
                // Use the DAO method that accepts filters/sorting
                listaEmpresas = dao.read(nomePesquisa, orderBy, direction);

                if (listaEmpresas == null) {
                    erro = "Lista de administradores não carregada.";
                    listaEmpresas = new ArrayList<>();
                }

            } catch (Exception e) {
                e.printStackTrace();
                erro = "Erro ao buscar lista de administradores.";
                listaEmpresas = new ArrayList<>();
            }

            request.setAttribute("listaEmpresas", listaEmpresas);

            if (erro != null) {
                request.setAttribute("erro", erro);
            }

            request.getRequestDispatcher("/WEB-INF/pages/empresas.jsp").forward(request, response);
        }
    }
}