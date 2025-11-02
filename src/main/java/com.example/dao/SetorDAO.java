package com.example.dao;

import com.example.Controller.Conexao;
import com.example.Model.Setor;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Classe DAO (Data Access Object) para a entidade Setor.
 * Responsável pelas operações CRUD no banco de dados.
 * Segue o padrão de propagar SQLException e usar try-with-resources.
 */
public class SetorDAO {

    /*
     * Cria um novo registro de setor no banco de dados.
     */
    public boolean create(Setor setor) throws SQLException {
        Conexao conexao = new Conexao();
        String createSQL = "INSERT INTO setor (nome, descricao, turnos, qnt_funcionarios, fk_empresa_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(createSQL)) {

            pstmt.setString(1, setor.getNome());
            pstmt.setString(2, setor.getDescricao());
            pstmt.setString(3, setor.getTurnos());
            pstmt.setInt(4, setor.getQntFuncionarios());
            pstmt.setInt(5, setor.getIdEmpresa());

            return pstmt.executeUpdate() > 0;
        }
    }

    /*
     * Busca todos os Setores no banco de dados, ordenados por ID.
     */
    public List<Setor> read() throws SQLException {
        Conexao conexao = new Conexao();
        List<Setor> setores = new LinkedList<>();
        String readSQL = "SELECT id, nome, descricao, turnos, qnt_funcionarios, fk_empresa_id FROM setor ORDER BY id ASC";

        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(readSQL);
             ResultSet rset = pstmt.executeQuery()) {

            while (rset.next()) {
                Setor setor = new Setor(
                        rset.getInt("id"),
                        rset.getString("nome"),
                        rset.getString("descricao"),
                        rset.getString("turnos"),
                        rset.getInt("qnt_funcionarios"),
                        rset.getInt("fk_empresa_id")
                );
                setores.add(setor);
            }
        }
        return setores;
    }

    /*
     * Busca Setores filtrando por nome (case-insensitive) e permitindo ordenação.
     */
    public List<Setor> read(String nome, String orderBy, String direction) throws SQLException {
        Conexao conexao = new Conexao();
        List<Setor> setores = new LinkedList<>();

        List<Object> parametros = new LinkedList<>();
        StringBuilder sqlBuilder = new StringBuilder("SELECT id, nome, descricao, turnos, qnt_funcionarios, fk_empresa_id FROM setor WHERE 1=1");

        if (nome != null && !nome.trim().isEmpty()) {
            sqlBuilder.append(" AND nome ILIKE ?");
            parametros.add("%" + nome.trim() + "%");
        }

        // Whitelisting da coluna de ordenação
        String colunaOrdenacao = "id";
        if (orderBy != null) {
            String lowerOrderBy = orderBy.trim().toLowerCase();
            if (lowerOrderBy.equals("nome")) colunaOrdenacao = "nome";
            else if (lowerOrderBy.equals("qnt_funcionarios")) colunaOrdenacao = "qnt_funcionarios";
        }

        String dir = (direction != null && direction.trim().equalsIgnoreCase("DESC")) ? "DESC" : "ASC";
        sqlBuilder.append(" ORDER BY ").append(colunaOrdenacao).append(" ").append(dir);

        String sql = sqlBuilder.toString();

        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Define os parâmetros (?)
            for (int i = 0; i < parametros.size(); i++) {
                pstmt.setObject(i + 1, parametros.get(i));
            }

            try (ResultSet rset = pstmt.executeQuery()) {
                while (rset.next()) {
                    Setor setor = new Setor(
                            rset.getInt("id"),
                            rset.getString("nome"),
                            rset.getString("descricao"),
                            rset.getString("turnos"),
                            rset.getInt("qnt_funcionarios"),
                            rset.getInt("fk_empresa_id")
                    );
                    setores.add(setor);
                }
            }
        }
        return setores;
    }

    /*
     * Busca um Setor específico pelo seu ID.
     */
    public Setor read(int id) throws SQLException {
        Conexao conexao = new Conexao();
        Setor setor = null;
        String readSQL = "SELECT id, nome, descricao, turnos, qnt_funcionarios, fk_empresa_id FROM setor WHERE id = ?";

        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(readSQL)) {

            pstmt.setInt(1, id);

            try (ResultSet rset = pstmt.executeQuery()) {
                if (rset.next()) {
                    setor = new Setor(
                            rset.getInt("id"),
                            rset.getString("nome"),
                            rset.getString("descricao"),
                            rset.getString("turnos"),
                            rset.getInt("qnt_funcionarios"),
                            rset.getInt("fk_empresa_id")
                    );
                }
            }
        }
        return setor;
    }

    /*
     * Atualiza os dados de um setor existente, baseado em um objeto.
     */
    public int update(Setor setor) throws SQLException {
        Conexao conexao = new Conexao();
        String updateSQL = "UPDATE setor SET nome = ?, descricao = ?, turnos = ?, qnt_funcionarios = ?, fk_empresa_id = ? WHERE id = ?";

        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {

            pstmt.setString(1, setor.getNome());
            pstmt.setString(2, setor.getDescricao());
            pstmt.setString(3, setor.getTurnos());
            pstmt.setInt(4, setor.getQntFuncionarios());
            pstmt.setInt(5, setor.getIdEmpresa());
            pstmt.setInt(6, setor.getId());

            return pstmt.executeUpdate();
        }
    }

    /*
     * Atualiza os dados de um setor existente, baseado nos parâmetros.
     */
    public int update(int id, String nome, String descricao, String turnos, int qntFunc, int idEmpresa) throws SQLException {
        Conexao conexao = new Conexao();
        String updateSQL = "UPDATE setor SET nome = ?, descricao = ?, turnos = ?, qnt_funcionarios = ?, fk_empresa_id = ? WHERE id = ?";

        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {

            pstmt.setString(1, nome);
            pstmt.setString(2, descricao);
            pstmt.setString(3, turnos);
            pstmt.setInt(4, qntFunc);
            pstmt.setInt(5, idEmpresa);
            pstmt.setInt(6, id);

            return pstmt.executeUpdate();
        }
    }

    /*
     * Exclui um setor do banco de dados pelo ID.
     */
    public int delete(int id) throws SQLException {
        Conexao conexao = new Conexao();
        String deleteSQL = "DELETE FROM setor WHERE id = ?";

        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate();
        }
    }

    /*
     * Exclui um setor do banco de dados pelo nome. (Não recomendado).
     */
    public int delete(String nome) throws SQLException {
        Conexao conexao = new Conexao();
        String deleteSQL = "DELETE FROM setor WHERE nome = ?";

        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {

            pstmt.setString(1, nome);
            return pstmt.executeUpdate();
        }
    }

    /*
     * Associa um Setor a uma Empresa (atualiza a FK).
     */
    public boolean addEmpresaToSetor(int idSetor, int idEmpresa) throws SQLException {
        Conexao conexao = new Conexao();
        String addEmpresaSQL = "UPDATE setor SET fk_empresa_id = ? WHERE id = ?";

        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(addEmpresaSQL)) {

            pstmt.setInt(1, idEmpresa);
            pstmt.setInt(2, idSetor);
            return pstmt.executeUpdate() > 0;
        }
    }
}