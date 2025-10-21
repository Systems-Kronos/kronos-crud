package com.example.Servlet.ServletUsuario;

import com.example.Model.Usuario;
import com.example.dao.UsuarioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@WebServlet("/usuarios-crud")
public class ServletReadUsuario extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Pega o valor do campo de pesquisa
        String pesquisa = request.getParameter("pesquisa");

        // Pega a ordem (crescente ou decrescente)
        String ordem = request.getParameter("ordem"); // pode ser "crescente" ou "decrescente"

        String direction = "ASC"; // padrão
        if (ordem != null && ordem.equalsIgnoreCase("decrescente")) {
            direction = "DESC";
        }

        // Criar DAO e buscar usuários
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        List<Usuario> listaUsuarios = usuarioDAO.read(pesquisa, "nome", direction);

        // Salvar a lista no request para exibir no JSP
        request.setAttribute("usuarios", listaUsuarios);

        // Redireciona para o JSP
        request.getRequestDispatcher("/WEB-INF/usuario.jsp").forward(request, response);
    }
}
