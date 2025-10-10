package com.example.dao;
import com.example.Controller.*;
import com.example.Model.Administracao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdministracaoDAO {


    // Ler todos os registros
    public List<Administracao> read() {
        List<Administracao> lista = new ArrayList<>();
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT id, nome, email, senha, codigo_acesso FROM administracao";

        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Administracao admin = new Administracao(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("senha"),
                        rs.getString("codigo_acesso")
                );
                lista.add(admin);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao ler dados da tabela administracao: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos: " + e.getMessage());
            }
        }

        return lista;
    }

    //    Inserir
    public boolean inserir(Administracao administracao) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        String inserir = "INSERT INTO administracao (nome, email, senha, codigo_acesso) VALUES (?,?,?,?)";
        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(inserir);
            pstmt.setString(1, administracao.getNome());
            pstmt.setString(2, administracao.getEmail());
            pstmt.setString(3, administracao.getSenha());
            pstmt.setString(4, administracao.getCodigoAcesso());

            return pstmt.executeUpdate() > 0; // true se inseriu
        } catch (SQLException e) {
            System.err.println("Erro ao inserir departamento: " + e.getMessage());
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
    //Remover
    public boolean remover(int id){
        Conexao conexao = new Conexao();
        Connection conn = conexao.conectar();
        String remover = "DELETE FROM administracao WHERE id = ?";
        //Instanciando o objeto preparedStatement(pstmt)
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(remover);
            pstmt.setInt(1, id);
            pstmt.execute();
            conexao.desconectar(conn);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    //    Alterar
    public boolean alterarNome(int id, String novoNome) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        String alterarNome = "UPDATE administracao SET nome = ? WHERE id =? ";
        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(alterarNome);
            pstmt.setString(1, novoNome);
            pstmt.setInt(2, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao alterar: " + e.getMessage());
            e.printStackTrace(); // mostra o stack completo
            return false;
        } finally {
            finalizar(pstmt, conn);
        }}
    //    Outros
    public static void finalizar(Statement pstmt, Connection conn) {
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
        }}}
