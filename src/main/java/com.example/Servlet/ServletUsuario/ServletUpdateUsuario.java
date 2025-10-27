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

@WebServlet("/usuario-update")
public class ServletUpdateUsuario extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UsuarioDAO dao = new UsuarioDAO();
        List<Usuario> listaUsuarios = null;
        String erro = null;

        try {
            listaUsuarios = dao.read();
            if (listaUsuarios == null) listaUsuarios = new ArrayList<>();
        } catch(Exception e){
            e.printStackTrace();
            erro = "Erro ao carregar lista de usuários.";
            listaUsuarios = new ArrayList<>();
        }
        request.setAttribute("listaUsuarios", listaUsuarios);

        String idParam = request.getParameter("id");
        Usuario usuarioModal = null;

        try {
            int id = Integer.parseInt(idParam);
            usuarioModal = dao.read(id);
            if (usuarioModal != null) {
                request.setAttribute("usuarioModal", usuarioModal);
                request.setAttribute("abrirModal", "update");
            } else {
                if (erro == null) erro = "Usuário ID " + id + " não encontrado.";
            }
        } catch (NumberFormatException e) {
            erro = "ID inválido.";
            System.err.println("ID inválido ('id') update usuário (doGet): " + idParam);
        } catch (Exception e) {
            e.printStackTrace();
            if (erro == null) erro = "Erro ao buscar dados do usuário.";
        }

        if (erro != null) {
            request.setAttribute("erro", erro);
        }
        request.getRequestDispatcher("/WEB-INF/pages/usuarios.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        UsuarioDAO dao = new UsuarioDAO();
        int id = 0;
        boolean success = false;

        String idParam = request.getParameter("id");
        String nome = request.getParameter("nome");
        String cpf = request.getParameter("cpf");
        String generoStr = request.getParameter("genero");
        String status = request.getParameter("status");
        String novaSenha = request.getParameter("senha");
        String idSetorStr = request.getParameter("idSetor");
        String idSupervisorStr = request.getParameter("idSupervisor");

        try {
            id = Integer.parseInt(idParam);
            char genero = (generoStr != null && !generoStr.isEmpty()) ? generoStr.charAt(0) : ' ';
            int idSetor = Integer.parseInt(idSetorStr);
            int idSupervisor = 0;
            if (idSupervisorStr != null && !idSupervisorStr.trim().isEmpty()) {
                idSupervisor = Integer.parseInt(idSupervisorStr);
            }

            Usuario usuarioParaAtualizar = dao.read(id);
            if (usuarioParaAtualizar == null) {
                throw new Exception("Usuário ID " + id + " não encontrado.");
            }

            usuarioParaAtualizar.setNome(nome);
            usuarioParaAtualizar.setCpf(cpf);
            usuarioParaAtualizar.setGenero(genero);
            usuarioParaAtualizar.setStatus(status);
            usuarioParaAtualizar.setIdSetor(idSetor);
            usuarioParaAtualizar.setIdSupervisor(idSupervisor);

            if (novaSenha != null && !novaSenha.trim().isEmpty()) {
                usuarioParaAtualizar.setSenha(novaSenha);
            }

            int resultado = dao.update(usuarioParaAtualizar);

            if (resultado > 0) {
                success = true;
            } else {
                request.setAttribute("erro", "Não foi possível atualizar (ID: " + id + ").");
            }

        } catch (IllegalArgumentException | NullPointerException | IllegalStateException | StringIndexOutOfBoundsException e) {
            request.setAttribute("erro", "Erro: " + e.getMessage());
            request.setAttribute("nome_previo", nome);
            request.setAttribute("cpf_previo", cpf);
            request.setAttribute("genero_previo", generoStr);
            request.setAttribute("status_previo", status);
            request.setAttribute("idSetor_previo", idSetorStr);
            request.setAttribute("idSupervisor_previo", idSupervisorStr);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro inesperado: " + e.getMessage());
        }

        if (success) {
            response.sendRedirect(request.getContextPath() + "/usuarios-crud");
        } else {
            System.err.println("Falha update usuário ID " + id + ". Forwarding.");
            List<Usuario> listaUsuarios = null;
            try { listaUsuarios = dao.read(); } catch (Exception readEx){ listaUsuarios = new ArrayList<>(); }
            request.setAttribute("listaUsuarios", listaUsuarios);

            if (id > 0 && request.getAttribute("usuarioModal") == null) {
                try { request.setAttribute("usuarioModal", dao.read(id)); } catch (Exception readEx) { /* Ignora */ }
            }
            request.setAttribute("abrirModal", "update");
            request.getRequestDispatcher("/WEB-INF/pages/usuario.jsp").forward(request, response);
        }
    }
}