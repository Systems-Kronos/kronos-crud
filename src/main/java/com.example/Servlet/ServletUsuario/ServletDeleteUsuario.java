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

@WebServlet("/usuario-delete")
public class ServletDeleteUsuario extends HttpServlet {

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
        request.setAttribute("listaUsuarios", listaUsuarios); // Nome esperado pelo JSP ('usuarios'?)

        String idParam = request.getParameter("id");
        Usuario usuarioModal = null;

        try {
            int id = Integer.parseInt(idParam);
            usuarioModal = dao.read(id);
            if (usuarioModal != null) {
                request.setAttribute("usuarioModal", usuarioModal);
                request.setAttribute("abrirModal", "delete");
            } else {
                if (erro == null) erro = "Usuário ID " + id + " não encontrado.";
            }
        } catch (NumberFormatException e) {
            erro = "ID inválido.";
            System.err.println("ID inválido ('id') delete usuário (doGet): " + idParam);
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

        UsuarioDAO dao = new UsuarioDAO();
        int id = 0;
        boolean success = false;

        try {
            String idParam = request.getParameter("id");
            id = Integer.parseInt(idParam);
            int resultado = dao.delete(id);
            if (resultado > 0) {
                success = true;
            } else {
                request.setAttribute("erro", "Não foi possível deletar (ID: " + id + "). Verifique dependências.");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("erro", "ID inválido.");
            System.err.println("ID inválido ('id') delete usuário doPost: " + request.getParameter("id"));
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro inesperado: " + e.getMessage());
        }

        if (success) {
            response.sendRedirect(request.getContextPath() + "/usuarios-crud");
        } else {
            System.err.println("Falha delete usuário ID " + id + ". Forwarding.");
            List<Usuario> listaUsuarios = null;
            try { listaUsuarios = dao.read(); } catch (Exception readEx){ listaUsuarios = new ArrayList<>(); }
            request.setAttribute("listaUsuarios", listaUsuarios); // Nome esperado pelo JSP ('usuarios'?)

            if (id > 0 && request.getAttribute("usuarioModal") == null) {
                try { request.setAttribute("usuarioModal", dao.read(id)); } catch (Exception readEx) { /* Ignora */ }
            }
            request.setAttribute("abrirModal", "delete");
            request.getRequestDispatcher("/WEB-INF/pages/usuario.jsp").forward(request, response);
        }
    }
}