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
 * Servlet focado em ATUALIZAR (Update) um Setor existente.
 * Segue o padrão robusto de tratamento de exceções.
 */
@WebServlet("/setor-update")
public class ServletUpdateSetores extends HttpServlet {

    /*
     * doGet: Prepara a página para a edição.
     * Carrega a lista completa de setores (tabela), o setor específico (modal)
     * e a lista completa de empresas (dropdowns).
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        SetorDAO dao = new SetorDAO();
        EmpresaDAO empresaDAO = new EmpresaDAO(); // DAO para Empresas
        List<Setor> listaSetores = new ArrayList<>();
        List<Empresa> listaEmpresas = new ArrayList<>(); // Lista para dropdowns
        String erro = null;

        String idParam = request.getParameter("id");

        // Bloco try-catch unificado para todas as leituras de banco
        try {
            // 1. Busca a lista completa de setores para a tabela de fundo
            listaSetores = dao.read(); // Pode lançar SQLException

            // 2. Busca a lista completa de empresas para os modais
            listaEmpresas = empresaDAO.read(); // Pode lançar SQLException

            // 3. Processa o ID (que já foi lido) para carregar o modal
            Setor setorModal = null;
            if (idParam != null && !idParam.isEmpty()) {
                int id = Integer.parseInt(idParam); // Pode lançar NumberFormatException
                setorModal = dao.read(id); // Pode lançar SQLException

                if (setorModal != null) {
                    request.setAttribute("setorModal", setorModal);
                    request.setAttribute("abrirModal", "update");
                } else {
                    erro = "Setor ID " + id + " não encontrado.";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            erro = "Erro de banco de dados ao carregar dados: " + e.getMessage();
        } catch (NumberFormatException e) {
            erro = "ID inválido fornecido (doGet): " + idParam;
            System.err.println("ID inválido ('id') update setor (doGet): " + idParam);
        } catch (Exception e) { // Outros erros (ex: EmpresaDAO falha)
            e.printStackTrace();
            erro = "Erro inesperado ao buscar dados: " + e.getMessage();
        }

        // Encaminhamento para o JSP
        if (erro != null) {
            request.setAttribute("erro", erro);
        }
        request.setAttribute("listaSetores", listaSetores);
        request.setAttribute("listaEmpresas", listaEmpresas);

        request.getRequestDispatcher("/WEB-INF/pages/setores.jsp").forward(request, response);
    }

    /*
     * doPost: Recebe dados do modal e salva as alterações.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        SetorDAO dao = new SetorDAO();
        int id = 0;
        boolean success = false;
        String erro = null;

        // 1. Coleta de parâmetros (para repopular em caso de erro)
        String idParam = request.getParameter("id");
        String nome = request.getParameter("nome");
        String qtnFuncionariosStr = request.getParameter("qtnFuncionarios");
        String turnos = request.getParameter("turnos");
        String descricao = request.getParameter("descricao");
        String idEmpresaStr = request.getParameter("idEmpresa");

        try {
            // 2. Conversão e Validação Preliminar
            id = Integer.parseInt(idParam);
            int qtnFuncionarios = Integer.parseInt(qtnFuncionariosStr);
            int idEmpresa = Integer.parseInt(idEmpresaStr);

            // 3. Busca objeto original
            Setor setorParaAtualizar = dao.read(id); // Pode lançar SQLException
            if (setorParaAtualizar == null) {
                throw new Exception("Setor ID " + id + " não encontrado para atualizar.");
            }

            // 4. Atualiza (dispara validações do Model)
            setorParaAtualizar.setNome(nome);
            setorParaAtualizar.setQntFuncionarios(qtnFuncionarios);
            setorParaAtualizar.setTurnos(turnos);
            setorParaAtualizar.setDescricao(descricao);
            setorParaAtualizar.setIdEmpresa(idEmpresa);

            // 5. Salva
            int resultado = dao.update(setorParaAtualizar); // Pode lançar SQLException

            if (resultado > 0) {
                success = true;
            } else {
                erro = "Não foi possível atualizar (ID: " + id + "). O registro pode não existir mais.";
            }

            // Captura erros de VALIDAÇÃO do Model ou de CONVERSÃO
        } catch (IllegalArgumentException | NullPointerException e) {
            // NumberFormatException é subclasse de IllegalArgumentException
            erro = "Erro de validação ou formato inválido: " + e.getMessage();

        } catch (SQLException e) {
            e.printStackTrace();
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                erro = "Erro: Já existe um setor com este nome.";
            } else if (e.getMessage().contains("violates foreign key constraint")) {
                erro = "Erro: A Empresa selecionada é inválida.";
            } else {
                erro = "Erro de banco de dados ao atualizar: " + e.getMessage();
            }
        } catch (Exception e) { // Outros erros
            e.printStackTrace();
            erro = "Erro inesperado ao atualizar: " + e.getMessage();
        }

        // 6. Fluxo de Resposta
        if (success) {
            // SUCESSO: Redireciona (PRG)
            System.out.println("Setor ID " + id + " atualizado com sucesso.");
            response.sendRedirect(request.getContextPath() + "/setores-crud");
            return; // Encerra
        }

        // 7. CAMINHO DE FALHA (Forward)
        System.err.println("Falha update setor ID " + id + ". Forwarding. Erro: " + erro);

        // Define atributos de erro e repopulação
        request.setAttribute("erro", erro);
        request.setAttribute("nome_previo", nome);
        request.setAttribute("qtnFuncionarios_previo", qtnFuncionariosStr);
        request.setAttribute("turnos_previo", turnos);
        request.setAttribute("descricao_previo", descricao);
        request.setAttribute("idEmpresa_previo", idEmpresaStr);

        // Recarrega lista de SETORES para a tabela
        List<Setor> listaSetores = new ArrayList<>();
        try {
            listaSetores = dao.read();
        } catch (SQLException readEx) {
            readEx.printStackTrace();
            request.setAttribute("erro", erro + " | ERRO ADICIONAL: Falha ao recarregar lista de setores.");
        }
        request.setAttribute("listaSetores", listaSetores);

        // Recarrega lista de empresas para os dropdowns
        try {
            EmpresaDAO empresaDAO = new EmpresaDAO();
            request.setAttribute("listaEmpresas", empresaDAO.read());
        } catch (Exception empresaEx) {
            empresaEx.printStackTrace();
            request.setAttribute("erro", erro + " | ERRO ADICIONAL: Falha ao recarregar lista de empresas.");
        }

        // Tenta recarregar modal com dados (se ID for válido)
        if (id > 0 && request.getAttribute("setorModal") == null) {
            try {
                request.setAttribute("setorModal", dao.read(id));
            } catch (Exception readEx) { /* Ignora erro menor */ }
        }

        request.setAttribute("abrirModal", "update"); // Avisa para reabrir
        request.getRequestDispatcher("/WEB-INF/pages/setores.jsp").forward(request, response);
    }
}