package com.example.Dao;

import com.example.Controller.Conexao;
import com.example.Model.HabilidadeUsuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HabilidadeUsuarioDAO {
    public boolean inserir(HabilidadeUsuario habilidadeUsuario) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        String inserir = "INSERT INTO  setor (fk_usuario_id, fk_habilidade_id) VALUES (?,?)";
        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(inserir);
            pstmt.setInt(1, habilidadeUsuario.getUsuarioId());
            pstmt.setInt(2, habilidadeUsuario.getHabilidadeId());

            return pstmt.executeUpdate() > 0; // true se inseriu
        } catch (SQLException e) {
            System.err.println("Erro ao inserir departamento: " + e.getMessage());
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
}
