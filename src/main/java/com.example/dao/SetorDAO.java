package com.example.dao;
import com.example.Controller.Conexao;
import com.example.Model.Empresa;
import com.example.Model.Setor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class SetorDAO {
    public boolean create(Setor setor) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        String create = "INSERT INTO  setor (nome, descricao, turnos, qnt_funcionarios, fk_empresa_id) VALUES (?,?,?,?,?)";
        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(create);
            pstmt.setString(1, setor.getNome());
            pstmt.setNString(2, setor.getDescricao());
            pstmt.setString(3, setor.getTurnos());
            pstmt.setInt(4, setor.getQntFuncionarios());
            pstmt.setInt(5, setor.getIdEmpresa());

            return pstmt.executeUpdate() > 0; // true se inseriu
        } catch (SQLException e) {
            System.err.println("Erro ao inserir setor: " + e.getMessage());
            return false;
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    System.out.println("Erro ao fechar PreparedStatement");
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.out.println("Erro ao fechar Connection");
                }
            }

        }
    }

//    READ ALL
    public List<Setor> read(){
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String read = "SELECT * FROM setor";
        List<Setor> listaSetor = new LinkedList<>();
        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(read);
            rset = pstmt.executeQuery();

            while (rset.next()) {
                Setor setor = new Setor(
                        rset.getInt("id"),
                        rset.getString("nome"),
                        rset.getString("descricao"),
                        rset.getString("turnos"),
                        rset.getInt("qnt_funcionarios"),
                        rset.getInt("fk_empresa_id")
                );
                listaSetor.add(setor);
            }
          }
 catch (SQLException e) {
        System.err.println("Erro ao buscar setores: " + e.getMessage());
        return null;
        } finally {
        try {
        if (rset != null) rset.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
        System.err.println("Erro ao fechar recursos ao buscar setores: " + e.getMessage());
        }
        }

        return listaSetor;
    }

//    READ By Id
    public Setor read(int id) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String readId = "SELECT * FROM setor WHERE id = ?";

        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(readId);
            pstmt.setInt(1, id);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                return new Setor(
                        rset.getInt("id"),
                        rset.getString("nome"),
                        rset.getString("descricao"),
                        rset.getString("turnos"),
                        rset.getInt("qnt_funcionarios"),
                        rset.getInt("fk_empresa_id"));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar setor por ID: " + e.getMessage());
        } finally {
            try {
                if (rset != null) rset.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos ao buscar setor por ID: " + e.getMessage());
            }
        }
        return null;
    }

//    UPDATE com objeto
    public int update(Setor setor) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        String update = "UPDATE setor SET nome = ?, descricao = ?, turnos = ?, qnt_funcionarios = ?, fk_empresa_id = ? WHERE id = ?";
        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(update);

            pstmt.setString(1, setor.getNome());
            pstmt.setString(2, setor.getDescricao());
            pstmt.setString(3, setor.getTurnos());
            pstmt.setInt(4, setor.getQntFuncionarios());
            pstmt.setInt(5, setor.getIdEmpresa());
            pstmt.setInt(6, setor.getId());
            if (pstmt.executeUpdate() > 0){
                return 1;
            }
            return 0;
        }
        catch (SQLException e) {
            System.err.println("Erro ao atualizar setor: " + e.getMessage());
            return -1;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão após atualizar setor: " + e.getMessage());
            }
        }
        }

//        UPDATE com todos os parametros
    public int update(String nome, String descricao, String turnos, int qntFuncionarios, int fkEmpresaId, int id) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        String update = "UPDATE setor SET nome = ?, descricao = ?, turnos = ?, qnt_funcionarios = ?, fk_empresa_id = ? WHERE id = ?";
        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(update);

            pstmt.setString(1, nome);
            pstmt.setString(2, descricao);
            pstmt.setString(3, turnos);
            pstmt.setInt(4, qntFuncionarios);
            pstmt.setInt(5, fkEmpresaId);
            pstmt.setInt(6, id);
            if (pstmt.executeUpdate() > 0){
                return 1;
            }
            return 0;
        }
        catch (SQLException e) {
            System.err.println("Erro ao atualizar setor: " + e.getMessage());
            return -1;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão após atualizar setor: " + e.getMessage());
            }
        }
    }

//    DELETE por id
    public int delete(int id) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        String delete = "DELETE FROM setor WHERE id = ?";
        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(delete);
            pstmt.setInt(1, id);

            if (pstmt.executeUpdate() > 0){
                return 1;
            }
            return 0;
        }catch (SQLException e) {
            System.err.println("Erro ao deletar setor: " + e.getMessage());
            return -1;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão após setor empresa: " + e.getMessage());
            }
        }
    }

//    DELETE by nome
    public int delete(String nome) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        String delete = "DELETE FROM setor WHERE nome = ?";
        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(delete);
            pstmt.setString(1, nome);

            if (pstmt.executeUpdate() > 0){
                return 1;
            }
            return 0;
        }catch (SQLException e) {
            System.err.println("Erro ao deletar setor: " + e.getMessage());
            return -1;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão após deletar setor: " + e.getMessage());
            }
        }
    }
}
