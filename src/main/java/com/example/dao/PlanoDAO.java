package com.example.dao;

import com.example.Controller.Conexao;
import com.example.Model.Plano;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Classe DAO (Data Access Object) para a entidade Plano.
 * Responsável pelas operações CRUD no banco de dados.
 * Segue o padrão de propagar SQLException e usar try-with-resources.
 */
public class PlanoDAO {

    /*
     * Cria um novo registro de plano no banco de dados.
     */
    public boolean create(Plano plano) throws SQLException {
        Conexao conexao = new Conexao();
        String createSQL = "INSERT INTO planos (nomeplano, custo, descricao, qnt_max_funcionario) VALUES (?, ?, ?, ?)";

        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(createSQL)) {

            pstmt.setString(1, plano.getNome());
            pstmt.setFloat(2, plano.getCusto());
            pstmt.setString(3, plano.getDescricao());
            pstmt.setInt(4, plano.getMaxFuncionarios());

            return pstmt.executeUpdate() > 0;
        }
    }

    /*
     * Busca todos os planos cadastrados, ordenados por ID.
     */
    public List<Plano> read() throws SQLException {
        Conexao conexao = new Conexao();
        String readSQL = "SELECT * FROM planos ORDER BY id ASC";
        List<Plano> listaPlanos = new LinkedList<>();

        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(readSQL);
             ResultSet rset = pstmt.executeQuery()) {

            while (rset.next()) {
                Plano plano = new Plano(
                        rset.getInt("id"),
                        rset.getString("nomeplano"),
                        rset.getFloat("custo"),
                        rset.getString("descricao"),
                        rset.getInt("qnt_max_funcionario")
                );
                listaPlanos.add(plano);
            }
        }
        return listaPlanos;
    }

    /*
     * Busca planos filtrando por nome (case-insensitive) e permitindo ordenação.
     * Usa validação de coluna (whitelisting) e prepared statements para segurança.
     */
    public List<Plano> read(String nome, String orderBy, String direction) throws SQLException {
        Conexao conexao = new Conexao();
        List<Plano> listaPlanos = new LinkedList<>();

        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM planos WHERE 1=1");
        List<Object> parametros = new LinkedList<>();

        // 1. Filtro de nome (SQL Injection prevenido)
        if (nome != null && !nome.trim().isEmpty()) {
            sqlBuilder.append(" AND nomeplano ILIKE ?");
            parametros.add("%" + nome.trim() + "%");
        }

        // 2. Validação da coluna de ordenação (Simplificada)
        String colunaOrdenacao = "id"; // Default seguro

        if (orderBy != null) {
            String lowerOrderBy = orderBy.trim().toLowerCase();
            if (lowerOrderBy.equals("nome")) {
                colunaOrdenacao = "nomeplano";
            } else if (lowerOrderBy.equals("custo")) {
                colunaOrdenacao = "custo";
            } else if (lowerOrderBy.equals("descricao")) {
                colunaOrdenacao = "descricao";
            } else if (lowerOrderBy.equals("qnt_max_funcionario")) {
                colunaOrdenacao = "qnt_max_funcionario";
            }
        }

        // 3. Validação da direção
        String dir = "ASC";
        if (direction != null && direction.trim().equalsIgnoreCase("DESC")) {
            dir = "DESC";
        }

        sqlBuilder.append(" ORDER BY ").append(colunaOrdenacao).append(" ").append(dir);

        String sql = sqlBuilder.toString();

        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Define os parâmetros (para o filtro LIKE)
            for (int i = 0; i < parametros.size(); i++) {
                pstmt.setObject(i + 1, parametros.get(i));
            }

            try (ResultSet rset = pstmt.executeQuery()) {
                while (rset.next()) {
                    Plano plano = new Plano(
                            rset.getInt("id"),
                            rset.getString("nomeplano"),
                            rset.getFloat("custo"),
                            rset.getString("descricao"),
                            rset.getInt("qnt_max_funcionario")
                    );
                    listaPlanos.add(plano);
                }
            }
        }
        return listaPlanos;
    }

    /*
     * Busca um plano específico pelo ID.
     */
    public Plano read(int id) throws SQLException {
        Conexao conexao = new Conexao();
        String readIdSQL = "SELECT * FROM planos WHERE id = ?";
        Plano plano = null;

        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(readIdSQL)) {

            pstmt.setInt(1, id);

            try (ResultSet rset = pstmt.executeQuery()) {
                if (rset.next()) {
                    plano = new Plano(
                            rset.getInt("id"),
                            rset.getString("nomeplano"),
                            rset.getFloat("custo"),
                            rset.getString("descricao"),
                            rset.getInt("qnt_max_funcionario")
                    );
                }
            }
        }
        return plano;
    }

    /*
     * Atualiza um plano existente com base em um objeto Plano.
     */
    public int update(Plano plano) throws SQLException {
        Conexao conexao = new Conexao();
        String updateSQL = "UPDATE planos SET nomeplano = ?, custo = ?, descricao = ?, qnt_max_funcionario = ? WHERE id = ?";

        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {

            pstmt.setString(1, plano.getNome());
            pstmt.setFloat(2, plano.getCusto());
            pstmt.setString(3, plano.getDescricao());
            pstmt.setInt(4, plano.getMaxFuncionarios());
            pstmt.setInt(5, plano.getId());

            return pstmt.executeUpdate();
        }
    }

    /*
     * Atualiza um plano existente com base nos parâmetros individuais.
     */
    public int update(int id, String nome, float custo, String descricao, int qntMaxFuncionario) throws SQLException {
        Conexao conexao = new Conexao();
        String updateSQL = "UPDATE planos SET nomeplano = ?, custo = ?, descricao = ?, qnt_max_funcionario = ? WHERE id = ?";

        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {

            pstmt.setString(1, nome);
            pstmt.setFloat(2, custo);
            pstmt.setString(3, descricao);
            pstmt.setInt(4, qntMaxFuncionario);
            pstmt.setInt(5, id);

            return pstmt.executeUpdate();
        }
    }

    /*
     * Exclui um plano do banco de dados pelo ID.
     */
    public int delete(int id) throws SQLException {
        Conexao conexao = new Conexao();
        String deleteSQL = "DELETE FROM planos WHERE id = ?";

        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate();
        }
    }

    /*
     * Exclui um plano do banco de dados pelo nome.
     */
    public int delete(String nomePlano) throws SQLException {
        Conexao conexao = new Conexao();
        String deleteSQL = "DELETE FROM planos WHERE nomeplano = ?";

        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {

            pstmt.setString(1, nomePlano);
            return pstmt.executeUpdate();
        }
    }
}