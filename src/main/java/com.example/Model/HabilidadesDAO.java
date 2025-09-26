//package com.example.Model;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import com.example.Controller.*;
//
//public class HabilidadesDAO {git
//    public boolean inserir(Habilidades habilidade) {
//        Conexao conexao = new Conexao();
//        Connection conn = null;
//        PreparedStatement pstmt = null;
//        String inserir = "INSERT INTO habilidade (id,nome, tag, descricao) VALUES (?,?,?,?,?)";
//        try {
//            // Tem que ver a lista com o Breno
//            conn = conexao.conectar();
//            pstmt = conn.prepareStatement(inserir);
//            pstmt.setInt(1, habilidade.getId());
//            pstmt.setString(2, habilidade.getNome());
//            pstmt.setObject(3, habilidade.getTags());
//            pstmt.setString(5, habilidade.getDescricao());
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
