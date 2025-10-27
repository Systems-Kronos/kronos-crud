package com.example.Servlet.ServletSetores;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.Model.Setor;
import com.example.dao.SetorDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/setores-crud")
public class ServletReadSetores extends HttpServlet {

    // Instanciando DAO
    private SetorDAO dao = new SetorDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pk = request.getParameter("pk");

        if (pk != null && !pk.isEmpty()) {

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            try {
                Setor setor = dao.read(Integer.parseInt(pk));

                if (setor != null) {

                    String json = "{"
                            + "\"id\":\"" + pk + "\","
                            + "\"nome\":\"" + setor.getNome() + "\","
                            + "\"qtnFuncionarios\":\"" + setor.getQntFuncionarios() + "\","
                            + "\"turnos\":\"" + setor.getTurnos() + "\","
                            + "\"descricao\":\"" + setor.getDescricao().trim() + "\","
                            + "\"idEmpresa\":\"" + setor.getIdEmpresa() + "\""
                            + "}";

                    response.getWriter().write(json);
                }
            } catch (NumberFormatException e) {
                response.getWriter().write("{\"erro\":\"PK inválida\"}");
            } catch (Exception e) {
                response.getWriter().write("{\"erro\":\"" + e.getMessage() + "\"}");
            }

        } else {

            List<Setor> listaSetores = null;
            String erro = null;

            try {
                listaSetores = dao.read();
                if (listaSetores == null) {
                    erro = "Lista de setores não carregada.";
                    listaSetores = new ArrayList<>();
                }
            } catch (Exception e) {
                e.printStackTrace();
                erro = "Erro ao buscar lista de setores.";
                listaSetores = new ArrayList<>();
            }

            request.setAttribute("listaSetores", listaSetores);

            if (erro != null) {
                request.setAttribute("erro", erro);
            }

            request.getRequestDispatcher("/WEB-INF/pages/setores.jsp").forward(request, response);
        }
    }
}