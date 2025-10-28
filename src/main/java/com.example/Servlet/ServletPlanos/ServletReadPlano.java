package com.example.Servlet.ServletPlanos; // Verifique o pacote

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.Model.Plano;      // Verifique o import
import com.example.dao.PlanoDAO;     // Verifique o import

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
Servlet focado SOMENTE em LER (Read) a lista de Planos.
**/

@WebServlet("/planos-crud") // URL principal
public class ServletReadPlano extends HttpServlet {

    // Instanciando DAO
    private PlanoDAO dao = new PlanoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pk = request.getParameter("pk");

        if (pk != null && !pk.isEmpty()) {

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            try {
                Plano plano = dao.read(Integer.parseInt(pk));

                if (plano != null) {

                    String json = "{"
                            + "\"id\":\"" + pk + "\","
                            + "\"nome\":\"" + plano.getNome() + "\","
                            + "\"custo\":\"" + plano.getCusto() + "\","
                            + "\"maxFuncionarios\":\"" + plano.getMaxFuncionarios() + "\","
                            + "\"descricao\":\"" + plano.getDescricao().trim() + "\""
                            + "}";

                    response.getWriter().write(json);
                }
            } catch (NumberFormatException e) {
                response.getWriter().write("{\"erro\":\"PK inválida\"}");
            } catch (Exception e) {
                response.getWriter().write("{\"erro\":\"" + e.getMessage() + "\"}");
            }

        } else {
        
            List<Plano> listaPlanos = null; 
            String erro = null;

            // --- Handle Search/Filter/Sort ---
            String nomePesquisa = request.getParameter("pesquisa");
            String ordem = request.getParameter("ordem"); // crescente ou decrescente

            // Determine orderBy column based on your logic if needed, default to ID
            String orderBy = "id"; // Default, adjust if your JSP sends a sort column
            String direction = ("decrescente".equalsIgnoreCase(ordem)) ? "DESC" : "ASC";

            try {
                // Use the DAO method that accepts filters/sorting
                listaPlanos = dao.read(nomePesquisa, orderBy, direction);

                if (listaPlanos == null) {
                    erro = "Lista de administradores não carregada.";
                    listaPlanos = new ArrayList<>();
                }

            } catch (Exception e) {
                e.printStackTrace();
                erro = "Erro ao buscar lista de administradores.";
                listaPlanos = new ArrayList<>();
            }

            request.setAttribute("listaPlanos", listaPlanos);

            if (erro != null) {
                request.setAttribute("erro", erro);
            }

            request.getRequestDispatcher("/WEB-INF/pages/planos.jsp").forward(request, response);
        }
    }
}