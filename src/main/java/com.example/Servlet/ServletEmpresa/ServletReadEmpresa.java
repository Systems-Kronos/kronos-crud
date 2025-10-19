package com.example.Servlet.ServletEmpresa;

import com.example.dao.EmpresaDAO;
import com.example.Model.Empresa;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/empresas-crud")
public class ServletReadEmpresa extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Instancia DAO
        EmpresaDAO dao = new EmpresaDAO();

        // Pega todas as empresas do banco
        List<Empresa> listaEmpresas = dao.read();

        // Passa para o JSP
        request.setAttribute("listaEmpresas", listaEmpresas);

        // Encaminha para o JSP dentro do WEB-INF
        request.getRequestDispatcher("/WEB-INF/empresas.jsp").forward(request, response);
    }
}
