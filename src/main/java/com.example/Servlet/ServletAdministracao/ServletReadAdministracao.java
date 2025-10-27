package com.example.Servlet.ServletAdministracao;

import com.example.dao.AdministracaoDAO;
import com.example.Model.Administracao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

@WebServlet("/admin-crud")
public class ServletReadAdministracao extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        AdministracaoDAO dao = new AdministracaoDAO();
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