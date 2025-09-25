//package com.example.Model;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import com.example.Controller.*;
//
//public class HabilidadesDAO {
//    public boolean inserir(Habilidades habilidade) {
//        Conexao conexao = new Conexao();
//        Connection conn = null;
//        PreparedStatement pstmt = null;
//        String inserir = "INSERT INTO habilidade (id,nome, tags, senha, codigo_acesso) VALUES (?,?,?,?,?)";
//        try {
//            // Tem que ver a lista com o Breno
//            conn = conexao.conectar();
//            pstmt = conn.prepareStatement(inserir);
//            pstmt.setInt(1, administracao.getId());
//            pstmt.setString(2, administracao.getNome());
//            pstmt.setString(3, administracao.getEmail());
//            pstmt.setString(4, administracao.getSenha());
//            pstmt.setString(5, administracao.getCodigoAcesso());
//
//            return pstmt.executeUpdate() > 0; // true se inseriu
//        } catch (SQLException e) {
//            System.err.println("Erro ao inserir departamento: " + e.getMessage());
//            return false;
//        }finally {
//            if (pstmt != null) {
//                try {
//                    pstmt.close();
//                } catch (SQLException e) {
//                    System.out.println("Erro ao fechar PreparedStatement");
//                }
//            }
//            if (conn != null) {
//                try {
//                    conn.close();
//                } catch (SQLException e) {
//                    System.out.println("Erro ao fechar Connection");
//                }
//            }
//
//        }
//    }
//}
