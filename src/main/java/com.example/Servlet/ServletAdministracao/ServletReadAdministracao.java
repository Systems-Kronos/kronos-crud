package com.example.Servlet.ServletAdministracao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.Model.Administracao;
import com.example.dao.AdministracaoDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin-crud")
public class ServletReadAdministracao extends HttpServlet {

    //  Instanciando DAO
    private AdministracaoDAO dao = new AdministracaoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String pk = request.getParameter("pk");

        if (pk != null && !pk.isEmpty()) {

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            try {
                Administracao admin = dao.read(Integer.parseInt(pk));

                if (admin != null) {

                    String json = "{"
                            + "\"id\":\"" + pk + "\","
                            + "\"nome\":\"" + admin.getNome() + "\","
                            + "\"email\":\"" + admin.getEmail() + "\","
                            + "\"senha\":\"" + admin.getSenha() + "\""
                            + "}";

                    response.getWriter().write(json);
                }
            } catch (NumberFormatException e) {
                response.getWriter().write("{\"erro\":\"PK inválida\"}");
            } catch (Exception e) {
                response.getWriter().write("{\"erro\":\"" + e.getMessage() + "\"}");
            }

        } else {
                
            List<Administracao> listaAdmins = null;
            String erro = null;

            // --- Handle Search/Filter/Sort ---
            String nomePesquisa = request.getParameter("pesquisa");
            String ordem = request.getParameter("ordem"); // crescente ou decrescente
            
            // Determine orderBy column based on your logic if needed, default to ID
            String orderBy = "id"; // Default, adjust if your JSP sends a sort column
            String direction = ("decrescente".equalsIgnoreCase(ordem)) ? "DESC" : "ASC";

            try {
                // Use the DAO method that accepts filters/sorting
                listaAdmins = dao.read(nomePesquisa, orderBy, direction);

                if (listaAdmins == null) {
                    erro = "Lista de administradores não carregada.";
                    listaAdmins = new ArrayList<>();
                }

            } catch (Exception e) {
                e.printStackTrace();
                erro = "Erro ao buscar lista de administradores.";
                listaAdmins = new ArrayList<>();
            }

            request.setAttribute("listaAdmins", listaAdmins);

            if (erro != null) {
                request.setAttribute("erro", erro);
            }

            request.getRequestDispatcher("/WEB-INF/pages/administrador.jsp").forward(request, response);
        }
    }
}