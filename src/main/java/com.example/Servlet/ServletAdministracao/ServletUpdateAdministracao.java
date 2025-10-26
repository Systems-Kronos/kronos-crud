package com.example.Servlet.ServletAdministracao;

import com.example.dao.AdministracaoDAO;
import com.example.Model.Administracao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin-update")
public class ServletUpdateAdministracao extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        AdministracaoDAO dao = new AdministracaoDAO();
        List<Administracao> listaAdmins = dao.read();
        request.setAttribute("listaAdmins", listaAdmins);
        String pk = request.getParameter("pk");
        Administracao adminModal = null;

        try {
            int id = Integer.parseInt(pk);
            adminModal = dao.read(id);
            if (adminModal != null) {
                request.setAttribute("adminModal", adminModal);
                request.setAttribute("abrirModal", "update");
            } else {
                request.setAttribute("erro", "Admin ID " + id + " não encontrado (doGet).");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("erro", "ID inválido fornecido (doGet).");
            System.err.println("ID inválido ('pk') em admin doGet: " + pk);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro ao buscar dados admin (doGet).");
        }
        request.getRequestDispatcher("/WEB-INF/pages/administrador.jsp").forward(request, response); // Caminho para JSP de Admin
    }

    // doPost focado em logging e fluxo correto
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        AdministracaoDAO dao = new AdministracaoDAO();
        int id = 0;
        String nome = null; // Inicializa variáveis para logging no catch
        String email = null;
        String novaSenha = null;
        boolean sucessoNaOperacao = false; // Flag para controlar redirect vs forward

        try {
            // --- Logging Detalhado dos Parâmetros Recebidos ---
            System.out.println("--- Servlet Update Admin (doPost) ---");
            id = Integer.parseInt(request.getParameter("pk"));
            nome = request.getParameter("nome");
            email = request.getParameter("email");
            novaSenha = request.getParameter("senha"); // Pode ser ""
            System.out.println("Recebido do formulário: ID=" + id + ", Nome=" + nome + ", Email=" + email + ", Senha fornecida? " + (novaSenha != null && !novaSenha.trim().isEmpty()));
            // --------------------------------------------------

            // Busca objeto original (pode lançar Exception se read falhar)
            Administracao adminParaAtualizar = dao.read(id);
            if (adminParaAtualizar == null) {
                System.err.println("Erro: Admin ID " + id + " não encontrado pelo DAO antes do update.");
                throw new Exception("Administrador ID " + id + " não encontrado para atualizar.");
            }
            System.out.println("Objeto original encontrado: ID=" + adminParaAtualizar.getId() + ", Nome=" + adminParaAtualizar.getNome());

            // Atualiza o objeto (pode lançar exceções de validação do Model)
            System.out.println("Aplicando novos dados ao objeto...");
            adminParaAtualizar.setNome(nome);
            adminParaAtualizar.setEmail(email);

            // Atualiza senha SOMENTE se fornecida
            if (novaSenha != null && !novaSenha.trim().isEmpty()) {
                System.out.println("Nova senha fornecida. Aplicando ao objeto...");
                // !!! CRIPTOGRAFIA DEVERIA ESTAR AQUI ANTES DO SET !!!
                adminParaAtualizar.setSenha(novaSenha);
            } else {
                System.out.println("Nenhuma nova senha fornecida. Senha antiga será mantida.");
            }

            // Chama o DAO para salvar no banco
            System.out.println("Chamando dao.update() com o objeto atualizado...");
            int resultado = dao.update(adminParaAtualizar); // O DAO retorna int
            System.out.println("Resultado retornado pelo dao.update(): " + resultado);

            if (resultado > 0) {
                System.out.println("Update BEM SUCEDIDO no DAO. Redirecionando...");
                sucessoNaOperacao = true; // Marca para fazer redirect
            } else if (resultado == 0) {
                System.err.println("DAO Update retornou 0. Nenhuma linha foi atualizada (ID " + id + " provavelmente não encontrado no momento do UPDATE).");
                request.setAttribute("erro", "Não foi possível atualizar: Registro não encontrado no banco no momento da alteração.");
            } else { // resultado == -1 ou outro erro do DAO
                System.err.println("DAO Update retornou erro (<0). Verifique logs do DAO.");
                request.setAttribute("erro", "Erro no banco de dados ao tentar atualizar.");
            }

        } catch (IllegalArgumentException | NullPointerException | IllegalStateException e) {
            System.err.println("ERRO DE VALIDAÇÃO (Model) ou Formato Numérico (ID): " + e.getMessage());
            request.setAttribute("erro", "Erro de validação: " + e.getMessage());
            // Guarda dados para repopular
            request.setAttribute("nome_previo", nome); // Usa as variáveis locais
            request.setAttribute("email_previo", email);

        } catch (Exception e) { // Outros erros (ex: falha no dao.read inicial)
            System.err.println("ERRO INESPERADO no doPost: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("erro", "Erro inesperado: " + e.getMessage());
        }

        // --- Fluxo de Resposta ---
        if (sucessoNaOperacao) {
            // SUCESSO: Redireciona
            response.sendRedirect(request.getContextPath() + "/admin-crud");
        } else {
            // FALHA: Faz forward com erro
            System.err.println("OPERAÇÃO FALHOU. Fazendo forward para o JSP com erro.");

            // Recarrega dados necessários para o JSP
            List<Administracao> listaAdmins = dao.read();
            request.setAttribute("listaAdmins", listaAdmins);
            if (id > 0 && request.getAttribute("adminModal") == null) {
                // Tenta recarregar o modal com dados atuais se possível
                try { request.setAttribute("adminModal", dao.read(id)); } catch (Exception readEx) { /* Ignora */ }
            }

            request.setAttribute("abrirModal", "update"); // Avisa para reabrir
            request.getRequestDispatcher("/WEB-INF/pages/administrador.jsp").forward(request, response);
        }
    }
}