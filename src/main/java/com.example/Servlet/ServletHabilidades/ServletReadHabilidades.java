package com.example.Servlet.ServletHabilidades;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.Model.Habilidades;
import com.example.dao.HabilidadesDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/habilidades-crud")
public class ServletReadHabilidades extends HttpServlet {

    // Instanciando DAO
    private HabilidadesDAO dao = new HabilidadesDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pk = request.getParameter("pk");

        if (pk != null && !pk.isEmpty()) {

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            try {
                Habilidades habilidade = dao.read(Integer.parseInt(pk));

                if (habilidade != null) {

                    String json = "{"
                            + "\"id\":\"" + pk + "\","
                            + "\"nome\":\"" + habilidade.getNome() + "\","
                            + "\"tag\":\"" + habilidade.getTag() + "\","
                            + "\"descricao\":\"" + habilidade.getDescricao().trim() + "\""
                            + "}";

                    response.getWriter().write(json);
                }
            } catch (NumberFormatException e) {
                response.getWriter().write("{\"erro\":\"PK inválida\"}");
            } catch (Exception e) {
                response.getWriter().write("{\"erro\":\"" + e.getMessage() + "\"}");
            }

        } else {

            List<Habilidades> listaHabilidades = null;
            String erro = null;

            // --- Handle Search/Filter/Sort ---
            String nomePesquisa = request.getParameter("pesquisa");
            String ordem = request.getParameter("ordem"); // crescente ou decrescente
            
            // Determine orderBy column based on your logic if needed, default to ID
            String orderBy = "id"; // Default, adjust if your JSP sends a sort column
            String direction = ("decrescente".equalsIgnoreCase(ordem)) ? "DESC" : "ASC";

            try {
                // Use the DAO method that accepts filters/sorting
                listaHabilidades = dao.read(nomePesquisa, orderBy, direction);

                if (listaHabilidades == null) {
                    erro = "Lista de habilidades não carregada.";
                    listaHabilidades = new ArrayList<>();
                }

            } catch (Exception e) {
                e.printStackTrace();
                erro = "Erro ao buscar lista de habilidades.";
                listaHabilidades = new ArrayList<>();
            }

            request.setAttribute("listaHabilidades", listaHabilidades);

            if (erro != null) {
                request.setAttribute("erro", erro);
            }

            request.getRequestDispatcher("/WEB-INF/pages/habilidades.jsp").forward(request, response);
        }
    }
}