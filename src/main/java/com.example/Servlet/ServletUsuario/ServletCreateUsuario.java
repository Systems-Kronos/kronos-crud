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
        String senha = request.getParameter("senha");
        String idSetorStr = request.getParameter("idSetor");
        String idSupervisorStr = request.getParameter("idSupervisor");
        String cargo = request.getParameter("cargo");


        UsuarioDAO dao = new UsuarioDAO();

        try {

            char genero = (generoStr != null && !generoStr.isEmpty()) ? generoStr.charAt(0) : ' ';
            int idSetor = Integer.parseInt(idSetorStr);
            int idSupervisor = 0;
            if (idSupervisorStr != null && !idSupervisorStr.trim().isEmpty()) {
                idSupervisor = Integer.parseInt(idSupervisorStr);
            }

            Usuario novoUsuario = new Usuario(
                    nome,
                    cpf,
                    genero,
                    status,
                    senha,
                    idSetor,
                    idSupervisor,
                    cargo
            );


            boolean sucesso = dao.create(novoUsuario);

            if (sucesso) {
                System.out.println("Usuário criado com sucesso!");

                response.sendRedirect(request.getContextPath() + "/usuarios-crud");
                return;
            } else {

                request.setAttribute("erro", "Erro ao cadastrar usuário. Verifique se o CPF já existe ou se os IDs de Setor/Supervisor são válidos.");
            }

        } catch (IllegalArgumentException | NullPointerException | IllegalStateException | StringIndexOutOfBoundsException e) {

            request.setAttribute("erro", "Erro de validação: " + e.getMessage());


            request.setAttribute("nome_previo", nome);
            request.setAttribute("cpf_previo", cpf);
            request.setAttribute("genero_previo", generoStr);
            request.setAttribute("status_previo", status);
            request.setAttribute("idSetor_previo", idSetorStr);
            request.setAttribute("idSupervisor_previo", idSupervisorStr);
            request.setAttribute("cargo_previo", cargo);


        }

        System.err.println("Falha na criação do usuário. Fazendo forward para o JSP com erro.");


        List<Usuario> listaUsuarios = dao.read();
        request.setAttribute("listaUsuarios", listaUsuarios);


        request.setAttribute("abrirModal", "create");


        request.getRequestDispatcher("/WEB-INF/usuario.jsp").forward(request, response);
    }
}