package com.example.Servlet.ServletUsuario;

import com.example.dao.UsuarioDAO;
import com.example.Model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

@WebServlet("/usuario-create")
public class ServletCreateUsuario extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String nome = request.getParameter("nome");
        String cpf = request.getParameter("cpf");
        String generoStr = request.getParameter("genero");
        String status = request.getParameter("status");
        String senha = request.getParameter("senha"); // Lembre-se de CRIPTOGRAFAR
        String idSetorStr = request.getParameter("idSetor");
        String idSupervisorStr = request.getParameter("idSupervisor");
        // String cargo = request.getParameter("cargo"); // Adicione se o campo existir no form

        UsuarioDAO dao = new UsuarioDAO();
        boolean success = false;

        try {
            char genero = (generoStr != null && !generoStr.isEmpty()) ? generoStr.charAt(0) : ' ';
            int idSetor = Integer.parseInt(idSetorStr);
            int idSupervisor = 0;
            if (idSupervisorStr != null && !idSupervisorStr.trim().isEmpty()) {
                idSupervisor = Integer.parseInt(idSupervisorStr);
            }

            // Criar o objeto Usuario usando o CONSTRUTOR apropriado.
            // As validações do seu Model são disparadas aqui.
            // VERIFIQUE A ORDEM EXATA DOS PARÂMETROS NO SEU Usuario.java!
            Usuario novoUsuario = new Usuario(
                    nome,
                    cpf,
                    genero,
                    status,
                    senha,
                    idSetor,
                    idSupervisor,
                    null
            );
            // novoUsuario.setCargo(cargo); // Adicione se necessário

            success = dao.create(novoUsuario);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/usuarios-crud");
                return;
            } else {
                request.setAttribute("erro", "Erro ao cadastrar usuário.");
            }

        } catch (IllegalArgumentException | NullPointerException | IllegalStateException | StringIndexOutOfBoundsException  e) {
            request.setAttribute("erro", "Erro: " + e.getMessage());
            request.setAttribute("nome_previo", nome);
            request.setAttribute("cpf_previo", cpf);
            request.setAttribute("genero_previo", generoStr);
            request.setAttribute("status_previo", status);
            request.setAttribute("idSetor_previo", idSetorStr);
            request.setAttribute("idSupervisor_previo", idSupervisorStr);
            // request.setAttribute("cargo_previo", cargo); // Adicione se necessário

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro inesperado: " + e.getMessage());
        }

        if (!success) {
            System.err.println("Falha na criação do usuário. Forwarding.");
            List<Usuario> listaUsuarios = null;
            try {
                listaUsuarios = dao.read();
                if (listaUsuarios == null) listaUsuarios = new ArrayList<>();
            } catch (Exception readEx){
                listaUsuarios = new ArrayList<>();
            }
            request.setAttribute("listaUsuarios", listaUsuarios); // Nome esperado pelo JSP ('usuarios'?)
            request.setAttribute("abrirModal", "create");
            request.getRequestDispatcher("/WEB-INF/pages/usuario.jsp").forward(request, response);
        }
    }
}