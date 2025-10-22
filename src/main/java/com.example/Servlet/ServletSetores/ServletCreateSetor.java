package com.example.Servlet.ServletSetores; // Ajuste seu pacote

import com.example.dao.SetorDAO;
import com.example.Model.Setor;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
// REMOVA a importação não utilizada de ServletException se sua IDE avisar
// import jakarta.servlet.ServletException; // Remova se não usar explicitamente
import java.time.LocalTime; // Adicione se precisar converter tempo (não parece ser o caso aqui)
import java.time.format.DateTimeParseException; // Adicione se precisar converter tempo


@WebServlet("/setor-create") // URL que o formulário do modal 'create' deve chamar
public class ServletCreateSetor extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String nome = request.getParameter("nome");
        String turnos = request.getParameter("turnos");
        String qtdFuncionariosStr = request.getParameter("qtdFuncionarios");
        String descricao = request.getParameter("descricao");
        String idEmpresaStr = request.getParameter("idEmpresa");

        SetorDAO dao = new SetorDAO();

        try {
            // Integer.parseInt pode lançar NumberFormatException (que é um IllegalArgumentException)
            int qtdFuncionarios = Integer.parseInt(qtdFuncionariosStr);
            int idEmpresa = Integer.parseInt(idEmpresaStr);

            // Construtor/Setters podem lançar IllegalArgumentException, NullPointerException, IllegalStateException
            Setor novoSetor = new Setor(
                    nome,
                    descricao,
                    turnos,
                    qtdFuncionarios,
                    idEmpresa
            );

            boolean sucesso = dao.create(novoSetor);

            if (sucesso) {
                System.out.println("Setor criado com sucesso!");
                response.sendRedirect(request.getContextPath() + "/setores-crud");
                return; // Encerra após redirect
            } else {
                request.setAttribute("erro", "Erro ao cadastrar setor no banco de dados. Verifique os dados, especialmente o ID da Empresa.");
            }

        } catch (IllegalArgumentException | NullPointerException | IllegalStateException e) {
            // --- CORREÇÃO AQUI ---
            // REMOVIDO NumberFormatException da lista.
            // IllegalArgumentException já cobre NumberFormatException.
            request.setAttribute("erro", "Erro de validação ou formato inválido: " + e.getMessage());

            // Mantém os dados preenchidos
            request.setAttribute("nome_previo", nome);
            request.setAttribute("turnos_previo", turnos);
            request.setAttribute("qtdFuncionarios_previo", qtdFuncionariosStr);
            request.setAttribute("descricao_previo", descricao);
            request.setAttribute("idEmpresa_previo", idEmpresaStr);

        } // O catch específico para NumberFormatException JÁ TINHA SIDO REMOVIDO


        // --- PLANO B (Se deu erro) ---
        System.err.println("Falha na criação do setor. Fazendo forward para o JSP com erro.");

        List<Setor> listaSetores = dao.read(); // Recarrega lista
        request.setAttribute("listaSetores", listaSetores);
        request.setAttribute("abrirModal", "create"); // Avisa para reabrir modal
        request.getRequestDispatcher("/WEB-INF/setores.jsp").forward(request, response); // Faz forward com erro
    }
}