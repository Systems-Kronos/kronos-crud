package com.example.dao;

import com.example.Controller.Conexao;
import com.example.Model.Habilidades;
import com.example.Model.Usuario;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * DAO para Usuario. Segue o padrão de propagar SQLException.
 */
public class UsuarioDAO {

    /*
     * Cria um novo usuário e RETORNA O ID gerado.
     */
    public int create(Usuario usuario) throws SQLException {
        Conexao conexao = new Conexao();
        // 1. SQL pede o retorno das chaves geradas (o ID)
        String createSQL = "INSERT INTO usuario (nome, cpf, telefone, genero, status, senha, fk_setor_id, fk_supervisor_id, cargo) VALUES (?,?,?,?,?,?,?,?,?)";

        // 2. try-with-resources para gerenciar Conexão e PreparedStatement
        try (Connection conn = conexao.conectar();
             // 3. Pede ao PreparedStatement para retornar a chave gerada (o ID)
             PreparedStatement pstmt = conn.prepareStatement(createSQL, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, usuario.getNome());
            pstmt.setString(2, usuario.getCpf());
            pstmt.setString(3, usuario.getTelefone());
            pstmt.setString(4, usuario.getGenero() != null ? String.valueOf(usuario.getGenero()) : null);
            pstmt.setString(5, usuario.getStatus());
            pstmt.setString(6, usuario.getSenha());
            pstmt.setInt(7, usuario.getIdSetor());
            pstmt.setInt(8, usuario.getIdSupervisor());
            pstmt.setString(9, usuario.getCargo());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                // 4. Lança exceção se a inserção falhar
                throw new SQLException("Criação do usuário falhou, nenhuma linha afetada.");
            }

            // 5. Busca o ID retornado pelo banco
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Retorna o ID
                } else {
                    throw new SQLException("Criação do usuário falhou, nenhum ID foi retornado.");
                }
            }
        }
        // SQLException (de conexão, duplicata, etc.) é propagada
    }

    /*
     * Busca todos os Usuários e suas Habilidades associadas.
     */
    public List<Usuario> read() throws SQLException {
        Conexao conexao = new Conexao();
        String readSQL = "SELECT u.id AS u_id, u.nome AS u_nome, u.cpf AS u_cpf, u.telefone AS u_telefone, u.genero AS u_genero, " +
                "u.status AS u_status, u.senha AS u_senha, u.fk_setor_id AS u_id_setor, " +
                "u.fk_supervisor_id AS u_id_supervisor, u.cargo AS u_cargo, " +
                "h.id AS h_id, h.nome AS h_nome, h.tag AS h_tag, h.descricao AS h_descricao " +
                "FROM usuario u " +
                "LEFT JOIN usuario_habilidade uh ON u.id = uh.fk_usuario_id " +
                "LEFT JOIN habilidade h ON h.id = uh.fk_habilidade_id " +
                "ORDER BY u.id, h.id";

        List<Usuario> listaUsuario = new LinkedList<>();
        Usuario usuarioAtual = null;

        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(readSQL);
             ResultSet rset = pstmt.executeQuery()) {

            while (rset.next()) {
                int idUsuario = rset.getInt("u_id");
                if (usuarioAtual == null || idUsuario != usuarioAtual.getId()) {
                    usuarioAtual = new Usuario(
                            idUsuario,
                            rset.getString("u_nome"),
                            rset.getString("u_cpf"),
                            rset.getString("u_telefone"),
                            (rset.getString("u_genero") != null && !rset.getString("u_genero").isEmpty()) ? rset.getString("u_genero").charAt(0) : null,
                            rset.getString("u_status"),
                            rset.getString("u_senha"),
                            rset.getInt("u_id_setor"),
                            rset.getInt("u_id_supervisor"),
                            rset.getString("u_cargo")
                    );
                    listaUsuario.add(usuarioAtual);
                }
                int idHabilidade = rset.getInt("h_id");
                if (!rset.wasNull()) {
                    Habilidades habilidade = new Habilidades(
                            idHabilidade,
                            rset.getString("h_nome"),
                            rset.getString("h_tag"),
                            rset.getString("h_descricao")
                    );
                    try {
                        if (usuarioAtual != null) usuarioAtual.adicionarHabilidade(habilidade);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Aviso: Habilidade duplicada (ID: " + idHabilidade + ") para usuário (ID: " + idUsuario + ").");
                    }
                }
            }
        }
        return listaUsuario;
    }

    /*
     * Busca Usuários (e suas Habilidades) filtrando por nome e ordenando.
     */
    public List<Usuario> read(String nome, String orderBy, String direction) throws SQLException {
        Conexao conexao = new Conexao();
        List<Usuario> usuarios = new LinkedList<>();
        Usuario usuarioAtual = null;

        List<Object> parametros = new LinkedList<>();
        StringBuilder sqlBuilder = new StringBuilder(
                "SELECT u.id AS u_id, u.nome AS u_nome, u.cpf AS u_cpf, u.telefone AS u_telefone, u.genero AS u_genero, " +
                        "u.status AS u_status, u.senha AS u_senha, u.fk_setor_id AS u_id_setor, " +
                        "u.fk_supervisor_id AS u_id_supervisor, u.cargo AS u_cargo, " +
                        "h.id AS h_id, h.nome AS h_nome, h.tag AS h_tag, h.descricao AS h_descricao " +
                        "FROM usuario u " +
                        "LEFT JOIN usuario_habilidade uh ON u.id = uh.fk_usuario_id " +
                        "LEFT JOIN habilidade h ON h.id = uh.fk_habilidade_id " +
                        "WHERE 1=1 ");

        if (nome != null && !nome.trim().isEmpty()) {
            sqlBuilder.append(" AND u.nome ILIKE ?"); // Placeholder
            parametros.add("%" + nome.trim() + "%");
        }

        // Whitelisting da coluna de ordenação
        String colunaOrdenacao = "u.id";
        if (orderBy != null) {
            String lowerOrderBy = orderBy.trim().toLowerCase();
            if (lowerOrderBy.equals("nome")) colunaOrdenacao = "u.nome";
            else if (lowerOrderBy.equals("cargo")) colunaOrdenacao = "u.cargo";
            else if (lowerOrderBy.equals("status")) colunaOrdenacao = "u.status";
        }
        String dir = "ASC";
        if (direction != null && direction.trim().equalsIgnoreCase("DESC")) dir = "DESC";
        sqlBuilder.append(" ORDER BY ").append(colunaOrdenacao).append(" ").append(dir).append(", h.id");

        String sql = sqlBuilder.toString();

        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Define os parâmetros (?)
            for (int i = 0; i < parametros.size(); i++) {
                pstmt.setObject(i + 1, parametros.get(i));
            }

            try (ResultSet rset = pstmt.executeQuery()) {
                while (rset.next()) {
                    int idUsuario = rset.getInt("u_id");
                    if (usuarioAtual == null || idUsuario != usuarioAtual.getId()) {
                        usuarioAtual = new Usuario(
                                idUsuario,
                                rset.getString("u_nome"),
                                rset.getString("u_cpf"),
                                rset.getString("u_telefone"),
                                (rset.getString("u_genero") != null && !rset.getString("u_genero").isEmpty()) ? rset.getString("u_genero").charAt(0) : null,
                                rset.getString("u_status"),
                                rset.getString("u_senha"),
                                rset.getInt("u_id_setor"),
                                rset.getInt("u_id_supervisor"),
                                rset.getString("u_cargo")
                        );
                        usuarios.add(usuarioAtual);
                    }
                    int idHabilidade = rset.getInt("h_id");
                    if (!rset.wasNull()) {
                        Habilidades habilidade = new Habilidades(
                                idHabilidade,
                                rset.getString("h_nome"),
                                rset.getString("h_tag"),
                                rset.getString("h_descricao")
                        );
                        try {
                            if (usuarioAtual != null) usuarioAtual.adicionarHabilidade(habilidade);
                        } catch (IllegalArgumentException e) {
                            System.err.println("Aviso: Habilidade duplicada (ID: " + idHabilidade + ") para usuário (ID: " + idUsuario + ").");
                        }
                    }
                }
            }
        }
        return usuarios;
    }

    /*
     * Busca um usuário específico pelo ID (NÃO carrega habilidades).
     */
    public Usuario read(int id) throws SQLException {
        Conexao conexao = new Conexao();
        String readIdSQL = "SELECT * FROM usuario WHERE id = ?";
        Usuario usuario = null;

        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(readIdSQL)) {
            pstmt.setInt(1, id);
            try (ResultSet rset = pstmt.executeQuery()) {
                if (rset.next()) {
                    usuario = new Usuario(
                            rset.getInt("id"),
                            rset.getString("nome"),
                            rset.getString("cpf"),
                            rset.getString("telefone"),
                            (rset.getString("genero") != null && !rset.getString("genero").isEmpty()) ? rset.getString("genero").charAt(0) : null,
                            rset.getString("status"),
                            rset.getString("senha"),
                            rset.getInt("fk_setor_id"),
                            rset.getInt("fk_supervisor_id"),
                            rset.getString("cargo")
                    );
                }
            }
        }
        return usuario;
    }

    /*
     * Atualiza dados da tabela 'usuario'. NÃO mexe nas habilidades.
     */
    public int update(Usuario usuario) throws SQLException {
        Conexao conexao = new Conexao();
        String updateSQL = "UPDATE usuario SET nome = ?, cpf = ?, telefone = ?, genero = ?, status = ?, senha = ?, fk_setor_id = ?, fk_supervisor_id = ?, cargo = ? WHERE id = ?";

        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
            pstmt.setString(1, usuario.getNome());
            pstmt.setString(2, usuario.getCpf());
            pstmt.setString(3, usuario.getTelefone());
            pstmt.setString(4, usuario.getGenero() != null ? usuario.getGenero().toString() : null);
            pstmt.setString(5, usuario.getStatus());
            pstmt.setString(6, usuario.getSenha());
            pstmt.setInt(7, usuario.getIdSetor());
            pstmt.setInt(8, usuario.getIdSupervisor());
            pstmt.setString(9, usuario.getCargo());
            pstmt.setInt(10, usuario.getId());
            return pstmt.executeUpdate();
        }
    }

    /*
     * Atualiza dados da tabela 'usuario'. NÃO mexe nas habilidades.
     */
    public int update(int id, String nome, String cpf, String telefone, char genero, String status, String senha, int idSetor, int idSupervisor, String cargo) throws SQLException {
        Conexao conexao = new Conexao();
        String updateSQL = "UPDATE usuario SET nome = ?, cpf = ?, telefone = ?, genero = ?, status = ?, senha = ?, fk_setor_id = ?, fk_supervisor_id = ?, cargo = ? WHERE id = ?";

        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
            pstmt.setString(1, nome);
            pstmt.setString(2, cpf);
            pstmt.setString(3, telefone);
            pstmt.setString(4, String.valueOf(genero));
            pstmt.setString(5, status);
            pstmt.setString(6, senha);
            pstmt.setInt(7, idSetor);
            pstmt.setInt(8, idSupervisor);
            pstmt.setString(9, cargo);
            pstmt.setInt(10, id);
            return pstmt.executeUpdate();
        }
    }

    /*
     * Exclui um usuário da tabela 'usuario'.
     */
    public int delete(int id) throws SQLException {
        Conexao conexao = new Conexao();
        String deleteUsuarioSQL = "DELETE FROM usuario WHERE id = ?";
        try (Connection conn = conexao.conectar();
             PreparedStatement pstmtUsuario = conn.prepareStatement(deleteUsuarioSQL)) {
            pstmtUsuario.setInt(1, id);
            return pstmtUsuario.executeUpdate();
        }
    }

    /*
     * Exclui um usuário pelo nome. (Não recomendado, pode apagar muitos).
     */
    public int delete(String nome) throws SQLException {
        Conexao conexao = new Conexao();
        String deleteSQL = "DELETE FROM usuario WHERE nome = ?";
        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
            pstmt.setString(1, nome);
            return pstmt.executeUpdate();
        }
    }

    // --- MÉTODOS PARA ASSOCIAR/DESASSOCIAR HABILIDADES ---

    /*
     * Associa uma Habilidade a um Usuário na tabela 'usuario_habilidade'.
     */
    public boolean addHabilidadeToUsuario(int idUsuario, int idHabilidade) throws SQLException {
        Conexao conexao = new Conexao();
        String createHabilidadeSQL = "INSERT INTO usuario_habilidade (fk_usuario_id, fk_habilidade_id) VALUES (?, ?)";
        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(createHabilidadeSQL)) {
            pstmt.setInt(1, idUsuario);
            pstmt.setInt(2, idHabilidade);
            return pstmt.executeUpdate() > 0;
        }
    }

    /*
     * Remove UMA associação entre Usuário e Habilidade.
     */
    public int removeHabilidadeFromUsuario(int idUsuario, int idHabilidade) throws SQLException {
        Conexao conexao = new Conexao();
        String removeHabilidadeSQL = "DELETE FROM usuario_habilidade WHERE fk_usuario_id=? AND fk_habilidade_id=?";
        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(removeHabilidadeSQL)) {
            pstmt.setInt(1, idUsuario);
            pstmt.setInt(2, idHabilidade);
            return pstmt.executeUpdate();
        }
    }

    /*
     * Remove TODAS as associações de habilidades para um único usuário.
     */
    public int removeAllHabilidadesFromUsuario(int idUsuario) throws SQLException {
        Conexao conexao = new Conexao();
        String removeAllSQL = "DELETE FROM usuario_habilidade WHERE fk_usuario_id = ?";
        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(removeAllSQL)) {
            pstmt.setInt(1, idUsuario);
            return pstmt.executeUpdate();
        }
    }

    /*
     * Busca APENAS os IDs de todas as habilidades associadas a um usuário.
     */
    public List<Integer> getHabilidadeIdsPorUsuario(int idUsuario) throws SQLException {
        List<Integer> idsHabilidades = new LinkedList<>();
        String sql = "SELECT fk_habilidade_id FROM usuario_habilidade WHERE fk_usuario_id = ?";
        Conexao conexao = new Conexao();
        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            try (ResultSet rset = pstmt.executeQuery()) {
                while (rset.next()) {
                    idsHabilidades.add(rset.getInt("fk_habilidade_id"));
                }
            }
        }
        return idsHabilidades;
    }
}