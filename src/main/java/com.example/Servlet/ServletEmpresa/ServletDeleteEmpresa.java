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

    /**
     * doGet: Busca os dados para PREENCHER o modal de confirmação.
     * (Este método já está correto como você enviou)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        EmpresaDAO dao = new EmpresaDAO();


        List<Empresa> listaEmpresas = dao.read();
        request.setAttribute("listaEmpresas", listaEmpresas);


        try {
            int id = Integer.parseInt(request.getParameter("pk"));
            Empresa empresaModal = dao.read(id); // Use seu método read(id)
            if (empresaModal != null) {
                request.setAttribute("empresaModal", empresaModal);
                request.setAttribute("abrirModal", "delete");
            } else {
                request.setAttribute("erro", "Empresa com ID " + id + " não encontrada.");
            }
        } catch (NumberFormatException e) {
            System.err.println("ID inválido para delete: " + request.getParameter("pk"));
            request.setAttribute("erro", "ID inválido fornecido para deleção.");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro ao buscar dados da empresa para deletar.");
        }

        request.getRequestDispatcher("/WEB-INF/empresas.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        EmpresaDAO dao = new EmpresaDAO();
        int id = 0; // Inicializa o ID

        try {
            id = Integer.parseInt(request.getParameter("pk"));

            int resultado = dao.delete(id);

            if (resultado > 0) {

                System.out.println("Empresa ID " + id + " deletada com sucesso.");
                response.sendRedirect(request.getContextPath() + "/empresas-crud");
                return;
            } else {
                request.setAttribute("erro", "Não foi possível deletar a empresa (ID: " + id + "). " +
                        "Verifique se ela não está sendo usada por outros registros.");
            }

        } catch (NumberFormatException e) {
            request.setAttribute("erro", "ID inválido fornecido para deleção.");
            System.err.println("ID inválido para delete: " + request.getParameter("pk"));
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro inesperado ao processar a deleção: " + e.getMessage());
        }

        System.err.println("Falha ao deletar empresa ID " + id + ". Fazendo forward para o JSP com erro.");

        List<Empresa> listaEmpresas = dao.read();
        request.setAttribute("listaEmpresas", listaEmpresas);

        if (id > 0) {
            try {
                Empresa empresaModal = dao.read(id);
                if(empresaModal != null) {
                    request.setAttribute("empresaModal", empresaModal);
                }
            } catch (Exception readEx) {
                System.err.println("Erro ao tentar reler empresa " + id + " após falha no delete: " + readEx.getMessage());
            }
        }
        request.setAttribute("abrirModal", "delete");

        request.getRequestDispatcher("/WEB-INF/pages/empresas.jsp").forward(request, response);
    }
}