package com.example.Servlet.ServletSetores;

import com.example.dao.SetorDAO;
import com.example.dao.EmpresaDAO; // <-- IMPORT ADICIONADO
import com.example.Model.Setor;
import com.example.Model.Empresa; // <-- IMPORT ADICIONADO
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException; // <-- IMPORT ADICIONADO
import java.util.List;
import java.util.ArrayList;

/**
 * Servlet focado SOMENTE em CRIAR (Create) um novo Setor.
 * Segue o padrão robusto, tratando exceções do Model e DAO,
 * e repopulando o formulário em caso de erro.
 */
@WebServlet("/setor-create")
public class ServletCreateSetores extends HttpServlet {

    /*
     * Processa a criação de um novo Setor via POST request.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // 1. Coleta de parâmetros
        String nome = request.getParameter("nome");
        String turnos = request.getParameter("turnos");
        String qtdFuncionariosStr = request.getParameter("qtnFuncionarios");
        String descricao = request.getParameter("descricao");
        String idEmpresaStr = request.getParameter("idEmpresa");

        SetorDAO dao = new SetorDAO();
        boolean success = false;
        String erro = null;

        try {
            // 2. Conversão e Validação Preliminar
            //    (Lançam exceções que são pegas abaixo)
            int qtdFuncionarios = Integer.parseInt(qtdFuncionariosStr);
            int idEmpresa = Integer.parseInt(idEmpresaStr);

            // 3. Validação (Model)
            // O construtor/Setters podem lançar IllegalArgument/NullPointer
            Setor novoSetor = new Setor(
                    nome,
                    descricao,
                    turnos,
                    qtdFuncionarios,
                    idEmpresa
            );

            // 4. Persistência (DAO)
            // O DAO (corrigido) lança SQLException
            success = dao.create(novoSetor);

            if (success) {
                // 5. SUCESSO (PRG Pattern)
                System.out.println("Setor criado com sucesso!");
                response.sendRedirect(request.getContextPath() + "/setores-crud");
                return;
            } else {
                // Falha no DAO (ex: create retornou false)
                erro = "Erro ao cadastrar setor (DAO retornou false).";
            }

            // Captura erros de VALIDAÇÃO do Model ou de CONVERSÃO de tipos
        } catch (IllegalArgumentException | NullPointerException e) {
            // NumberFormatException é subclasse de IllegalArgumentException
            erro = "Erro de validação: " + e.getMessage();

        } catch (SQLException e) {
            e.printStackTrace();
            // Mensagens amigáveis para violações comuns
            if (e.getMessage().contains("Duplicate entry") || e.getMessage().contains("UNIQUE constraint failed")) {
                erro = "Erro: Já existe um setor com este nome.";
            } else if (e.getMessage().contains("violates foreign key constraint")) {
                erro = "Erro: A Empresa selecionada é inválida ou não existe.";
            } else {
                erro = "Erro de banco de dados ao criar setor: " + e.getMessage();
            }

        } catch (Exception e) { // Captura outros erros inesperados
            e.printStackTrace();
            erro = "Erro inesperado ao criar setor: " + e.getMessage();
        }

        // --- 6. CAMINHO DE FALHA (Forward) ---
        // O código só chega aqui se 'success' for false ou se uma exceção foi pega.
        System.err.println("Falha na criação do setor. Fazendo forward. Erro: " + erro);

        // Define os atributos de erro e de repopulação do formulário
        request.setAttribute("erro", erro);
        request.setAttribute("nome_previo", nome);
        request.setAttribute("turnos_previo", turnos);
        request.setAttribute("qtdFuncionarios_previo", qtdFuncionariosStr);
        request.setAttribute("descricao_previo", descricao);
        request.setAttribute("idEmpresa_previo", idEmpresaStr); // Envia a String do ID

        // Recarrega a lista de SETORES para a tabela de fundo
        List<Setor> listaSetores = new ArrayList<>();
        try {
            listaSetores = dao.read();
        } catch (SQLException readEx) {
            readEx.printStackTrace();
            request.setAttribute("erro", erro + " | ERRO ADICIONAL: Falha ao recarregar lista de setores.");
        }
        request.setAttribute("listaSetores", listaSetores);

        // Recarrega a lista de EMPRESAS (necessária para o <select> do modal)
        // O JSP 'setores.jsp' DEVE ter um loop para exibir os <option> desta lista.
        try {
            EmpresaDAO empresaDAO = new EmpresaDAO(); // Assume que EmpresaDAO existe
            request.setAttribute("listaEmpresas", empresaDAO.read());
        } catch (Exception empresaEx) { // Captura genérica caso EmpresaDAO falhe
            empresaEx.printStackTrace();
            request.setAttribute("erro", erro + " | ERRO ADICIONAL: Falha ao recarregar lista de empresas.");
        }

        // Avisa o JSP para reabrir o modal de CREATE
        request.setAttribute("abrirModal", "create");

        // Encaminha (forward) com erro e dados prévios
        request.getRequestDispatcher("/WEB-INF/pages/setores.jsp").forward(request, response);
    }
}