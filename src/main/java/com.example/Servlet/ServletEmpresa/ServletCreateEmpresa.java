package com.example.Servlet.ServletEmpresa;

import com.example.dao.EmpresaDAO;
import com.example.Model.Empresa;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List; // Importe o List

@WebServlet("/empresa-create")
public class ServletCreateEmpresa extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String nome = request.getParameter("nome");
        String cep = request.getParameter("cep");
        String cnpj = request.getParameter("cnpj");
        String email = request.getParameter("email");
        String telefone = request.getParameter("telefone");
        String porte = request.getParameter("porte");
        String horaEntradaStr = request.getParameter("horaEntrada");
        String horaFechamentoStr = request.getParameter("horaFechamento");
        String regrasNegocios = request.getParameter("regrasNegocios");

        EmpresaDAO dao = new EmpresaDAO();

        try {
            LocalTime horaEntrada = LocalTime.parse(horaEntradaStr);
            LocalTime horaFechamento = LocalTime.parse(horaFechamentoStr);

            // 5. Criar o objeto Empresa (suas validações do Model são disparadas aqui)
            Empresa novaEmpresa = new Empresa(
                    nome, cep, cnpj, email, telefone, porte,
                    horaEntrada, horaFechamento, regrasNegocios
            );

            // 6. Inserir no banco de dados
            boolean sucesso = dao.create(novaEmpresa);

            if (sucesso) {
                System.out.println("Empresa criada com sucesso!");
                // SUCESSO: Redireciona para a lista (Padrão PRG)
                response.sendRedirect(request.getContextPath() + "/empresas-crud");
                return; // IMPORTANTE: Encerra o método aqui
            } else {
                // Falha no DAO (ex: erro de SQL)
                request.setAttribute("erro", "Erro ao cadastrar empresa no banco de dados.");
            }

        } catch (IllegalArgumentException | NullPointerException | IllegalStateException | DateTimeParseException e) {
            // Captura exceções de validação (do Model ou do LocalTime.parse)
            request.setAttribute("erro", "Erro de validação: " + e.getMessage());

            // Mantém os dados preenchidos no formulário
            request.setAttribute("nome_previo", nome);
            request.setAttribute("cep_previo", cep);
            request.setAttribute("cnpj_previo", cnpj);
            request.setAttribute("email_previo", email);
            request.setAttribute("telefone_previo", telefone);
            request.setAttribute("porte_previo", porte);
            request.setAttribute("horaEntrada_previo", horaEntradaStr);
            request.setAttribute("horaFechamento_previo", horaFechamentoStr);
            request.setAttribute("regrasNegocios_previo", regrasNegocios);
        }

        // --- PLANO B (Se deu erro) ---
        // Se o código chegou aqui, é porque uma falha ocorreu (no 'else' ou no 'catch').

        System.err.println("Falha na criação. Fazendo forward para o JSP com erro.");

        // 1. Recarrega a lista (para a tabela de fundo do JSP não quebrar)
        List<Empresa> listaEmpresas = dao.read();
        request.setAttribute("listaEmpresas", listaEmpresas);

        // 2. Avisa o JSP para reabrir o modal de CREATE
        request.setAttribute("abrirModal", "create");

        // 3. Encaminha (forward) o request (com os erros e dados) de volta para o JSP
        request.getRequestDispatcher("/WEB-INF/empresas.jsp").forward(request, response);
    }
}