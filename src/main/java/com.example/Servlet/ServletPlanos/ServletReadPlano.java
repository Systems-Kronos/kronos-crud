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

            try {

                listaPlanos = dao.read(); 

                if (listaPlanos == null) {
                    erro = "Lista não carregada.";
                    listaPlanos = new ArrayList<>();
                }

            } catch (Exception e) {
                e.printStackTrace();
                erro = "Erro ao buscar lista.";
                listaPlanos = new ArrayList<>();
            }

            // Passa a lista (ou vazia) para o JSP
            request.setAttribute("listaPlanos", listaPlanos); // Nome usado no JSP

            // Passa erro, se houver
            if (erro != null) {
                request.setAttribute("erro", erro);
            }

            // Encaminha para o JSP correto
            request.getRequestDispatcher("/WEB-INF/pages/planos.jsp").forward(request, response);
        }
    }
}