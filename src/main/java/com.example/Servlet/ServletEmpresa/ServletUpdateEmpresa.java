package com.example.Servlet.ServletEmpresa;

import com.example.dao.EmpresaDAO;
import com.example.Model.Empresa;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;


@WebServlet("/empresas-update")
public class ServletUpdateEmpresa extends HttpServlet {


    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        EmpresaDAO dao = new EmpresaDAO();


        List<Empresa> listaEmpresas = dao.read();
        request.setAttribute("listaEmpresas", listaEmpresas);


        String pk = request.getParameter("pk");
        Empresa empresaModal = null;

        try {
            if (pk != null && !pk.isEmpty()) {
                int id = Integer.parseInt(pk);
                empresaModal = dao.read(id);

                if (empresaModal != null) {
                    request.setAttribute("empresaModal", empresaModal);
                    request.setAttribute("abrirModal", "update");
                } else {
                    request.setAttribute("erro", "Empresa com ID " + id + " não encontrada.");
                }
            } else {
                request.setAttribute("erro", "ID da empresa não fornecido para edição.");
            }
        } catch (NumberFormatException e) {
            System.err.println("ID inválido para update: " + pk);
            request.setAttribute("erro", "ID inválido fornecido para edição.");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro ao buscar dados da empresa para editar.");
        }


        request.getRequestDispatcher("/WEB-INF/empresas.jsp").forward(request, response);
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        EmpresaDAO dao = new EmpresaDAO();
        int id = 0;

        try {
            id = Integer.parseInt(request.getParameter("pk"));
            String nome = request.getParameter("nome");
            String cep = request.getParameter("cep");
            String cnpj = request.getParameter("cnpj");
            String email = request.getParameter("email");
            String telefone = request.getParameter("telefone");
            String porte = request.getParameter("porte");
            String horaEntradaStr = request.getParameter("horaEntrada");
            String horaFechamentoStr = request.getParameter("horaFechamento");
            String regrasNegocios = request.getParameter("regrasNegocios");


            LocalTime horaEntrada = LocalTime.parse(horaEntradaStr);
            LocalTime horaFechamento = LocalTime.parse(horaFechamentoStr);


            Empresa empresa = dao.read(id);
            if (empresa == null) {
                throw new Exception("Empresa com ID " + id + " não encontrada para atualizar.");
            }
            empresa.setNome(nome);
            empresa.setCep(cep);
            empresa.setCnpj(cnpj);
            empresa.setEmail(email);
            empresa.setTelefone(telefone);
            empresa.setPorte(porte);
            empresa.setHorarioAbertura(horaEntrada);
            empresa.setHorarioFechamento(horaFechamento);
            empresa.setRegraDeNegocios(regrasNegocios);

            int resultado = dao.update(empresa);


            if (resultado > 0) {
                System.out.println("Empresa ID " + id + " atualizada com sucesso.");
                response.sendRedirect(request.getContextPath() + "/empresas-crud");
                return; // Encerra o método
            } else {
                request.setAttribute("erro", "Não foi possível atualizar a empresa (ID: " + id + ").");
            }

        } catch (IllegalArgumentException | NullPointerException | IllegalStateException | DateTimeParseException e) {
            // Captura erros de VALIDAÇÃO (do Model ou do LocalTime.parse)
            request.setAttribute("erro", "Erro de validação ao atualizar: " + e.getMessage());
            System.err.println("Erro de validação no update: " + e.getMessage());

        }catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro inesperado ao processar a atualização: " + e.getMessage());
        }



        System.err.println("Falha ao atualizar empresa ID " + id + ". Fazendo forward para o JSP com erro.");

        List<Empresa> listaEmpresas = dao.read();
        request.setAttribute("listaEmpresas", listaEmpresas);


        if (id > 0) {
            try {

                Empresa empresaModal = dao.read(id);
                if(empresaModal != null) {

                    request.setAttribute("empresaModal", empresaModal);
                } else {

                }
            } catch (Exception readEx) {
                System.err.println("Erro ao tentar reler empresa " + id + " após falha no update: " + readEx.getMessage());
            }
        }

        request.setAttribute("abrirModal", "update");


        request.getRequestDispatcher("/WEB-INF/empresas.jsp").forward(request, response);
    }
}