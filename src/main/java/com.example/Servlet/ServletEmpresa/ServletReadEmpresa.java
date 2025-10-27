package com.example.Servlet.ServletEmpresa;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList; // Para lista vazia

import com.example.Model.Empresa;
import com.example.dao.EmpresaDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet focado SOMENTE em LER (Read) a lista de Empresas.
 */
@WebServlet("/empresas-crud") // URL principal
public class ServletReadEmpresa extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        EmpresaDAO dao = new EmpresaDAO();
        List<Empresa> listaEmpresas = null;
        String erro = null;

        try {
            // --- LÓGICA SIMPLIFICADA ---
            // Pega todas as empresas do banco
            listaEmpresas = dao.read(); // Usa o read() sem parâmetros

            if (listaEmpresas == null) {
                // Trata caso de DAO retornar null
                System.err.println("DAO retornou lista nula de empresas.");
                erro = "Não foi possível carregar a lista de empresas.";
                listaEmpresas = new ArrayList<>(); // Garante lista vazia no JSP
            }

        } catch (Exception e) {
            e.printStackTrace();
            erro = "Erro ao buscar a lista de empresas.";
            listaEmpresas = new ArrayList<>(); // Garante lista vazia
        }

        // Passa a lista para o JSP
        request.setAttribute("listaEmpresas", listaEmpresas);

        // Passa erro, se houver
        if (erro != null) {
            request.setAttribute("erro", erro);
        }

        // Encaminha para o JSP dentro do WEB-INF/pages
        request.getRequestDispatcher("/WEB-INF/pages/empresas.jsp").forward(request, response);
    }

    // Não precisa de doPost neste servlet
}