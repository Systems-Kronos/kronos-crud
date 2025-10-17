package com.example.Servlet;

import com.example.Model.Administracao;
import com.example.Model.Usuario;
import com.example.dao.AdministracaoDAO;
import jakarta.servlet.ServletContext;
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
        request.setCharacterEncoding("UTF-8");
        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");

        if (nome == null || nome.trim().isEmpty() || email == null || email.trim().isEmpty() ||
                senha == null || senha.trim().isEmpty()) {
            request.setAttribute("mensagemErro", "Erro: preencha os campos obrigatorios");
            request.getRequestDispatcher("/WEB-INF/cadastro.jsp").forward(request, response);
            return;
        }

        try {
            Administracao admin = new Administracao(nome, email, senha);
            AdministracaoDAO dao = new AdministracaoDAO();
            boolean sucesso = dao.create(admin);


            if (sucesso) {
                System.out.println("Administrador cadastrado com sucesso!");
            } else {
                System.out.println("Falha ao cadastrar administrador.");
                request.setAttribute("mensagemErro", "Ocorreu um erro ao salvar os dados. Tente novamente.");
                request.getRequestDispatcher("/WEB-INF/cadastro.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensagemErro", "Ocorreu um erro inesperado no servidor");
            request.getRequestDispatcher("/WEB-INF/cadastro.jsp").forward(request, response);
        }
    }
}
