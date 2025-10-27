package com.example.Servlet.ServletSetores;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import com.example.Model.Setor;
import com.example.dao.SetorDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/setores-crud")
public class ServletReadSetores extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        SetorDAO dao = new SetorDAO();
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