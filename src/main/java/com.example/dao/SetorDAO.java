package com.example.dao;

import com.example.Controller.Conexao;
import com.example.Model.Empresa;
import com.example.Model.Setor;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class SetorDAO {

    // CREATE
    public boolean create(Setor setor) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        String create = "INSERT INTO setor (nome, descricao, turnos, qnt_funcionarios, fk_empresa_id) VALUES (?, ?, ?, ?, ?)";

        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(create);
            pstmt.setString(1, setor.getNome());
            pstmt.setString(2, setor.getDescricao());
            pstmt.setString(3, setor.getTurnos());
            pstmt.setInt(4, setor.getQntFuncionarios());
            pstmt.setInt(5, setor.getEmpresa().getId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao inserir setor: " + e.getMessage());
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
    public List<Setor> read() {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        List<Setor> setores = new LinkedList<>();

        String read = """
            SELECT s.id, s.nome, s.descricao, s.turnos, s.qnt_funcionarios,
                   e.id AS empresa_id, e.nome AS empresa_nome
            FROM setor s
            LEFT JOIN empresa e ON s.fk_empresa_id = e.id
            ORDER BY s.id;
        """;

        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(read);
            rset = pstmt.executeQuery();

            while (rset.next()) {
                Empresa empresa = null;
                int empresaId = rset.getInt("empresa_id");
                if (empresaId > 0) {
                    empresa = new Empresa(
                            empresaId,
                            rset.getString("empresa_nome"),
                            null, null, null, null, null, null, null, null, null
                    );
                }

                Setor setor = new Setor(
                        rset.getInt("id"),
                        rset.getString("nome"),
                        rset.getString("descricao"),
                        rset.getString("turnos"),
                        rset.getInt("qnt_funcionarios"),
                        empresa
                );

                setores.add(setor);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar setores: " + e.getMessage());
        } finally {
            try {
                if (rset != null) rset.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }

        return setores;
    }

    // READ com filtro (nome)
    public List<Setor> read(String nome, String orderBy, String direction) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        List<Setor> setores = new LinkedList<>();

        StringBuilder sql = new StringBuilder("""
            SELECT s.id, s.nome, s.descricao, s.turnos, s.qnt_funcionarios,
                   e.id AS empresa_id, e.nome AS empresa_nome
            FROM setor s
            LEFT JOIN empresa e ON s.fk_empresa_id = e.id
            WHERE 1=1
        """);

        if (nome != null && !nome.isEmpty()) {
            sql.append(" AND s.nome ILIKE '%").append(nome).append("%'");
        }

        String colunaOrdenacao = "s.id";
        if (orderBy != null) {
            if (orderBy.equalsIgnoreCase("nome")) colunaOrdenacao = "s.nome";
            else if (orderBy.equalsIgnoreCase("qnt_funcionarios")) colunaOrdenacao = "s.qnt_funcionarios";
        }

        String dir = "ASC";
        if (direction != null && direction.equalsIgnoreCase("DESC")) dir = "DESC";

        sql.append(" ORDER BY ").append(colunaOrdenacao).append(" ").append(dir);

        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(sql.toString());
            rset = pstmt.executeQuery();

            while (rset.next()) {
                Empresa empresa = null;
                int empresaId = rset.getInt("empresa_id");
                if (empresaId > 0) {
                    empresa = new Empresa(
                            empresaId,
                            rset.getString("empresa_nome"),
                            null, null, null, null, null, null, null, null, null
                    );
                }

                Setor setor = new Setor(
                        rset.getInt("id"),
                        rset.getString("nome"),
                        rset.getString("descricao"),
                        rset.getString("turnos"),
                        rset.getInt("qnt_funcionarios"),
                        empresa
                );

                setores.add(setor);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar setores por nome: " + e.getMessage());
        } finally {
            try {
                if (rset != null) rset.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }

        return setores;
    }

    // READ by ID
    public Setor read(int id) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        Setor setor = null;

        String read = """
            SELECT s.id, s.nome, s.descricao, s.turnos, s.qnt_funcionarios,
                   e.id AS empresa_id, e.nome AS empresa_nome
            FROM setor s
            LEFT JOIN empresa e ON s.fk_empresa_id = e.id
            WHERE s.id = ?
        """;

        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(read);
            pstmt.setInt(1, id);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                Empresa empresa = null;
                int empresaId = rset.getInt("empresa_id");
                if (empresaId > 0) {
                    empresa = new Empresa(
                            empresaId,
                            rset.getString("empresa_nome"),
                            null, null, null, null, null, null, null, null, null
                    );
                }

                setor = new Setor(
                        rset.getInt("id"),
                        rset.getString("nome"),
                        rset.getString("descricao"),
                        rset.getString("turnos"),
                        rset.getInt("qnt_funcionarios"),
                        empresa
                );
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar setor por ID: " + e.getMessage());
        } finally {
            try {
                if (rset != null) rset.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }

        return setor;
    }

    // UPDATE por objeto
    public boolean update(Setor setor) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        String update = """
            UPDATE setor
            SET nome = ?, descricao = ?, turnos = ?, qnt_funcionarios = ?, fk_empresa_id = ?
            WHERE id = ?
        """;

        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(update);
            pstmt.setString(1, setor.getNome());
            pstmt.setString(2, setor.getDescricao());
            pstmt.setString(3, setor.getTurnos());
            pstmt.setInt(4, setor.getQntFuncionarios());
            pstmt.setInt(5, setor.getEmpresa().getId());
            pstmt.setInt(6, setor.getId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar setor: " + e.getMessage());
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

    // UPDATE por parâmetros
    public boolean update(int id, String nome, String descricao, String turnos, int qntFunc, Empresa empresa) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        String update = """
            UPDATE setor
            SET nome = ?, descricao = ?, turnos = ?, qnt_funcionarios = ?, fk_empresa_id = ?
            WHERE id = ?
        """;

        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(update);
            pstmt.setString(1, nome);
            pstmt.setString(2, descricao);
            pstmt.setString(3, turnos);
            pstmt.setInt(4, qntFunc);
            pstmt.setInt(5, empresa != null ? empresa.getId() : Types.INTEGER);
            pstmt.setInt(6, id);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar setor por parâmetros: " + e.getMessage());
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

    // DELETE por ID
    public boolean delete(int id) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        String delete = "DELETE FROM setor WHERE id = ?";

        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(delete);
            pstmt.setInt(1, id);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao deletar setor: " + e.getMessage());
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

    // DELETE por nome
    public boolean delete(String nome) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        String delete = "DELETE FROM setor WHERE nome = ?";

        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(delete);
            pstmt.setString(1, nome);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao deletar setor por nome: " + e.getMessage());
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

    // ADICIONAR EMPRESA AO SETOR
    public boolean addEmpresaToSetor(int idSetor, int idEmpresa) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        String addEmpresa = "UPDATE setor SET fk_empresa_id = ? WHERE id = ?";

        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(addEmpresa);
            pstmt.setInt(1, idEmpresa);
            pstmt.setInt(2, idSetor);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar empresa ao setor: " + e.getMessage());
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

    // REMOVER EMPRESA DO SETOR
    public boolean removeEmpresaFromSetor(int idSetor) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        String removeEmpresa = "UPDATE setor SET fk_empresa_id = NULL WHERE id = ?";

        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(removeEmpresa);
            pstmt.setInt(1, idSetor);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao remover empresa do setor: " + e.getMessage());
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
}
