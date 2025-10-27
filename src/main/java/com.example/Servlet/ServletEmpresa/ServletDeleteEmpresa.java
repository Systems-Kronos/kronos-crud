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

@WebServlet("/empresas-delete")
public class ServletDeleteEmpresa extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        EmpresaDAO dao = new EmpresaDAO();
        List<Empresa> listaEmpresas = dao.read();
        request.setAttribute("listaEmpresas", listaEmpresas);

        // --- MUDANÇA: Usando "id" ---
        String idParam = request.getParameter("id");
        Empresa empresaModal = null;

        try {
            int id = Integer.parseInt(idParam);
            empresaModal = dao.read(id);
            if (empresaModal != null) {
                request.setAttribute("empresaModal", empresaModal);
                request.setAttribute("abrirModal", "delete");
            } else {
                request.setAttribute("erro", "Empresa com ID " + id + " não encontrada.");
            }
        } catch (NumberFormatException e) {
            System.err.println("ID inválido para delete: " + idParam); // Mensagem atualizada
            request.setAttribute("erro", "ID inválido fornecido para deleção.");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro ao buscar dados da empresa para deletar.");
        }
        // --- MUDANÇA: Caminho JSP ---
        request.getRequestDispatcher("/WEB-INF/pages/empresas.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        EmpresaDAO dao = new EmpresaDAO();
        int id = 0;
        boolean success = false;

        try {
            // --- MUDANÇA: Usando "id" ---
            String idParam = request.getParameter("id");
            id = Integer.parseInt(idParam);

            int resultado = dao.delete(id);

            if (resultado > 0) {
                success = true; // Marca para redirect
            } else {
                request.setAttribute("erro", "Não foi possível deletar a empresa (ID: " + id + "). Verifique dependências.");
            }

        } catch (NumberFormatException e) {
            request.setAttribute("erro", "ID inválido fornecido para deleção.");
            System.err.println("ID inválido para delete: " + request.getParameter("id")); // Mensagem atualizada
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro inesperado ao processar a deleção: " + e.getMessage());
        }

        if (success) {
            response.sendRedirect(request.getContextPath() + "/empresas-crud"); // Redirect para lista
        } else {
            // FALHA: Forward com erro
            System.err.println("Falha ao deletar empresa ID " + id + ". Fazendo forward.");
            List<Empresa> listaEmpresas = dao.read(); // Recarrega lista
            request.setAttribute("listaEmpresas", listaEmpresas);
            // Tenta recarregar modal
            if (id > 0) {
                try {
                    Empresa empresaModal = dao.read(id);
                    if(empresaModal != null) request.setAttribute("empresaModal", empresaModal);
                } catch (Exception readEx) { /* Ignora */ }
            }
            request.setAttribute("abrirModal", "delete"); // Avisa para reabrir
            // --- MUDANÇA: Caminho JSP ---
            request.getRequestDispatcher("/WEB-INF/pages/empresas.jsp").forward(request, response);
        }
    }
}