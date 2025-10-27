package com.example.Servlet.ServletSetores;

import com.example.dao.SetorDAO;
import com.example.Model.Setor;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

@WebServlet("/setor-create")
public class ServletCreateSetores extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String nome = request.getParameter("nome");
        String turnos = request.getParameter("turnos");
        String qtdFuncionariosStr = request.getParameter("qtnFuncionarios");
        String descricao = request.getParameter("descricao");
        String idEmpresaStr = request.getParameter("idEmpresa");

        SetorDAO dao = new SetorDAO();
        boolean success = false;

        try {
            int qtdFuncionarios = Integer.parseInt(qtdFuncionariosStr);
            int idEmpresa = Integer.parseInt(idEmpresaStr);

            Setor novoSetor = new Setor(
                    nome,
                    descricao,
                    turnos,
                    qtdFuncionarios,
                    idEmpresa
            );

            success = dao.create(novoSetor);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/setores-crud");
                return;
            } else {
                request.setAttribute("erro", "Erro ao cadastrar setor.");
            }

        } catch (IllegalArgumentException | NullPointerException | IllegalStateException e) {
            request.setAttribute("erro", "Erro: " + e.getMessage());
            request.setAttribute("nome_previo", nome);
            request.setAttribute("turnos_previo", turnos);
            request.setAttribute("qtdFuncionarios_previo", qtdFuncionariosStr);
            request.setAttribute("descricao_previo", descricao);
            request.setAttribute("idEmpresa_previo", idEmpresaStr);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro inesperado: " + e.getMessage());
        }

        if (!success) {
            System.err.println("Falha na criação do setor. Forwarding.");
            List<Setor> listaSetores = null;
            try {
                listaSetores = dao.read();
                if (listaSetores == null) listaSetores = new ArrayList<>();
            } catch (Exception readEx) {
                listaSetores = new ArrayList<>();
            }
            request.setAttribute("listaSetores", listaSetores);
            request.setAttribute("abrirModal", "create");
            request.getRequestDispatcher("/WEB-INF/pages/setores.jsp").forward(request, response);
        }
    }
}