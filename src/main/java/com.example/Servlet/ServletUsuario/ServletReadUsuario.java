package com.example.Servlet.ServletUsuario;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import com.example.Model.Usuario;
import com.example.dao.UsuarioDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/usuarios-crud")
public class ServletReadUsuario extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UsuarioDAO dao = new UsuarioDAO();
        List<Usuario> listaUsuarios = null;
        String erro = null;

        try {
            String pesquisa = request.getParameter("pesquisa");
            String ordem = request.getParameter("ordem");

            String direction = "ASC";
            if (ordem != null && ordem.equalsIgnoreCase("decrescente")) {
                direction = "DESC";
            }
            String orderBy = "nome";

            listaUsuarios = dao.read(pesquisa, orderBy, direction);

            if (listaUsuarios == null) {
                erro = "Lista de usuários não carregada.";
                listaUsuarios = new ArrayList<>();
            }

        } catch (Exception e) {
            e.printStackTrace();
            erro = "Erro ao buscar lista de usuários.";
            listaUsuarios = new ArrayList<>();
        }

        request.setAttribute("usuarios", listaUsuarios);

        if (erro != null) {
            request.setAttribute("erro", erro);
        }

        request.getRequestDispatcher("/WEB-INF/pages/usuario.jsp").forward(request, response);
    }
}