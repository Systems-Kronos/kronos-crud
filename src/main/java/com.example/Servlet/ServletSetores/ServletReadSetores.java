package com.example.Servlet.ServletSetores;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.Model.Setor;
import com.example.dao.SetorDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/setores-crud")
public class ServletReadSetores extends HttpServlet {

    // Instanciando DAO
    private SetorDAO dao = new SetorDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pk = request.getParameter("pk");

        if (pk != null && !pk.isEmpty()) {

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            try {
                Setor setor = dao.read(Integer.parseInt(pk));

                if (setor != null) {

                    String json = "{"
                            + "\"id\":\"" + pk + "\","
                            + "\"nome\":\"" + setor.getNome() + "\","
                            + "\"qtnFuncionarios\":\"" + setor.getQntFuncionarios() + "\","
                            + "\"turnos\":\"" + setor.getTurnos() + "\","
                            + "\"descricao\":\"" + setor.getDescricao().trim() + "\","
                            + "\"empresa\":\"" + setor.getEmpresa().getNome() + "\""
                            + "}";

                    response.getWriter().write(json);
                }
            } catch (NumberFormatException e) {
                response.getWriter().write("{\"erro\":\"PK inválida\"}");
            } catch (Exception e) {
                response.getWriter().write("{\"erro\":\"" + e.getMessage() + "\"}");
            }

        } else {

            List<Setor> listaSetores = null;
            String erro = null;

            // --- Handle Search/Filter/Sort ---
            String nomePesquisa = request.getParameter("pesquisa");
            String ordem = request.getParameter("ordem"); // crescente ou decrescente

            // Determine orderBy column based on your logic if needed, default to ID
            String orderBy = "id"; // Default, adjust if your JSP sends a sort column
            String direction = ("decrescente".equalsIgnoreCase(ordem)) ? "DESC" : "ASC";

            try {
                // Use the DAO method that accepts filters/sorting
                listaSetores = dao.read(nomePesquisa, orderBy, direction);

                if (listaSetores == null) {
                    erro = "Lista de administradores não carregada.";
                    listaSetores = new ArrayList<>();
                }

            } catch (Exception e) {
                e.printStackTrace();
                erro = "Erro ao buscar lista de administradores.";
                listaSetores = new ArrayList<>();
            }

            request.setAttribute("listaSetores", listaSetores);

            if (erro != null) {
                request.setAttribute("erro", erro);
            }

            request.getRequestDispatcher("/WEB-INF/pages/setores.jsp").forward(request, response);
        }
    }
}