package com.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.example.Controller.*;
import com.example.Model.Habilidades;

/**
 * Classe DAO (Data Access Object) para a entidade Habilidades.
 * Responsável pelas operações CRUD no banco de dados.
 */
public class HabilidadesDAO {

    /*
     * Cria um novo registro de habilidade no banco de dados.
     */
    public boolean create(Habilidades habilidade) throws SQLException {
        Conexao conexao = new Conexao();
        String create = "INSERT INTO habilidade (nome, tag, descricao) VALUES (?,?,?)";

        // Usa try-with-resources para garantir fechamento automático
        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(create)) {

            pstmt.setString(1, habilidade.getNome());
            pstmt.setString(2, habilidade.getTag());
            pstmt.setString(3, habilidade.getDescricao());

            return pstmt.executeUpdate() > 0; // true se inseriu
        }
        // SQLException é propagada
    }

    /*
     * Busca todas as Habilidades no banco de dados, ordenadas por ID.
     */
    public List<Habilidades> read() throws SQLException {
        Conexao conexao = new Conexao();
        String read = "SELECT * FROM habilidade ORDER BY id ASC";
        List<Habilidades> listaHabilidade = new LinkedList<>();

        // Usa try-with-resources
        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(read);
             ResultSet rset = pstmt.executeQuery()) {

            while (rset.next()) {
                Habilidades habilidade = new Habilidades(
                        rset.getInt("id"),
                        rset.getString("nome"),
                        rset.getString("tag"),
                        rset.getString("descricao")
                );
                listaHabilidade.add(habilidade);
            }
        }
        // SQLException é propagada
        return listaHabilidade;
    }

    /*
     * Busca Habilidades filtrando por nome (case-insensitive) e permitindo ordenação.
     */
    public List<Habilidades> read(String nome, String orderBy, String direction) throws SQLException {
        Conexao conexao = new Conexao();
        List<Habilidades> listaHabilidades = new LinkedList<>();

        // Usando StringBuilder e placeholders
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM habilidade");
        List<Object> parametros = new LinkedList<>();

        // Adiciona filtro WHERE com placeholder
        if (nome != null && !nome.trim().isEmpty()) {
            sqlBuilder.append(" WHERE nome ILIKE ?");
            parametros.add("%" + nome.trim() + "%");
        }

        // Validação (Whitelisting) da coluna de ordenação
        String colunaOrdenacao = "id"; // Default seguro
        if (orderBy != null) {
            String lowerOrderBy = orderBy.trim().toLowerCase();
            if (lowerOrderBy.equals("nome")) {
                colunaOrdenacao = "nome";
            } else if (lowerOrderBy.equals("tag")) {
                colunaOrdenacao = "tag";
            } else if (lowerOrderBy.equals("descricao")) {
                colunaOrdenacao = "descricao";
            }
        }

        // Validação da direção
        String dir = "ASC";
        if (direction != null && direction.trim().equalsIgnoreCase("DESC")) {
            dir = "DESC";
        }

        // Adiciona ORDER BY seguro (após validação)
        sqlBuilder.append(" ORDER BY ").append(colunaOrdenacao).append(" ").append(dir);
        String sql = sqlBuilder.toString();

        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Define os parâmetros (?)
            for (int i = 0; i < parametros.size(); i++) {
                pstmt.setObject(i + 1, parametros.get(i));
            }

            // Executa e processa
            try (ResultSet rset = pstmt.executeQuery()) {
                while (rset.next()) {
                    Habilidades habilidade = new Habilidades(
                            rset.getInt("id"),
                            rset.getString("nome"),
                            rset.getString("tag"),
                            rset.getString("descricao")
                    );
                    listaHabilidades.add(habilidade);
                }
            }
        }
        // SQLException é propagada
        return listaHabilidades;
    }

    /*
     * Busca uma Habilidade específica pelo seu ID.
     */
    public Habilidades read(int id) throws SQLException {
        Conexao conexao = new Conexao();
        String readId = "SELECT * FROM habilidade WHERE id = ?";
        Habilidades habilidade = null;

        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(readId)) {

            pstmt.setInt(1, id);

            try (ResultSet rset = pstmt.executeQuery()) {
                if (rset.next()) {
                    habilidade = new Habilidades(
                            rset.getInt("id"),
                            rset.getString("nome"),
                            rset.getString("tag"),
                            rset.getString("descricao")
                    );
                }
            }
        }
        // SQLException é propagada
        return habilidade; // Retorna o objeto ou null
    }

    /*
     * Atualiza os dados de uma habilidade existente, baseado em um objeto.
     */
    public int update(Habilidades habilidade) throws SQLException {
        Conexao conexao = new Conexao();
        String update = "UPDATE habilidade SET nome = ?, tag = ?, descricao = ? WHERE id = ?";

        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(update)) {

            pstmt.setString(1, habilidade.getNome());
            pstmt.setString(2, habilidade.getTag());
            pstmt.setString(3, habilidade.getDescricao());
            pstmt.setInt(4, habilidade.getId());

            return pstmt.executeUpdate(); // Retorna o número de linhas afetadas
        }
        // SQLException é propagada
    }

    /*
     * Atualiza os dados de uma habilidade existente, baseado nos parâmetros.
     */
    public int update(int id, String nome, String tag, String descricao) throws SQLException {
        Conexao conexao = new Conexao();
        String update = "UPDATE habilidade SET nome = ?, tag = ?, descricao = ? WHERE id = ?";

        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(update)) {

            pstmt.setString(1, nome);
            pstmt.setString(2, tag);
            pstmt.setString(3, descricao);
            pstmt.setInt(4, id);

            return pstmt.executeUpdate();
        }
        // SQLException é propagada
    }

    /*
     * Exclui uma habilidade do banco de dados pelo ID.
     */
    public int delete(int id) throws SQLException {
        Conexao conexao = new Conexao();
        String delete = "DELETE FROM habilidade WHERE id = ?";

        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(delete)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate();
        }
        // SQLException é propagada
    }

    /*
     * Exclui uma habilidade do banco de dados pelo nome.
     */
    public int delete(String nome) throws SQLException {
        Conexao conexao = new Conexao();
        String delete = "DELETE FROM habilidade WHERE nome = ?";

        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(delete)) {

            pstmt.setString(1, nome);
            return pstmt.executeUpdate();
        }
        // SQLException é propagada
    }
}