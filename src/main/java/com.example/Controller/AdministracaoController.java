package com.example.Controller;

import com.example.Model.Administracao;
import com.example.dao.AdministracaoDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/AdministracaoController")
public class AdministracaoController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException{

        String acao = request.getParameter("acao");

        if ("inserir".equals(acao)) {
            inserirAdministracao(request, response);
        } else if ("listar".equals(acao)) {
            listarAdministracoes(request, response);
        } else {
            response.getWriter().println("❌ Ação inválida: " + acao);
        }
    }

    private void inserirAdministracao(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            String nome = request.getParameter("nome");
            String email = request.getParameter("email");
            String senha = request.getParameter("senha");
            String codigoAcesso = request.getParameter("codigoAcesso");

            Administracao admin = new Administracao( nome, email, senha, codigoAcesso);
            AdministracaoDAO dao = new AdministracaoDAO();

            boolean sucesso = dao.inserir(admin);

            if (sucesso) {
                response.sendRedirect("empresa/listar-empresa.jsp");
            } else {
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listarAdministracoes(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            AdministracaoDAO dao = new AdministracaoDAO();
            List<Administracao> lista = dao.read();

            request.setAttribute("listaAdministracao", lista);
            RequestDispatcher rd = request.getRequestDispatcher("administracao-lista.jsp");
            rd.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
