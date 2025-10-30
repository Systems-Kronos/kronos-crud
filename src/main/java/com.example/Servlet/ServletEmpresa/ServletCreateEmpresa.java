package com.example.Servlet.ServletEmpresa;

import com.example.dao.EmpresaDAO;
import com.example.dao.PlanoDAO;
import com.example.Model.Empresa;
import com.example.Model.Plano;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.ArrayList;

/**
 * Servlet focado SOMENTE em CRIAR (Create) uma nova Empresa.
 * Segue o padrão robusto, tratando exceções do Model e DAO,
 * e repopulando o formulário em caso de erro.
 */
@WebServlet("/empresa-create")
public class ServletCreateEmpresa extends HttpServlet {

    /*
     * Processa a criação de uma nova Empresa via POST request.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // 1. Coleta de parâmetros (declarados fora para usar no catch/finally)
        String nome = request.getParameter("nome");
        String cep = request.getParameter("cep");
        String cnpj = request.getParameter("cnpj");
        String email = request.getParameter("email");
        String telefone = request.getParameter("telefone");
        String porte = request.getParameter("porte");
        String horaEntradaStr = request.getParameter("horaAbertura");
        String horaFechamentoStr = request.getParameter("horaFechamento");
        String regrasNegocios = request.getParameter("regrasNegocios");
        String idPlanoStr = request.getParameter("plano");

        EmpresaDAO dao = new EmpresaDAO();
        boolean success = false;
        String erro = null; // Armazena a mensagem de erro

        try {
            // 2. Conversão e Validação Preliminar
            //    (Lançam exceções que são pegas abaixo)
            LocalTime horaEntrada = LocalTime.parse(horaEntradaStr);
            LocalTime horaFechamento = LocalTime.parse(horaFechamentoStr);
            int idPlano = Integer.parseInt(idPlanoStr);

            // 3. Validação (Model)
            // O construtor/Setters podem lançar IllegalArgument/NullPointer/IllegalState
            Empresa novaEmpresa = new Empresa(
                    nome, cep, cnpj, email, telefone, porte,
                    horaEntrada, horaFechamento, regrasNegocios, idPlano
            );

            // 4. Persistência (DAO)
            // O DAO (corrigido) lança SQLException
            success = dao.create(novaEmpresa);

            if (success) {
                // 5. SUCESSO (PRG Pattern)
                System.out.println("Empresa criada com sucesso!");
                response.sendRedirect(request.getContextPath() + "/empresas-crud");
                return; // IMPORTANTE: Encerra aqui após redirect
            } else {
                // Falha no DAO (ex: create retornou false sem lançar exceção)
                erro = "Erro ao cadastrar empresa no banco (DAO retornou false).";
            }

            // Captura erros de VALIDAÇÃO do Model ou de CONVERSÃO de tipos
        } catch (IllegalArgumentException | NullPointerException | IllegalStateException | DateTimeParseException e) {
            // NumberFormatException é subclasse de IllegalArgumentException
            erro = "Erro de validação: " + e.getMessage();

        } catch (SQLException e) {
            e.printStackTrace();
            // Mensagens amigáveis para violações comuns
            if (e.getMessage().contains("Duplicate entry") || e.getMessage().contains("UNIQUE constraint failed")) {
                erro = "Erro: Já existe uma empresa com este CNPJ ou E-mail.";
            } else if (e.getMessage().contains("violates foreign key constraint")) {
                erro = "Erro: O Plano selecionado é inválido ou não existe.";
            } else {
                erro = "Erro de banco de dados ao criar empresa: " + e.getMessage();
            }

        } catch (Exception e) { // Captura outros erros inesperados
            e.printStackTrace();
            erro = "Erro inesperado ao criar empresa: " + e.getMessage();
        }

        // --- 6. CAMINHO DE FALHA (Forward) ---
        // O código só chega aqui se 'success' for false ou se uma exceção foi pega.
        System.err.println("Falha na criação da empresa. Fazendo forward. Erro: " + erro);

        // Define os atributos de erro e de repopulação do formulário
        request.setAttribute("erro", erro);
        request.setAttribute("nome_previo", nome);
        request.setAttribute("cep_previo", cep);
        request.setAttribute("cnpj_previo", cnpj);
        request.setAttribute("email_previo", email);
        request.setAttribute("telefone_previo", telefone);
        request.setAttribute("porte_previo", porte);
        request.setAttribute("horaAbertura_previo", horaEntradaStr);
        request.setAttribute("horaFechamento_previo", horaFechamentoStr);
        request.setAttribute("regrasNegocios_previo", regrasNegocios);
        request.setAttribute("plano_previo", idPlanoStr);

        // Recarrega a lista de EMPRESAS para a tabela de fundo
        List<Empresa> listaEmpresas = new ArrayList<>();
        try {
            listaEmpresas = dao.read();
        } catch (SQLException readEx) {
            readEx.printStackTrace();
            request.setAttribute("erro", erro + " | ERRO ADICIONAL: Falha ao recarregar lista de empresas.");
        }
        request.setAttribute("listaEmpresas", listaEmpresas);

        try {
            PlanoDAO planoDAO = new PlanoDAO(); // Assume que PlanoDAO e Plano.java existem
            List<Plano> listaPlanos = planoDAO.read();
            request.setAttribute("listaPlanos", listaPlanos);
        } catch (Exception planoEx) { // Captura genérica caso PlanoDAO não exista
            planoEx.printStackTrace();
            request.setAttribute("erro", erro + " | ERRO ADICIONAL: Falha ao recarregar a lista de planos.");
        }

        // Avisa o JSP para reabrir o modal de CREATE
        request.setAttribute("abrirModal", "create");

        // Encaminha (forward) com erro e dados prévios
        request.getRequestDispatcher("/WEB-INF/pages/empresas.jsp").forward(request, response);
    }
}