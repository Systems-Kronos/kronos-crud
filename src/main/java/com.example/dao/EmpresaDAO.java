package com.example.dao;

import com.example.Controller.Conexao;
import com.example.Model.Empresa;
import com.example.Model.Plano;

import java.sql.*;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;


public class EmpresaDAO {

    // CREATE
    public boolean create(Empresa empresa) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        String create = "INSERT INTO empresa (nome, cep, cnpj, email, telefone, porte, horario_abertura, horario_encerramento, regradenegocio, fk_plano_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(create);

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

        } catch (SQLException e) {
            System.err.println("Erro ao inserir empresa: " + e.getMessage());
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }

    // READ ALL
    public List<Empresa> read() {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        List<Empresa> empresas = new LinkedList<>();

        String read = """
            SELECT e.id, e.nome, e.cep, e.cnpj, e.email, e.telefone, e.porte,
                   e.horario_abertura, e.horario_encerramento, e.regradenegocio,
                   e.fk_plano_id
            FROM empresa e
            ORDER BY e.id;
        """;

        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(read);
            rset = pstmt.executeQuery();

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

        } catch (SQLException e) {
            System.err.println("Erro ao buscar empresas: " + e.getMessage());
        } finally {
            try {
                if (rset != null) rset.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }

        return empresas;
    }

    // READ by filter (por nome)
    public List<Empresa> read(String nome, String orderBy, String direction) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        List<Empresa> empresas = new LinkedList<>();

        StringBuilder sql = new StringBuilder("""
            SELECT e.id, e.nome, e.cep, e.cnpj, e.email, e.telefone, e.porte,
                   e.horario_abertura, e.horario_encerramento, e.regradenegocio,
                   e.fk_plano_id
            FROM empresa e
            WHERE 1=1
            """);

        if (nome != null && !nome.isEmpty()) {
            sql.append(" AND e.nome ILIKE '%").append(nome).append("%'");
        }

        String colunaOrdenacao = "e.id";
        if (orderBy != null) {
            if (orderBy.equalsIgnoreCase("nome")) colunaOrdenacao = "e.nome";
            else if (orderBy.equalsIgnoreCase("cep")) colunaOrdenacao = "e.cep";
        }

        String dir = "ASC";
        if (direction != null && direction.equalsIgnoreCase("DESC")) dir = "DESC";

        sql.append(" ORDER BY ").append(colunaOrdenacao).append(" ").append(dir);

        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(sql.toString());
            rset = pstmt.executeQuery();

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

        } catch (SQLException e) {
            System.err.println("Erro ao buscar empresas por nome: " + e.getMessage());
        } finally {
            try {
                if (rset != null) rset.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }

        return empresas;
    }

    // READ by ID
    public Empresa read(int id) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        Empresa empresa = null;

        String read = """
            SELECT e.id, e.nome, e.cep, e.cnpj, e.email, e.telefone, e.porte,
                   e.horario_abertura, e.horario_encerramento, e.regradenegocio,
                   e.fk_plano_id
            FROM empresa e
            WHERE e.id = ?
            """;

        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(read);
            pstmt.setInt(1, id);
            rset = pstmt.executeQuery();

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

        } catch (SQLException e) {
            System.err.println("Erro ao buscar empresa por ID: " + e.getMessage());
        } finally {
            try {
                if (rset != null) rset.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }

        return empresa;
    }

    // UPDATE por objeto
    public int update(Empresa empresa) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        String update = """
            UPDATE empresa
            SET nome = ?, cep = ?, cnpj = ?, email = ?, telefone = ?, porte = ?,
                horario_abertura = ?, horario_encerramento = ?, regradenegocio = ?, fk_plano_id = ?
            WHERE id = ?
            """;

        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(update);

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

            if (pstmt.executeUpdate() > 0) {
                return 1;
            }
            return 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar empresa: " + e.getMessage());
            return -1;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }

    // UPDATE por parâmetros (novo)
    public int update(int id, String nome, String cep, String cnpj, String email, String telefone, String porte, LocalTime abertura, LocalTime fechamento, String regra, int idPlano) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        String update = """
            UPDATE empresa
            SET nome = ?, cep = ?, cnpj = ?, email = ?, telefone = ?, porte = ?,
                horario_abertura = ?, horario_encerramento = ?, regradenegocio = ?, fk_plano_id = ?
            WHERE id = ?
            """;

        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(update);

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

            if (pstmt.executeUpdate() > 0) {
                return 1;
            }
            return 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar empresa por ID: " + e.getMessage());
            return -1;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }

    // DELETE por ID
    public int delete(int id) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        String delete = "DELETE FROM empresa WHERE id = ?";

        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(delete);
            pstmt.setInt(1, id);

            if (pstmt.executeUpdate() > 0) {
                return 1;
            }
            return 0;

        } catch (SQLException e) {
            System.err.println("Erro ao deletar empresa: " + e.getMessage());
            return -1;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }

    // DELETE por nome (novo)
    public int delete(String nome) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        String delete = "DELETE FROM empresa WHERE nome = ?";

        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(delete);
            pstmt.setString(1, nome);

            if (pstmt.executeUpdate() > 0) {
                return 1;
            }
            return 0;

        } catch (SQLException e) {
            System.err.println("Erro ao deletar empresa por nome: " + e.getMessage());
            return -1;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }

    public String joinPlanoEmpresa(int idEmpresa) throws SQLException {

        String nomePlano = null;
        String sql = """
                SELECT p.nomeplano
                FROM planos p
                LEFT JOIN empresa e ON e.fk_plano_id = p.id
                WHERE e.id = ?
            """;

        Conexao conexao = new Conexao();

        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idEmpresa);

            try (ResultSet rset = pstmt.executeQuery()) {
                if (rset.next()) {
                    nomePlano = rset.getString("nomeplano");
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar plano da empresa: " + e.getMessage());
            throw new SQLException("Erro ao consultar o plano da empresa.", e);
        }
        return nomePlano;
    }
}