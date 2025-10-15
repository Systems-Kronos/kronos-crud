package com.example.dao;

import com.example.Controller.Conexao;
import com.example.Model.Empresa;
import com.example.Model.Plano;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class PlanoDAO {
     //    Create
        public boolean create(Plano plano) {
            Conexao conexao = new Conexao();
            Connection conn = null;
            PreparedStatement pstmt = null;
            String create = "INSERT INTO  planos (nomeplano, custo, descricao, qnt_max_funcionario) VALUES (?,?,?,?)";
            try {
                conn = conexao.conectar();
                pstmt = conn.prepareStatement(create);
                pstmt.setString(1, plano.getNome());
                pstmt.setFloat(2, plano.getCusto());
                pstmt.setString(3, plano.getDescricao());
                pstmt.setInt(4, plano.getMaxFuncionarios());

                return pstmt.executeUpdate() > 0; // true se inseriu
            } catch (SQLException e) {
                System.err.println("Erro ao inserir plano: " + e.getMessage());
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

//        READ ALL
        public List<Plano> read() {
            Conexao conexao = new Conexao();
            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet rset = null;
            String read = "SELECT * FROM planos";
            List<Plano> listaPlano = new LinkedList<>();

            try {
                conn = conexao.conectar();
                pstmt = conn.prepareStatement(read);
                rset = pstmt.executeQuery();

                while (rset.next()) {
                    Plano plano = new Plano(rset.getInt("id"),
                            rset.getString("nomeplano"),
                            rset.getFloat("custo"),
                            rset.getString("descricao"),
                            rset.getInt("qnt_max_funcionario"));
                    listaPlano.add(plano);
                }
            } catch (SQLException e) {
                System.err.println("Erro ao buscar planos: " + e.getMessage());
                return null;
            } finally {
                try {
                    if (rset != null) rset.close();
                    if (pstmt != null) pstmt.close();
                    if (conn != null) conn.close();
                } catch (SQLException e) {
                    System.err.println("Erro ao fechar recursos ao buscar planos: " + e.getMessage());
                }
            }

            return listaPlano;
            }

//            READ BY ID
    public Plano read(int id) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String readId = "SELECT * FROM planos WHERE id = ?";
        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(readId);
            pstmt.setInt(1, id);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                return new Plano(rset.getInt("id"),
                        rset.getString("nomeplano"),
                        rset.getFloat("custo"),
                        rset.getString("descricao"),
                        rset.getInt("qnt_max_funcionario"));
            }
    }catch (SQLException e) {
            System.err.println("Erro ao buscar planos por ID: " + e.getMessage());
        } finally {
            try {
                if (rset != null) rset.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos ao buscar planos por ID: " + e.getMessage());
            }
        }
        return null;
    }

//    Update pelo objeto
    public int update(Plano plano) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        String update = "UPDATE planos SET nomeplano = ?, custo = ?, descricao = ?, qnt_max_funcionario = ? WHERE id = ?";
        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(update);

            pstmt.setString(1, plano.getNome());
            pstmt.setFloat(2, plano.getCusto());
            pstmt.setString(3, plano.getDescricao());
            pstmt.setInt(4, plano.getMaxFuncionarios());
            pstmt.setInt(5, plano.getId());

            if (pstmt.executeUpdate() > 0){
                return 1;
            }
            return 0;
        }
        catch (SQLException e) {
            System.err.println("Erro ao atualizar planos: " + e.getMessage());
            return -1;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão após atualizar planos: " + e.getMessage());
            }
        }

    }

//    Update por parametro
    public int update(String nomeplano, float custo, String descricao, int qnt_max_funcionario, int id) {
    Conexao conexao = new Conexao();
    Connection conn = null;
    PreparedStatement pstmt = null;
    String update = "UPDATE planos SET nomeplano = ?, custo = ?, descricao = ?, qnt_max_funcionario = ? WHERE id = ?";
    try {
        conn = conexao.conectar();
        pstmt = conn.prepareStatement(update);

        pstmt.setString(1, nomeplano);
        pstmt.setFloat(2, custo);
        pstmt.setString(3, descricao);
        pstmt.setInt(4, qnt_max_funcionario);
        pstmt.setInt(5, id);

        if (pstmt.executeUpdate() > 0){
            return 1;
        }
        return 0;
    }
    catch (SQLException e) {
        System.err.println("Erro ao atualizar planos: " + e.getMessage());
        return -1;
    } finally {
        try {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Erro ao fechar conexão após atualizar planos: " + e.getMessage());
        }
    }
}

// Delete by Id
    public int delete(int id) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        String delete = "DELETE FROM planos WHERE id = ?";
        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(delete);
            pstmt.setInt(1, id);

            if (pstmt.executeUpdate() > 0){
                return 1;
            }
            return 0;
        }catch (SQLException e) {
            System.err.println("Erro ao deletar planos: " + e.getMessage());
            return -1;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão após deletar planos: " + e.getMessage());
            }
        }
}
//  DELETE By Nome
    public int delete(String nomeplano) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        String delete = "DELETE FROM planos WHERE nomeplano = ?";
        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(delete);
            pstmt.setString(1, nomeplano);

            if (pstmt.executeUpdate() > 0){
                return 1;
            }
            return 0;
        }catch (SQLException e) {
            System.err.println("Erro ao deletar plano: " + e.getMessage());
            return -1;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão após deletar plano: " + e.getMessage());
            }
        }}
}

