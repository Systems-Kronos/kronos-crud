package com.example.dao;
import com.example.Controller.*;
import com.example.Model.Administracao;
import com.example.Model.Empresa;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class AdministracaoDAO {
//    Create
    public boolean create(Administracao administracao) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        String create = "INSERT INTO administracao (id,nome, email, senha, codigo_acesso) VALUES (?,?,?,?,?)";
        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(create);
            pstmt.setInt(1, administracao.getId());
            pstmt.setString(2, administracao.getNome());
            pstmt.setString(3, administracao.getEmail());
            pstmt.setString(4, administracao.getSenha());
            pstmt.setString(5, administracao.getCodigoAcesso());

            return pstmt.executeUpdate() > 0; // true se inseriu
        } catch (SQLException e) {
            System.err.println("Erro ao inserir admnistracao: " + e.getMessage());
            return false;
        }finally {
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

    public List<Administracao> read() {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String read = "SELECT * FROM administracao";
        List<Administracao> listaAdministracao = new LinkedList<>();

        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(read);
            rset = pstmt.executeQuery();

            while (rset.next()) {
            Administracao administracao = new Administracao(rset.getInt("id"),
                    rset.getString("nome"),
                    rset.getString("email"),
                    rset.getString("senha"),
                    rset.getString("codigo_acesso"));
            listaAdministracao.add(administracao);
            }
        }catch (SQLException e) {
            System.err.println("Erro ao buscar administracao: " + e.getMessage());
            return null;
        } finally {
            try {
                if (rset != null) rset.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos ao buscar administracao: " + e.getMessage());
            }
        }

        return listaAdministracao;
    }

//    READ By Id
    public Administracao read(int id) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String readId = "SELECT * FROM administracao WHERE id = ?";

        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(readId);
            pstmt.setInt(1, id);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                return new Administracao(rset.getInt("id"),
                        rset.getString("nome"),
                        rset.getString("email"),
                        rset.getString("senha"),
                        rset.getString("codigo_acesso"));
            }

    }catch (SQLException e) {
            System.err.println("Erro ao buscar administracao por ID: " + e.getMessage());
        } finally {
            try {
                if (rset != null) rset.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos ao buscar administracao por ID: " + e.getMessage());
            }
        }
        return null;
    }

//    Update pelo objeto
    public int update(Administracao administracao) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        String update = "UPDATE administracao SET nome = ?, email = ?, senha = ?, codigo_acesso = ? WHERE id = ?";
        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(update);

            pstmt.setString(1, administracao.getNome());
            pstmt.setString(2, administracao.getEmail());
            pstmt.setString(3, administracao.getSenha());
            pstmt.setString(4, administracao.getCodigoAcesso());
            pstmt.setInt(5, administracao.getId());
            if (pstmt.executeUpdate() > 0){
                return 1;
            }
            return 0;
        }
        catch (SQLException e) {
            System.err.println("Erro ao atualizar administracao: " + e.getMessage());
            return -1;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão após atualizar administracao: " + e.getMessage());
            }
        }
    }

//    Update com os parametros
public int update(String nome, String email, String senha, String codigoAcesso, int id) {
    Conexao conexao = new Conexao();
    Connection conn = null;
    PreparedStatement pstmt = null;
    String update = "UPDATE administracao SET nome = ?, email = ?, senha = ?, codigo_acesso = ? WHERE id = ?";
    try {
        conn = conexao.conectar();
        pstmt = conn.prepareStatement(update);

        pstmt.setString(1, nome);
        pstmt.setString(2, email);
        pstmt.setString(3, senha);
        pstmt.setString(4, codigoAcesso);
        pstmt.setInt(5, id);
        if (pstmt.executeUpdate() > 0){
            return 1;
        }
        return 0;
    }
    catch (SQLException e) {
        System.err.println("Erro ao atualizar administracao: " + e.getMessage());
        return -1;
    } finally {
        try {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Erro ao fechar conexão após atualizar administracao: " + e.getMessage());
        }
    }
}

//Delete By Id
    public int delete(int id) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        String delete = "DELETE FROM administracao WHERE id = ?";
        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(delete);
            pstmt.setInt(1, id);

            if (pstmt.executeUpdate() > 0){
                return 1;
            }
            return 0;
        }catch (SQLException e) {
            System.err.println("Erro ao deletar adiministracao: " + e.getMessage());
            return -1;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão após deletar adiministracao: " + e.getMessage());
            }
        }
    }
   }
