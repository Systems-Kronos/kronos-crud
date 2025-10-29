package com.example.Servlet.ServletSetores;

import com.example.Model.Empresa;
import com.example.dao.EmpresaDAO;
import com.example.dao.SetorDAO;
import com.example.Model.Setor;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

@WebServlet("/setor-update")
public class ServletUpdateSetores extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        SetorDAO dao = new SetorDAO();
        List<Setor> listaSetores = null;
        String erro = null;

        try {
            listaSetores = dao.read();
            if (listaSetores == null) listaSetores = new ArrayList<>();
        } catch(Exception e){
            e.printStackTrace();
            erro = "Erro ao carregar lista de setores.";
            listaSetores = new ArrayList<>();
        }
        request.setAttribute("listaSetores", listaSetores);

        String idParam = request.getParameter("id");
        Setor setorModal = null;

        try {
            int id = Integer.parseInt(idParam);
            setorModal = dao.read(id);
            if (setorModal != null) {
                request.setAttribute("setorModal", setorModal);
                request.setAttribute("abrirModal", "update");
            } else {
                if (erro == null) erro = "Setor ID " + id + " não encontrado.";
            }
        } catch (NumberFormatException e) {
            erro = "ID inválido fornecido.";
            System.err.println("ID inválido ('id') update setor (doGet): " + idParam);
        } catch (Exception e) {
            e.printStackTrace();
            if (erro == null) erro = "Erro ao buscar dados do setor.";
        }

        if (erro != null) {
            request.setAttribute("erro", erro);
        }
        request.getRequestDispatcher("/WEB-INF/pages/setores.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        SetorDAO dao = new SetorDAO();
        int id = 0;
        boolean success = false;


        String idParam = request.getParameter("id");
        String nome = request.getParameter("nome");
        String qtnFuncionariosStr = request.getParameter("qtnFuncionarios");
        String turnos = request.getParameter("turnos");
        String descricao = request.getParameter("descricao");
        String idEmpresaStr = request.getParameter("idEmpresa");

        try {

            id = Integer.parseInt(idParam);
            int qtnFuncionarios = Integer.parseInt(qtnFuncionariosStr);
            int idEmpresa = Integer.parseInt(idEmpresaStr);

            EmpresaDAO empresaDAO = new EmpresaDAO();
            Empresa empresa = empresaDAO.read(idEmpresa);


            Setor setorParaAtualizar = dao.read(id);
            if (setorParaAtualizar == null) {
                throw new Exception("Setor ID " + id + " não encontrado.");
            }


            setorParaAtualizar.setNome(nome);
            setorParaAtualizar.setQntFuncionarios(qtnFuncionarios);
            setorParaAtualizar.setTurnos(turnos);
            setorParaAtualizar.setDescricao(descricao);
            setorParaAtualizar.setEmpresa(empresa);

            // Save
            int resultado = dao.update(setorParaAtualizar);

            if (resultado > 0) {
                success = true;
            } else {
                request.setAttribute("erro", "Não foi possível atualizar (ID: " + id + ").");
            }

        } catch (IllegalArgumentException | NullPointerException | IllegalStateException  e) {
            request.setAttribute("erro", "Erro: " + e.getMessage());
            request.setAttribute("nome_previo", nome);
            request.setAttribute("qtnFuncionarios_previo", qtnFuncionariosStr); // Use correct name
            request.setAttribute("turnos_previo", turnos);
            request.setAttribute("descricao_previo", descricao);
            request.setAttribute("idEmpresa_previo", idEmpresaStr);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro inesperado: " + e.getMessage());
        }


        if (success) {
            response.sendRedirect(request.getContextPath() + "/setores-crud"); // Redirect to sector list
        } else {
            System.err.println("Falha update setor ID " + id + ". Forwarding.");
            List<Setor> listaSetores = null;
            try { listaSetores = dao.read(); } catch (Exception readEx){ listaSetores = new ArrayList<>(); }
            request.setAttribute("listaSetores", listaSetores);


            if (id > 0 && request.getAttribute("setorModal") == null) {
                try { request.setAttribute("setorModal", dao.read(id)); } catch (Exception readEx) { /* Ignore */ }
            }
            request.setAttribute("abrirModal", "update"); // Signal to reopen
            request.getRequestDispatcher("/WEB-INF/pages/setores.jsp").forward(request, response); // Forward JSP
        }
    }
}