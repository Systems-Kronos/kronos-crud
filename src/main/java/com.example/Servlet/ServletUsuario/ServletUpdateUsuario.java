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
 * Servlet focado em ATUALIZAR (Update) um Usuário.
 * Usa doGet para carregar o modal com dados existentes e doPost para executar a atualização.
 * Segue o padrão dos Servlets de Administracao e Habilidades.
 */
@WebServlet("/usuario-update")
public class ServletUpdateUsuario extends HttpServlet {

    /*
     * Prepara a página para a edição do Usuário.
     * Carrega a lista completa e, com o ID fornecido,
     * busca o usuário específico (sem habilidades) para preencher o modal de edição.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UsuarioDAO dao = new UsuarioDAO();
        List<Usuario> listaUsuarios = new ArrayList<>(); // Inicia vazia por segurança
        String erro = null;

        try {
            // 1. Busca a lista completa (com habilidades) para a tabela de fundo
            //    Considerar otimização se a tabela não precisar das habilidades.
            listaUsuarios = dao.read(); // Pode lançar SQLException

            // 2. Pega o ID da URL para carregar o modal de edição
            String idParam = request.getParameter("id");
            Usuario usuarioModal = null;

            if (idParam != null && !idParam.isEmpty()) {
                int id = Integer.parseInt(idParam); // Pode lançar NumberFormatException
                // Busca o usuário SEM habilidades para o modal (mais eficiente)
                usuarioModal = dao.read(id); // Pode lançar SQLException

                if (usuarioModal != null) {
                    request.setAttribute("usuarioModal", usuarioModal); // Envia objeto para o JSP
                    request.setAttribute("abrirModal", "update"); // Avisa o JSP para abrir o modal
                    List<Integer> idsHabilidades = dao.getHabilidadeIdsPorUsuario(id);
                    request.setAttribute("habilidades_previas_ids", idsHabilidades); // Passa List<Integer>
                } else {
                    erro = "Usuário ID " + id + " não encontrado (doGet).";
                }
            }
            // Se idParam for nulo, apenas carrega a página sem modal

        } catch (SQLException e) {
            e.printStackTrace();
            erro = "Erro de banco de dados ao carregar dados: " + e.getMessage();
        } catch (NumberFormatException e) {
            erro = "ID inválido fornecido (doGet): " + request.getParameter("id");
            System.err.println("ID inválido ('id') update usuário (doGet): " + request.getParameter("id"));
        } catch (Exception e) { // Outros erros inesperados
            e.printStackTrace();
            erro = "Erro inesperado ao buscar dados (doGet): " + e.getMessage();
        }

        // Encaminhamento para o JSP
        if (erro != null) {
            request.setAttribute("erro", erro);
        }
        request.setAttribute("listaUsuarios", listaUsuarios); // Envia a lista (mesmo que vazia)
        // Confirme o nome do seu JSP de usuários
        request.getRequestDispatcher("/WEB-INF/pages/usuario.jsp").forward(request, response);
    }

    /*
     * Processa a atualização de um Usuário e suas Habilidades associadas via POST request.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        UsuarioDAO dao = new UsuarioDAO();
        int id = 0; // Inicializa ID
        boolean success = false;
        String erro = null;

        // 1. Coleta de parâmetros básicos (declarados fora para usar no catch/finally)
        String idParam = request.getParameter("id");
        String nome = request.getParameter("nome");
        String cpf = request.getParameter("cpf");
        String telefone = request.getParameter("telefone");
        String generoStr = request.getParameter("genero");
        String status = request.getParameter("status");
        String senha = request.getParameter("senha");
        String idSetorStr = request.getParameter("idSetor");
        String idSupervisorStr = request.getParameter("idSupervisor");
        String cargo = request.getParameter("cargo");

        String[] idsHabilidadesSubmetidasStr = request.getParameterValues("habilidadeId");
        List<Integer> idsHabilidadesSubmetidas = new ArrayList<>();
        if (idsHabilidadesSubmetidasStr != null) {
            for (String idStr : idsHabilidadesSubmetidasStr) {
                try {
                    idsHabilidadesSubmetidas.add(Integer.parseInt(idStr));
                } catch (NumberFormatException e) {
                    // Ignora discretamente IDs de habilidades inválidos/maliciosos
                    System.err.println("Ignorando habilidadeId inválido: " + idStr);
                }
            }
        }

        try {
            // 2. Conversão e Validação Preliminar
            id = Integer.parseInt(idParam); // Pode lançar NumberFormatException
            int idSetor = Integer.parseInt(idSetorStr);
            int idSupervisor = 0; // Ajustar se 0 não for válido
            if (idSupervisorStr != null && !idSupervisorStr.trim().isEmpty()) {
                idSupervisor = Integer.parseInt(idSupervisorStr);
            }
            Character genero = null;
            if (generoStr != null && !generoStr.trim().isEmpty()) {
                genero = generoStr.trim().charAt(0);
            } else {
                throw new IllegalArgumentException("Gênero não pode ser vazio.");
            }

            // 3. Busca o objeto original no banco (sem habilidades)
            Usuario usuarioParaAtualizar = dao.read(id); // Pode lançar SQLException
            if (usuarioParaAtualizar == null) {
                throw new Exception("Usuário ID " + id + " não encontrado para atualizar.");
            }

            // 4. Aplica as mudanças (Model dispara validações)
            usuarioParaAtualizar.setNome(nome);
            usuarioParaAtualizar.setCpf(cpf);
            usuarioParaAtualizar.setTelefone(telefone);
            usuarioParaAtualizar.setGenero(genero);
            usuarioParaAtualizar.setStatus(status);
            usuarioParaAtualizar.setIdSetor(idSetor);
            usuarioParaAtualizar.setIdSupervisor(idSupervisor);
            usuarioParaAtualizar.setCargo(cargo);

            // 5. Tratar atualização de senha
            if (senha != null && !senha.trim().isEmpty()) {
                usuarioParaAtualizar.setSenha(senha);
            }

            // 6. Persiste dados básicos do USUÁRIO
            int resultado = dao.update(usuarioParaAtualizar); // Pode lançar SQLException

            if (resultado > 0) {
                // 7. Sincronizar Habilidades
                // Isso só acontece se a atualização principal do usuário for bem-sucedida.
                try {
                    // 7A. Busca as habilidades que o usuário tem AGORA no banco
                    List<Integer> idsAtuaisDoBanco = dao.getHabilidadeIdsPorUsuario(id);

                    // 7B. Calcula o que adicionar:
                    // Itera sobre as habilidades submetidas (do formulário).
                    // Se uma submetida NÃO ESTÁ no banco, adiciona.
                    for (int idSubmetido : idsHabilidadesSubmetidas) {
                        if (!idsAtuaisDoBanco.contains(idSubmetido)) {
                            System.out.println("Adicionando habilidade " + idSubmetido + " ao usuário " + id);
                            dao.addHabilidadeToUsuario(id, idSubmetido);
                        }
                    }

                    // 7C. Calcula o que remover:
                    // Itera sobre as habilidades do banco.
                    // Se uma do banco NÃO ESTÁ na lista submetida, remove.
                    for (int idDoBanco : idsAtuaisDoBanco) {
                        if (!idsHabilidadesSubmetidas.contains(idDoBanco)) {
                            System.out.println("Removendo habilidade " + idDoBanco + " do usuário " + id);
                            dao.removeHabilidadeFromUsuario(id, idDoBanco);
                        }
                    }

                } catch (SQLException syncException) {
                    // Erro parcial: A atualização do usuário funcionou, mas a sincronização de habilidades falhou.
                    syncException.printStackTrace();
                    // Esta mensagem de "erro_parcial" deve ser lida e exibida no JSP da *próxima* página (o 'usuarios-crud').
                    request.getSession().setAttribute("erro_parcial", "Usuário atualizado, mas falha ao sincronizar habilidades: " + syncException.getMessage());
                }

                success = true; // Confirma sucesso da operação principal
            } else {
                erro = "Não foi possível atualizar o usuário (ID: " + id + ").";
            }

            // Blocos Catch
        } catch (IllegalArgumentException | NullPointerException e) {
            erro = "Erro de validação ou formato inválido: " + e.getMessage();

        } catch (SQLException e) {
            e.printStackTrace();
            if (e.getMessage().contains("Duplicate entry") || e.getMessage().contains("UNIQUE constraint failed")) {
                erro = "Erro: Já existe um usuário com este CPF ou outros dados únicos.";
            } else if (e.getMessage().contains("violates foreign key constraint")) {
                erro = "Erro: O Setor ou Supervisor informado não existe ou é inválido.";
            } else {
                erro = "Erro de banco de dados ao atualizar usuário: " + e.getMessage();
            }

        } catch (Exception e) {
            e.printStackTrace();
            erro = "Erro inesperado ao atualizar usuário: " + e.getMessage();
        }

        // Fluxo de Resposta (Padrão)
        if (success) {
            System.out.println("Usuário ID " + id + " atualizado com sucesso.");
            response.sendRedirect(request.getContextPath() + "/usuarios-crud");
            return; // Encerra a execução
        }

        // Caminho de Falha (Forward)
        System.err.println("Falha ao atualizar usuário ID " + id + ". Fazendo forward. Erro: " + erro);

        // Define atributos para repopulação
        request.setAttribute("erro", erro);
        request.setAttribute("nome_previo", nome);
        request.setAttribute("cpf_previo", cpf);
        request.setAttribute("telefone_previo", telefone);
        request.setAttribute("genero_previo", generoStr);
        request.setAttribute("status_previo", status);
        request.setAttribute("idSetor_previo", idSetorStr);
        request.setAttribute("idSupervisor_previo", idSupervisorStr);
        request.setAttribute("cargo_previo", cargo);
        request.setAttribute("habilidades_previas_ids", idsHabilidadesSubmetidas); // Passa a List<Integer>

        // Recarrega a lista de fundo (com habilidades)
        List<Usuario> listaUsuarios = new ArrayList<>();
        try {
            listaUsuarios = dao.read(); // Carrega com JOINs
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("erro", erro + " | ERRO ADICIONAL: Falha ao recarregar a lista.");
        }
        request.setAttribute("listaUsuarios", listaUsuarios);

        // Tenta recarregar o objeto do modal (sem habilidades)
        if (id > 0) {
            try {
                request.setAttribute("usuarioModal", dao.read(id));
            } catch (Exception readEx) {
                System.err.println("Falha ao recarregar dados do modal de update (ID: " + id + "): " + readEx.getMessage());
            }
        }

        request.setAttribute("abrirModal", "update");
        // Precisamos reenviar a lista de todas as habilidades para o JSP
        try {
            HabilidadesDAO hDAO = new HabilidadesDAO();
            request.setAttribute("todasAsHabilidades", hDAO.read());
        } catch (SQLException e) {
            request.setAttribute("erro", erro + " | ERRO ADICIONAL: Falha ao recarregar a lista de checkboxes.");
        }

        request.getRequestDispatcher("/WEB-INF/pages/usuario.jsp").forward(request, response);
    }
}