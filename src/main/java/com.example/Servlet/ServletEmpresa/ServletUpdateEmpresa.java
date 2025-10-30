package com.example.Servlet.ServletEmpresa;

import com.example.dao.EmpresaDAO;
import com.example.dao.PlanoDAO; // <-- IMPORT ADICIONADO
import com.example.Model.Empresa;
import com.example.Model.Plano;   // <-- IMPORT ADICIONADO
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException; // <-- IMPORT ADICIONADO
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.ArrayList; // <-- IMPORT ADICIONADO

/**
 * Servlet focado em ATUALIZAR (Update) uma Empresa existente.
 * Segue o padrão robusto de tratamento de exceções.
 */
@WebServlet("/empresas-update")
public class ServletUpdateEmpresa extends HttpServlet {

    /*
     * doGet: Prepara a página para a edição.
     * Carrega a lista completa de empresas (tabela), a empresa específica (modal)
     * e a lista completa de planos (dropdowns).
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        EmpresaDAO dao = new EmpresaDAO();
        PlanoDAO planoDAO = new PlanoDAO(); // DAO para Planos
        List<Empresa> listaEmpresas = new ArrayList<>();
        List<Plano> listaPlanos = new ArrayList<>(); // Lista para dropdowns
        String erro = null;

        // Bloco try-catch unificado para todas as leituras de banco
        try {
            // 1. Busca a lista completa de empresas para a tabela de fundo
            listaEmpresas = dao.read(); // Pode lançar SQLException

            // 2. CORREÇÃO: Busca a lista completa de planos para os modais
            listaPlanos = planoDAO.read(); // Pode lançar SQLException

            // 3. Pega o ID da URL para carregar o modal de update
            String idParam = request.getParameter("id");
            Empresa empresaModal = null;

            if (idParam != null && !idParam.isEmpty()) {
                int id = Integer.parseInt(idParam); // Pode lançar NumberFormatException
                empresaModal = dao.read(id); // Pode lançar SQLException

                if (empresaModal != null) {
                    request.setAttribute("empresaModal", empresaModal);
                    request.setAttribute("abrirModal", "update");
                } else {
                    erro = "Empresa com ID " + id + " não encontrada.";
                }
            }
            // Se idParam for nulo, apenas carrega a página sem modal

        } catch (SQLException e) {
            e.printStackTrace();
            erro = "Erro de banco de dados ao carregar dados: " + e.getMessage();
        } catch (NumberFormatException e) {
            erro = "ID inválido fornecido (doGet): " + request.getParameter("id");
            System.err.println("ID inválido ('id') update empresa (doGet): " + request.getParameter("id"));
        } catch (Exception e) { // Outros erros
            e.printStackTrace();
            erro = "Erro inesperado ao buscar dados: " + e.getMessage();
        }

        // Encaminhamento para o JSP
        if (erro != null) {
            request.setAttribute("erro", erro);
        }
        request.setAttribute("listaEmpresas", listaEmpresas);
        request.setAttribute("listaPlanos", listaPlanos);

        request.getRequestDispatcher("/WEB-INF/pages/empresas.jsp").forward(request, response);
    }

    /*
     * doPost: Recebe dados do modal e salva as alterações.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        EmpresaDAO dao = new EmpresaDAO();
        int id = 0;
        boolean success = false;
        String erro = null;

        // 1. Coleta de parâmetros (para repopular em caso de erro)
        String idParam = request.getParameter("id");
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

        try {
            // 2. Conversão e Validação Preliminar
            id = Integer.parseInt(idParam);
            LocalTime horaEntrada = LocalTime.parse(horaEntradaStr);
            LocalTime horaFechamento = LocalTime.parse(horaFechamentoStr);
            int idPlano = Integer.parseInt(idPlanoStr);

            // 3. Busca objeto original
            Empresa empresa = dao.read(id); // Pode lançar SQLException
            if (empresa == null) {
                throw new Exception("Empresa ID " + id + " não encontrada para atualizar.");
            }

            // 4. Atualiza (dispara validações do Model)
            empresa.setNome(nome);
            empresa.setCep(cep);
            empresa.setCnpj(cnpj);
            empresa.setEmail(email);
            empresa.setTelefone(telefone);
            empresa.setPorte(porte);
            empresa.setHorarioAbertura(horaEntrada);
            empresa.setHorarioFechamento(horaFechamento);
            empresa.setRegraDeNegocios(regrasNegocios);
            empresa.setIdPlano(idPlano);

            // 5. Salva
            int resultado = dao.update(empresa);

            if (resultado > 0) {
                success = true;
            } else {
                erro = "Não foi possível atualizar (ID: " + id + "). O registro pode não existir mais.";
            }

            // Captura erros de VALIDAÇÃO do Model ou de CONVERSÃO
        } catch (IllegalArgumentException | NullPointerException | IllegalStateException | DateTimeParseException e) {
            erro = "Erro de validação: " + e.getMessage();

        } catch (SQLException e) {
            e.printStackTrace();
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                erro = "Erro: Já existe uma empresa com este CNPJ ou E-mail.";
            } else if (e.getMessage().contains("violates foreign key constraint")) {
                erro = "Erro: O Plano selecionado é inválido.";
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
            System.out.println("Empresa ID " + id + " atualizada com sucesso.");
            response.sendRedirect(request.getContextPath() + "/empresas-crud");
            return; // Encerra
        }

        // 7. CAMINHO DE FALHA (Forward)
        System.err.println("Falha update empresa ID " + id + ". Forwarding. Erro: " + erro);

        // Define atributos de erro e repopulação
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
        request.setAttribute("plano_previo", idPlanoStr); // Envia a String do ID

        // Recarrega lista de EMPRESAS para a tabela
        List<Empresa> listaEmpresas = new ArrayList<>();
        try {
            listaEmpresas = dao.read(); // Trata SQLException
        } catch (SQLException readEx) {
            readEx.printStackTrace();
            request.setAttribute("erro", erro + " | ERRO ADICIONAL: Falha ao recarregar lista de empresas.");
        }
        request.setAttribute("listaEmpresas", listaEmpresas);

        // Recarrega lista de PLANOS para os dropdowns
        try {
            PlanoDAO planoDAO = new PlanoDAO();
            request.setAttribute("listaPlanos", planoDAO.read());
        } catch (Exception planoEx) {
            planoEx.printStackTrace();
            request.setAttribute("erro", erro + " | ERRO ADICIONAL: Falha ao recarregar lista de planos.");
        }

        // Tenta recarregar modal com dados (se ID for válido)
        if (id > 0 && request.getAttribute("empresaModal") == null) {
            try {
                request.setAttribute("empresaModal", dao.read(id));
            } catch (Exception readEx) { /* Ignora erro menor */ }
        }

        request.setAttribute("abrirModal", "update"); // Avisa para reabrir
        request.getRequestDispatcher("/WEB-INF/pages/empresas.jsp").forward(request, response);
    }
}