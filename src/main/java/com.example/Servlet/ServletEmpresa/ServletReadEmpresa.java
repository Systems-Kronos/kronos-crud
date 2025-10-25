
package com.example.Servlet.ServletEmpresa;

import java.io.IOException;
import java.util.List;

import com.example.Model.Empresa;
import com.example.dao.EmpresaDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/empresas-crud")
public class ServletReadEmpresa extends HttpServlet {
    
    // Instancia DAO
    private EmpresaDAO dao = new EmpresaDAO();

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
                            + "\"regrasNegocios\":\"" + empresa.getRegraDeNegocios() + "\""
                            + "}";

                    response.getWriter().write(json);
                }
            } catch (NumberFormatException e) {
                response.getWriter().write("{\"erro\":\"PK inválida\"}");
            } catch (Exception e) {
                response.getWriter().write("{\"erro\":\"" + e.getMessage() + "\"}");
            }

        } else {

            // Pega todas as empresas do banco
            List<Empresa> listaEmpresas = dao.read();

            // Passa para o JSP
            request.setAttribute("listaEmpresas", listaEmpresas);

            // Encaminha para o JSP dentro do WEB-INF
            request.getRequestDispatcher("/WEB-INF/pages/empresas.jsp").forward(request, response);
        }
    }
}
