package com.example.Servlet.ServletUsuario;

import java.io.IOException;
import java.util.List;

import com.example.Model.Empresa;
import com.example.Model.Usuario;
import com.example.dao.UsuarioDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/usuarios-crud")
public class ServletReadUsuario extends HttpServlet {
    
    // Instancia DAO
    private UsuarioDAO dao = new UsuarioDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String pk = request.getParameter("pk");

        if (pk != null && !pk.isEmpty()) {

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            try {
                Usuario usuario = dao.read(Integer.parseInt(pk));

                if (usuario != null) {

                    String json = "{"
                            + "\"id\":\"" + pk + "\","
                            + "\"nome\":\"" + usuario.getNome() + "\","
                            + "\"cpf\":\"" + usuario.getCpf() + "\","
                            + "\"senha\":\"" + usuario.getSenha() + "\","
                            + "\"genero\":\"" + usuario.getGenero() + "\","
                            + "\"status\":\"" + usuario.getStatus() + "\","
                            + "\"idSetor\":\"" + usuario.getIdSetor() + "\","
                            + "\"idSupervisor\":\"" + usuario.getIdSupervisor() + "\""
                            + "}";

                    response.getWriter().write(json);
                }
            } catch (NumberFormatException e) {
                response.getWriter().write("{\"erro\":\"PK inválida\"}");
            } catch (Exception e) {
                response.getWriter().write("{\"erro\":\"" + e.getMessage() + "\"}");
            }

        } else {

            // Pega o valor do campo de pesquisa
            String pesquisa = request.getParameter("pesquisa");

            // Pega a ordem (crescente ou decrescente)
            String ordem = request.getParameter("ordem"); // pode ser "crescente" ou "decrescente"

            String direction = "ASC"; // padrão
            if (ordem != null && ordem.equalsIgnoreCase("decrescente")) {
                direction = "DESC";
            }

            // Buscar usuários
            List<Usuario> listaUsuarios = dao.read(pesquisa, "nome", direction);

            // Salvar a lista no request para exibir no JSP
            request.setAttribute("usuarios", listaUsuarios);

            // Redireciona para o JSP
            request.getRequestDispatcher("/WEB-INF/pages/usuario.jsp").forward(request, response);
        }
    }
}
