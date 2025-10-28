package com.example.dao;
import com.example.Controller.Conexao;
import com.example.Model.Administracao;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Classe DAO (Data Access Object) para a entidade Administracao.
 * Responsável pelas operações CRUD (Create, Read, Update, Delete) no banco de dados.
 */
public class AdministracaoDAO {
    /*
     * Cria um novo registro de administrador no banco de dados.
     */
    public boolean create(Administracao administracao) throws SQLException{
        String sql = "INSERT INTO administracao (nome, email, senha) VALUES (?,?,?)";
        Conexao conexao = new Conexao();

        // Usa try-with-resources para garantir fechamento de Connection e PreparedStatement
        try (Connection conn = conexao.conectar(); // Pode lançar SQLException
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, administracao.getNome());
            pstmt.setString(2, administracao.getEmail());
            pstmt.setString(3, administracao.getSenha());

            // Retorna true se inseriu
            return pstmt.executeUpdate() > 0;
        } // rset, pstmt e conn são fechados automaticamente
        // SQLException é propagada se ocorrer
    }

    /*
     * Busca todos os registros de administradores no banco de dados.
     */
    public List<Administracao> read() throws SQLException {
        String sql = "SELECT * FROM administracao ORDER BY id ASC";
        Conexao conexao = new Conexao();
        List<Administracao> listaAdministracao = new LinkedList<>();

        // Usa try-with-resources
        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rset = pstmt.executeQuery()) {

            while (rset.next()) {
                // Cria o objeto Administracao
                Administracao admin = new Administracao(
                        rset.getInt("id"),
                        rset.getString("nome"),
                        rset.getString("email"),
                        rset.getString("senha")
                );
                listaAdministracao.add(admin);
            }
        } // rset, pstmt e conn são fechados automaticamente
        // SQLException é propagada se ocorrer
        return listaAdministracao;
    }

    /*
     * Busca administradores filtrando por nome (case-insensitive) e ordenando.
     */
    public List<Administracao> read(String nome, String orderBy, String direction) throws SQLException{
        Conexao conexao = new Conexao();
        List<Administracao> listaAdministracao = new LinkedList<>();

        // Usa StringBuilder para construir a query dinamicamente de forma segura
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM administracao");
        List<Object> parametros = new LinkedList<>(); // Lista para guardar parâmetros do PreparedStatement

        // Adiciona filtro WHERE com placeholder se 'nome' for fornecido
        if (nome != null && !nome.trim().isEmpty()) {
            sqlBuilder.append(" WHERE nome ILIKE ?");
            parametros.add("%" + nome.trim() + "%"); // Adiciona valor à lista de parâmetros
        }

        // Validação (Whitelisting) da coluna de ordenação
        String colunaOrdenacao = "id"; // Default seguro
        if (orderBy != null) {
            String lowerOrderBy = orderBy.trim().toLowerCase();
            if (lowerOrderBy.equals("nome")) {
                colunaOrdenacao = "nome";
            } else if (lowerOrderBy.equals("email")) {
                colunaOrdenacao = "email";
            }
        }

        // Validação da direção da ordenação
        String dir = "ASC";
        if (direction != null && direction.trim().equalsIgnoreCase("DESC")) {
            dir = "DESC";
        }

        // Adiciona ORDER BY seguro (após validação/whitelisting)
        sqlBuilder.append(" ORDER BY ").append(colunaOrdenacao).append(" ").append(dir);
        String sql = sqlBuilder.toString();

        // Usa try-with-resources
        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Define os parâmetros na ordem em que foram adicionados
            for (int i = 0; i < parametros.size(); i++) {
                pstmt.setObject(i + 1, parametros.get(i));
            }

            // Executa a query
            try (ResultSet rset = pstmt.executeQuery()) {
                // Processa os resultados
                while (rset.next()) {
                    Administracao admin = new Administracao(
                            rset.getInt("id"),
                            rset.getString("nome"),
                            rset.getString("email"),
                            rset.getString("senha")
                    );
                    listaAdministracao.add(admin);
                }
            } // rset é fechado automaticamente
        } // pstmt e conn são fechados automaticamente
        // SQLException é propagada
        return listaAdministracao;
    }

    /*
     * Busca um administrador pelo seu ID.
     */
    public Administracao read(int id) throws SQLException {
        String sql = "SELECT * FROM administracao WHERE id = ?";
        Conexao conexao = new Conexao();
        Administracao admin = null;

        // Usa try-with-resources
        try (Connection conn = conexao.conectar();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rset = pstmt.executeQuery()) {
                if (rset.next()) {
                    // Cria o objeto se encontrou
                    admin = new Administracao(
                            rset.getInt("id"),
                            rset.getString("nome"),
                            rset.getString("email"),
                            rset.getString("senha")
                    );
                }
            } // rset é fechado automaticamente
        } // pstmt e conn são fechados automaticamente
        // SQLException é propagada
        return admin; // Retorna o objeto ou null se não encontrou
    }


    public Administracao read(String email, String senha) throws SQLException {
        String sql = "SELECT * FROM administracao WHERE email = ? and senha = ?";
        Conexao conexao = new Conexao();
        Administracao admin = null;

        // Usa try-with-resources
        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            pstmt.setString(2, senha);

            try (ResultSet rset = pstmt.executeQuery()) {
                if (rset.next()) {
                    admin = new Administracao(
                            rset.getInt("id"),
                            rset.getString("nome"),
                            rset.getString("email"),
                            rset.getString("senha")
                    );
                }
            }
        }
        return admin;
    }

    /*
     * Atualiza os dados de um administrador existente no banco, baseado no objeto.
     */
    public int update(Administracao administracao) throws SQLException {
        String sql = "UPDATE administracao SET nome = ?, email = ?, senha = ? WHERE id = ?";
        Conexao conexao = new Conexao();

        // Usa try-with-resources
        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, administracao.getNome());
            pstmt.setString(2, administracao.getEmail());
            pstmt.setString(3, administracao.getSenha());
            pstmt.setInt(4, administracao.getId());

            if (pstmt.executeUpdate() > 0) {
                return 1;
            }
            return 0;
        } // conn e pstmt são fechados automaticamente
        // SQLException é propagada
    }


    /*
     * Atualiza os dados de um administrador existente no banco, baseado nos parâmetros.
     */
    public int update(String nome, String email, String senha, int id) throws SQLException {
        String sql = "UPDATE administracao SET nome = ?, email = ?, senha = ? WHERE id = ?";
        Conexao conexao = new Conexao();

        // Usa try-with-resources
        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nome);
            pstmt.setString(2, email);
            pstmt.setString(3, senha);
            pstmt.setInt(4, id);

            if (pstmt.executeUpdate() > 0) {
                return 1;
            }
            return 0;
        } // conn e pstmt são fechados automaticamente
        // SQLException é propagada
    }

    /*
     * Exclui um administrador do banco de dados pelo ID.
     */
    public int delete(int id) throws SQLException {
        String sql = "DELETE FROM administracao WHERE id = ?";
        Conexao conexao = new Conexao();

        // Usa try-with-resources
        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            if (pstmt.executeUpdate() > 0) {
                return 1;
            }
            return 0;
        } // conn e pstmt são fechados automaticamente
        // SQLException é propagada
    }


    /*
     * Exclui um administrador do banco de dados pelo nome.
     */
    public int delete(String nome) throws SQLException {
        String sql = "DELETE FROM administracao WHERE nome = ?";
        Conexao conexao = new Conexao();

        // Usa try-with-resources
        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nome);

            if (pstmt.executeUpdate() > 0) {
                return 1;
            }
            return 0;
        } // conn e pstmt são fechados automaticamente
        // SQLException é propagada
    }
}
