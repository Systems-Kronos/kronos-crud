package com.example.Servlet.ServletEmpresa;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List; // Para lista vazia

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

    // Instanciando DAO
    private EmpresaDAO dao = new EmpresaDAO();


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pk = request.getParameter("pk");

        if (pk != null && !pk.isEmpty()) {

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            try {
                Empresa empresa = dao.read(Integer.parseInt(pk));

                if (empresa != null) {

                    String json = "{"
                            + "\"id\":\"" + pk + "\","
                            + "\"nome\":\"" + empresa.getNome() + "\","
                            + "\"email\":\"" + empresa.getEmail() + "\","
                            + "\"cep\":\"" + empresa.getCep() + "\","
                            + "\"cnpj\":\"" + empresa.getCnpj() + "\","
                            + "\"telefone\":\"" + empresa.getTelefone() + "\","
                            + "\"porte\":\"" + empresa.getPorte() + "\","
                            + "\"horaAbertura\":\"" + empresa.getHorarioAbertura() + "\","
                            + "\"horaFechamento\":\"" + empresa.getHorarioFechamento() + "\","
                            + "\"regrasNegocios\":\"" + empresa.getRegraDeNegocios().trim() + "\""
                            + "}";

                    response.getWriter().write(json);
                }
            } catch (NumberFormatException e) {
                response.getWriter().write("{\"erro\":\"PK inválida\"}");
            } catch (Exception e) {
                response.getWriter().write("{\"erro\":\"" + e.getMessage() + "\"}");
            }

        } else {

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
    } // Não precisa de doPost neste servlet
}