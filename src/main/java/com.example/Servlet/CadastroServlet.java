package com.example.Servlet;

import com.example.Model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/cadastro")
public class CadastroServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/cadastro.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String telefone = request.getParameter("telefone");
        String senha = request.getParameter("senha");
        String cpf = request.getParameter("cpf");
        String genero = request.getParameter("genero");

        if(nome == null || nome.trim().isEmpty() || email == null || email.trim().isEmpty() || genero == null
                || genero.trim().isEmpty() || telefone == null || telefone.trim().isEmpty()
                || senha == null || senha.trim().isEmpty() || cpf == null || cpf.trim().isEmpty()
                || genero == null || genero.trim().isEmpty()) {
            request.setAttribute("mensagemErro", "Erro: preencha os campos obrigatorios" );
        }



    }
}
