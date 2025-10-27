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

            try {
                listaAdmins = dao.read(); // Busca a lista

                if (listaAdmins == null) {
                    // Opcional: Tratar DAO retornando null
                    System.err.println("DAO retornou lista nula de administradores.");
                    erro = "Não foi possível carregar a lista de administradores.";
                    listaAdmins = new ArrayList<>(); // Garante lista vazia no JSP
                }

            } catch (Exception e) {
                e.printStackTrace();
                erro = "Erro ao buscar a lista de administradores.";
                listaAdmins = new ArrayList<>(); // Garante lista vazia no JSP em caso de erro
            }

            request.setAttribute("listaAdmins", listaAdmins); // Envia a lista (ou vazia)

            if (erro != null) {
                request.setAttribute("erro", erro); // Envia o erro, se houver
            }

            // Encaminha para o JSP
            request.getRequestDispatcher("/WEB-INF/pages/administrador.jsp").forward(request, response);
        }
    }
}