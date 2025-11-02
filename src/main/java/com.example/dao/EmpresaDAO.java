package com.example.dao;

import com.example.Controller.Conexao;
import com.example.Model.Empresa;

import java.sql.*;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

/**
 * Classe DAO (Data Access Object) para a entidade Empresa.
 * Responsável pelas operações CRUD no banco de dados.
 * Segue o padrão de propagar SQLException e usar try-with-resources.
 */
public class EmpresaDAO {

    /*
     * Cria um novo registro de empresa no banco de dados.
     */
    public boolean create(Empresa empresa) throws SQLException {
        Conexao conexao = new Conexao();
        String createSQL = "INSERT INTO empresa (nome, cep, cnpj, email, telefone, porte, horario_abertura, horario_encerramento, regradenegocio, fk_plano_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Usa try-with-resources para garantir fechamento automático
        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(createSQL)) {

            pstmt.setString(1, empresa.getNome());
            pstmt.setString(2, empresa.getCep());
            pstmt.setString(3, empresa.getCnpj());
            pstmt.setString(4, empresa.getEmail());
            pstmt.setString(5, empresa.getTelefone());
            pstmt.setString(6, empresa.getPorte());
            pstmt.setTime(7, Time.valueOf(empresa.getHorarioAbertura()));
            pstmt.setTime(8, Time.valueOf(empresa.getHorarioFechamento()));
            pstmt.setString(9, empresa.getRegraDeNegocios());
            pstmt.setInt(10, empresa.getIdPlano());

            return pstmt.executeUpdate() > 0;
        }
        // SQLException é propagada
    }

    /*
     * Busca todas as Empresas no banco de dados, ordenadas por ID.
     */
    public List<Empresa> read() throws SQLException {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        List<Empresa> empresas = new LinkedList<>();

        String readSQL = "SELECT * FROM empresa ORDER BY id ASC";

        try (Connection connDb = conexao.conectar();
             PreparedStatement pstmtDb = connDb.prepareStatement(readSQL);
             ResultSet rsetDb = pstmtDb.executeQuery()) {

            while (rsetDb.next()) {
                Empresa empresa = new Empresa(
                        rsetDb.getInt("id"),
                        rsetDb.getString("nome"),
                        rsetDb.getString("cep"),
                        rsetDb.getString("cnpj"),
                        rsetDb.getString("email"),
                        rsetDb.getString("telefone"),
                        rsetDb.getString("porte"),
                        rsetDb.getTime("horario_abertura").toLocalTime(),
                        rsetDb.getTime("horario_encerramento").toLocalTime(),
                        rsetDb.getString("regradenegocio"),
                        rsetDb.getInt("fk_plano_id")
                );
                empresas.add(empresa);
            }
        }
        // SQLException é propagada
        return empresas;
    }

    /*
     * Busca Empresas filtrando por nome (case-insensitive) e permitindo ordenação.
     */
    public List<Empresa> read(String nome, String orderBy, String direction) throws SQLException {
        Conexao conexao = new Conexao();
        List<Empresa> empresas = new LinkedList<>();

        List<Object> parametros = new LinkedList<>();
        // WHERE 1=1 é um placeholder "coringa" que garante que todas as suas condições de
        // filtro dinâmicas possam ser adicionadas usando a palavra-chave AND
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM empresa WHERE 1=1");

        if (nome != null && !nome.trim().isEmpty()) {
            sqlBuilder.append(" AND nome ILIKE ?");
            parametros.add("%" + nome.trim() + "%");
        }

        // Whitelisting da coluna de ordenação
        String colunaOrdenacao = "id";
        if (orderBy != null) {
            String lowerOrderBy = orderBy.trim().toLowerCase();
            if (lowerOrderBy.equals("nome")) colunaOrdenacao = "nome";
            else if (lowerOrderBy.equals("cep")) colunaOrdenacao = "cep";
            // Adicione outras colunas permitidas aqui
        }

        String dir = "ASC";
        if (direction != null && direction.trim().equalsIgnoreCase("DESC")) dir = "DESC";

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
                    Empresa empresa = new Empresa(
                            rset.getInt("id"),
                            rset.getString("nome"),
                            rset.getString("cep"),
                            rset.getString("cnpj"),
                            rset.getString("email"),
                            rset.getString("telefone"),
                            rset.getString("porte"),
                            rset.getTime("horario_abertura").toLocalTime(),
                            rset.getTime("horario_encerramento").toLocalTime(),
                            rset.getString("regradenegocio"),
                            rset.getInt("fk_plano_id")
                    );
                    empresas.add(empresa);
                }
            }
        }
        // SQLException é propagada
        return empresas;
    }

    /*
     * Busca uma Empresa específica pelo seu ID.
     */
    public Empresa read(int id) throws SQLException {
        Conexao conexao = new Conexao();
        Empresa empresa = null;
        String readSQL = "SELECT * FROM empresa WHERE id = ?";

        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(readSQL)) {

            pstmt.setInt(1, id);

            try (ResultSet rset = pstmt.executeQuery()) {
                if (rset.next()) {
                    empresa = new Empresa(
                            rset.getInt("id"),
                            rset.getString("nome"),
                            rset.getString("cep"),
                            rset.getString("cnpj"),
                            rset.getString("email"),
                            rset.getString("telefone"),
                            rset.getString("porte"),
                            rset.getTime("horario_abertura").toLocalTime(),
                            rset.getTime("horario_encerramento").toLocalTime(),
                            rset.getString("regradenegocio"),
                            rset.getInt("fk_plano_id")
                    );
                }
            }
        }
        // SQLException é propagada
        return empresa;
    }

    /*
     * Atualiza os dados de uma empresa existente, baseado em um objeto.
     */
    public int update(Empresa empresa) throws SQLException {
        Conexao conexao = new Conexao();
        String updateSQL = "UPDATE empresa SET nome = ?, cep = ?, cnpj = ?, email = ?, telefone = ?, porte = ?, " +
                "horario_abertura = ?, horario_encerramento = ?, regradenegocio = ?, fk_plano_id = ? " +
                "WHERE id = ?";

        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {

            pstmt.setString(1, empresa.getNome());
            pstmt.setString(2, empresa.getCep());
            pstmt.setString(3, empresa.getCnpj());
            pstmt.setString(4, empresa.getEmail());
            pstmt.setString(5, empresa.getTelefone());
            pstmt.setString(6, empresa.getPorte());
            pstmt.setTime(7, Time.valueOf(empresa.getHorarioAbertura()));
            pstmt.setTime(8, Time.valueOf(empresa.getHorarioFechamento()));
            pstmt.setString(9, empresa.getRegraDeNegocios());
            pstmt.setInt(10, empresa.getIdPlano());
            pstmt.setInt(11, empresa.getId());

            return pstmt.executeUpdate();
        }
        // SQLException é propagada
    }

    /*
     * Atualiza os dados de uma empresa existente, baseado nos parâmetros.
     */
    public int update(int id, String nome, String cep, String cnpj, String email, String telefone, String porte, LocalTime abertura, LocalTime fechamento, String regra, int idPlano) throws SQLException {
        Conexao conexao = new Conexao();
        String updateSQL = "UPDATE empresa SET nome = ?, cep = ?, cnpj = ?, email = ?, telefone = ?, porte = ?, " +
                "horario_abertura = ?, horario_encerramento = ?, regradenegocio = ?, fk_plano_id = ? " +
                "WHERE id = ?";

        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {

            pstmt.setString(1, nome);
            pstmt.setString(2, cep);
            pstmt.setString(3, cnpj);
            pstmt.setString(4, email);
            pstmt.setString(5, telefone);
            pstmt.setString(6, porte);
            pstmt.setTime(7, Time.valueOf(abertura));
            pstmt.setTime(8, Time.valueOf(fechamento));
            pstmt.setString(9, regra);
            pstmt.setInt(10, idPlano);
            pstmt.setInt(11, id);

            return pstmt.executeUpdate();
        }
        // SQLException é propagada
    }

    /*
     * Exclui uma empresa do banco de dados pelo ID.
     */
    public int delete(int id) throws SQLException {
        Conexao conexao = new Conexao();
        String deleteSQL = "DELETE FROM empresa WHERE id = ?";

        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate();
        }
        // SQLException é propagada
    }

    /*
     * Exclui uma empresa do banco de dados pelo nome.
     */
    public int delete(String nome) throws SQLException {
        Conexao conexao = new Conexao();
        String deleteSQL = "DELETE FROM empresa WHERE nome = ?";

        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {

            pstmt.setString(1, nome);
            return pstmt.executeUpdate();
        }
        // SQLException é propagada
    }
}