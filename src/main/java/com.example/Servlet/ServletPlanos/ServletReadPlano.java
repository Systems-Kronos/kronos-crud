package com.example.Servlet.ServletPlanos; // Verifique o pacote

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import com.example.Model.Plano;      // Verifique o import
import com.example.dao.PlanoDAO;     // Verifique o import

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet focado SOMENTE em LER (Read) a lista de Planos.
 */
@WebServlet("/planos-crud") // URL principal
public class ServletReadPlano extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PlanoDAO dao = new PlanoDAO(); // DAO Correto
        List<Plano> listaPlanos = null; // Tipo Correto
        String erro = null;

        try {
            // Busca a lista completa
            listaPlanos = dao.read(); // Usa o read() do PlanoDAO

            if (listaPlanos == null) {
                erro = "Lista não carregada.";
                listaPlanos = new ArrayList<>();
            }

        } catch (Exception e) {
            e.printStackTrace();
            erro = "Erro ao buscar lista.";
            listaPlanos = new ArrayList<>();
        }

        // Passa a lista (ou vazia) para o JSP
        request.setAttribute("listaPlanos", listaPlanos); // Nome usado no JSP

        // Passa erro, se houver
        if (erro != null) {
            request.setAttribute("erro", erro);
        }

        // Encaminha para o JSP correto
        request.getRequestDispatcher("/WEB-INF/pages/planos.jsp").forward(request, response);
    }
}