package com.example.Servlet.ServletUsuario;

import com.example.dao.HabilidadesDAO;
import com.example.dao.UsuarioDAO;
import com.example.Model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

/**
 * Servlet focado SOMENTE em CRIAR (Create) um novo Usuário e associar suas habilidades iniciais.
 */
@WebServlet("/usuario-create")
public class ServletCreateUsuario extends HttpServlet {

    /*
     * Processa a criação de um novo Usuário via POST request.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // 1. Coleta de parâmetros
        String nome = request.getParameter("nome");
        String cpf = request.getParameter("cpf");
        String telefone = request.getParameter("telefone");
        String generoStr = request.getParameter("genero");
        String status = request.getParameter("status");
        String senha = request.getParameter("senha");
        String idSetorStr = request.getParameter("idSetor");
        String idSupervisorStr = request.getParameter("idSupervisor");
        String cargo = request.getParameter("cargo");

        // Coleta os IDs das habilidades selecionadas nos checkboxes
        String[] habilidadeIds = request.getParameterValues("habilidadeId");

        UsuarioDAO dao = new UsuarioDAO();
        int novoIdUsuario = -1; // Armazena o ID do usuário recém-criado
        String erro = null;
        boolean success = false; // Flag para controlar o fluxo

        try {
            // 2. Conversão e Validação Preliminar
            int idSetor = Integer.parseInt(idSetorStr);
            int idSupervisor = 0;
            if (idSupervisorStr != null && !idSupervisorStr.trim().isEmpty()) {
                idSupervisor = Integer.parseInt(idSupervisorStr);
            }
            Character genero = null;
            if (generoStr != null && !generoStr.trim().isEmpty()) {
                genero = generoStr.trim().charAt(0);
            } else {
                throw new IllegalArgumentException("Gênero não pode ser vazio.");
            }

            // 3. Validação (Model)
            Usuario novoUsuario = new Usuario(
                    nome, cpf,telefone, genero, status, senha,
                    idSetor, idSupervisor, cargo
            );

            // 4. Persistência (DAO) - Usuário
            novoIdUsuario = dao.create(novoUsuario); // Pode lançar SQLException

            // Se chegou aqui, o usuário foi criado (novoIdUsuario > 0)
            success = true;

            // 5. Persistência (DAO) - Habilidades
            // Associa as habilidades selecionadas ao ID recém-criado
            if (habilidadeIds != null) {
                try {
                    for (String idStr : habilidadeIds) {
                        int idHab = Integer.parseInt(idStr);
                        dao.addHabilidadeToUsuario(novoIdUsuario, idHab); // Pode lançar SQLException
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Ignorando habilidadeId inválido no create: " + e.getMessage());
                } catch (SQLException eHab) {
                    // Erro parcial: Usuário criado, mas habilidades falharam.
                    eHab.printStackTrace();
                    // Usamos a sessão para enviar um "aviso" (flash message) para a próxima página
                    request.getSession().setAttribute("erro_parcial", "Usuário criado (ID: " + novoIdUsuario + "), mas falha ao adicionar habilidades: " + eHab.getMessage());
                }
            }

            // 6. SUCESSO (PRG Pattern)
            System.out.println("Usuário criado com sucesso! ID: " + novoIdUsuario);
            response.sendRedirect(request.getContextPath() + "/usuarios-crud");
            return; // Encerra aqui após redirect

            // Captura erros de validação (Model) ou conversão (parseInt)
        } catch (IllegalArgumentException | NullPointerException e) {
            erro = "Erro de validação: " + e.getMessage();

        } catch (SQLException e) { // Captura erro do DAO.create()
            e.printStackTrace();
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                erro = "Erro: Já existe um usuário com este CPF ou outros dados únicos.";
            } else if (e.getMessage().contains("violates foreign key constraint")) {
                erro = "Erro: O Setor ou Supervisor informado não existe ou é inválido.";
            } else {
                erro = "Erro de banco de dados ao criar usuário: " + e.getMessage();
            }

        } catch (Exception e) { // Captura outros erros inesperados
            e.printStackTrace();
            erro = "Erro inesperado ao criar usuário: " + e.getMessage();
        }

        // 7. CAMINHO DE FALHA (Forward)
        // O código SÓ chega aqui se uma exceção foi pega ANTES do success=true.

        System.err.println("Falha na criação do usuário. Fazendo forward. Erro: " + erro);

        // Define os atributos de erro e de repopulação do formulário
        request.setAttribute("erro", erro);
        request.setAttribute("nome_previo", nome);
        request.setAttribute("cpf_previo", cpf);
        request.setAttribute("telefone_previo", telefone);
        request.setAttribute("genero_previo", generoStr);
        request.setAttribute("status_previo", status);
        request.setAttribute("idSetor_previo", idSetorStr);
        request.setAttribute("idSupervisor_previo", idSupervisorStr);
        request.setAttribute("cargo_previo", cargo);
        request.setAttribute("habilidades_previas_ids", habilidadeIds); // Passa o String[] para repopular checkboxes

        // Recarrega a lista de usuários para a tabela de fundo
        List<Usuario> listaUsuarios = new ArrayList<>();
        try {
            listaUsuarios = dao.read(); // Usa o read() que carrega usuários e habilidades
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("erro", erro + " | ERRO ADICIONAL: Falha ao recarregar a lista de usuários.");
        }
        request.setAttribute("listaUsuarios", listaUsuarios);

        // Recarrega TODAS as habilidades (para os checkboxes do modal)
        try {
            HabilidadesDAO hDAO = new HabilidadesDAO();

            request.setAttribute("todasAsHabilidades", hDAO.read());
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("erro", erro + " | ERRO ADICIONAL: Falha ao recarregar a lista de habilidades.");
        }

        request.setAttribute("abrirModal", "create");
        request.getRequestDispatcher("/WEB-INF/pages/usuario.jsp").forward(request, response);
    }
}