package com.example.Servlet.ServletEmpresa;

import com.example.Model.Plano;
import com.example.dao.EmpresaDAO;
import com.example.Model.Empresa;
import com.example.dao.PlanoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.ArrayList; // Para lista vazia

@WebServlet("/empresa-create")
public class ServletCreateEmpresa extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        // Pegar parâmetros...
        String nome = request.getParameter("nome");
        String cep = request.getParameter("cep");
        String cnpj = request.getParameter("cnpj");
        String email = request.getParameter("email");
        String telefone = request.getParameter("telefone");
        String porte = request.getParameter("porte");
        String horaEntradaStr = request.getParameter("horaAbertura");
        String horaFechamentoStr = request.getParameter("horaFechamento");
        String regrasNegocios = request.getParameter("regrasNegocios");
        int idPlano = Integer.parseInt(request.getParameter("idPlano"));

        PlanoDAO planoDAO = new PlanoDAO();
        Plano plano = planoDAO.read(idPlano);




        EmpresaDAO dao = new EmpresaDAO();
        boolean success = false;

        try {
            // LocalTime.parse pode lançar DateTimeParseException
            LocalTime horaEntrada = LocalTime.parse(horaEntradaStr);
            LocalTime horaFechamento = LocalTime.parse(horaFechamentoStr);
            // Integer/Float.parseInt lançaria NumberFormatException (que é um IllegalArgumentException)

            // Construtor/Setters podem lançar IllegalArgumentException, NullPointerException, IllegalStateException
            Empresa novaEmpresa = new Empresa(
                    nome, cep, cnpj, email, telefone, porte,
                    horaEntrada, horaFechamento, regrasNegocios, plano
            );

            success = dao.create(novaEmpresa);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/empresas-crud");
                return;
            } else {
                request.setAttribute("erro", "Erro ao cadastrar empresa no banco.");
            }

        } catch (IllegalArgumentException | NullPointerException | IllegalStateException | DateTimeParseException e) {
            // --- CORRIGIDO: NumberFormatException REMOVIDO daqui ---
            request.setAttribute("erro", "Erro: " + e.getMessage());
            request.setAttribute("nome_previo", nome);
            request.setAttribute("cep_previo", cep);
            request.setAttribute("cnpj_previo", cnpj);
            request.setAttribute("email_previo", email);
            request.setAttribute("telefone_previo", telefone);
            request.setAttribute("porte_previo", porte);
            request.setAttribute("horaEntrada_previo", horaEntradaStr);
            request.setAttribute("horaFechamento_previo", horaFechamentoStr);
            request.setAttribute("regrasNegocios_previo", regrasNegocios);

        } catch (Exception e) { // Outros erros
            e.printStackTrace();
            request.setAttribute("erro", "Erro inesperado: " + e.getMessage());
        }

        if (!success) {
            System.err.println("Falha na criação. Fazendo forward.");
            List<Empresa> listaEmpresas = null;
            try {
                listaEmpresas = dao.read();
            } catch (Exception readEx) {
                System.err.println("Erro ao recarregar lista após falha no create: " + readEx.getMessage());
                listaEmpresas = new ArrayList<>();
            }
            request.setAttribute("listaEmpresas", listaEmpresas);
            request.setAttribute("abrirModal", "create");
            request.getRequestDispatcher("/WEB-INF/pages/empresas.jsp").forward(request, response);
        }
    }
}