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
import java.util.List;

/**
 * Servlet para ATUALIZAR (Update) uma Empresa existente.
 */
@WebServlet("/empresas-update")
public class ServletUpdateEmpresa extends HttpServlet {

    /**
     * doGet: Busca dados para preencher o modal de edição.
     * Espera ser chamado via GET com o parâmetro 'id'.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        EmpresaDAO dao = new EmpresaDAO();

        // Busca a lista completa para a tabela de fundo
        List<Empresa> listaEmpresas = dao.read();
        request.setAttribute("listaEmpresas", listaEmpresas);

        String idParam = request.getParameter("id"); // Usando "id" como padrão
        Empresa empresaModal = null;

        try {
            int id = Integer.parseInt(idParam);
            empresaModal = dao.read(id); // Usa o read(id) do DAO

            if (empresaModal != null) {
                request.setAttribute("empresaModal", empresaModal);
                request.setAttribute("abrirModal", "update");
            } else {
                request.setAttribute("erro", "Empresa com ID " + id + " não encontrada.");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("erro", "ID inválido fornecido.");
            System.err.println("ID inválido ('id') para update de empresa (doGet): " + idParam);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro ao buscar dados da empresa.");
        }

        // Encaminha para o JSP
        request.getRequestDispatcher("/WEB-INF/pages/empresas.jsp").forward(request, response);
    }

    /**
     * doPost: Recebe dados do modal e salva as alterações.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        EmpresaDAO dao = new EmpresaDAO();
        int id = 0;
        boolean success = false;

        // Pegar dados do request (para repopular em caso de erro)
        String idParam = request.getParameter("id"); // Pega do hidden input
        String nome = request.getParameter("nome");
        String cep = request.getParameter("cep");
        String cnpj = request.getParameter("cnpj");
        String email = request.getParameter("email");
        String telefone = request.getParameter("telefone");
        String porte = request.getParameter("porte"); // Vem do <select>
        String horaEntradaStr = request.getParameter("horaAbertura");
        String horaFechamentoStr = request.getParameter("horaFechamento");
        String regrasNegocios = request.getParameter("regrasNegocios");

        try {
            // Converte e valida ID e tempos
            id = Integer.parseInt(idParam);
            LocalTime horaEntrada = LocalTime.parse(horaEntradaStr);
            LocalTime horaFechamento = LocalTime.parse(horaFechamentoStr);

            // Busca objeto original
            Empresa empresa = dao.read(id);
            if (empresa == null) {
                throw new Exception("Empresa ID " + id + " não encontrada.");
            }

            // Atualiza (dispara validações do Model)
            empresa.setNome(nome);
            empresa.setCep(cep);
            empresa.setCnpj(cnpj);
            empresa.setEmail(email);
            empresa.setTelefone(telefone);
            empresa.setPorte(porte); // Atualiza o porte
            empresa.setHorarioAbertura(horaEntrada);
            empresa.setHorarioFechamento(horaFechamento);
            empresa.setRegraDeNegocios(regrasNegocios);

            // Salva
            int resultado = dao.update(empresa);

            if (resultado > 0) {
                success = true;
            } else {
                request.setAttribute("erro", "Não foi possível atualizar (ID: " + id + ").");
            }

        } catch (IllegalArgumentException | NullPointerException | IllegalStateException | DateTimeParseException  e) {
            // Erro de validação ou formato
            request.setAttribute("erro", "Erro: " + e.getMessage());
            // Guarda dados para repopular
            request.setAttribute("nome_previo", nome);
            request.setAttribute("cep_previo", cep);
            request.setAttribute("cnpj_previo", cnpj);
            request.setAttribute("email_previo", email);
            request.setAttribute("telefone_previo", telefone);
            request.setAttribute("porte_previo", porte); // Guarda o porte
            request.setAttribute("horaEntrada_previo", horaEntradaStr);
            request.setAttribute("horaFechamento_previo", horaFechamentoStr);
            request.setAttribute("regrasNegocios_previo", regrasNegocios);

        } catch (Exception e) { // Outros erros
            e.printStackTrace();
            request.setAttribute("erro", "Erro inesperado: " + e.getMessage());
        }

        // --- Resposta ---
        if (success) {
            response.sendRedirect(request.getContextPath() + "/empresas-crud"); // Redirect para lista
        } else {
            // FALHA: Forward com erro
            System.err.println("Falha update empresa ID " + id + ". Forwarding.");
            List<Empresa> listaEmpresas = dao.read(); // Recarrega lista
            request.setAttribute("listaEmpresas", listaEmpresas);
            // Tenta recarregar modal com dados atuais
            if (id > 0 && request.getAttribute("empresaModal") == null) {
                try { request.setAttribute("empresaModal", dao.read(id)); } catch (Exception readEx) { /* Ignora */ }
            }
            request.setAttribute("abrirModal", "update"); // Avisa para reabrir modal
            request.getRequestDispatcher("/WEB-INF/pages/empresas.jsp").forward(request, response); // Forward JSP
        }
    }
}