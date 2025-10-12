package com.example.dao;

import com.example.Controller.Conexao;
import com.example.Model.Setor;
import com.example.Model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UsuarioDAO {
    public boolean create(Usuario usuario) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        String create = "INSERT INTO  usuario (id, nome, cpf, genero, status, senha, fk_setor_id, fk_supervisor_id, cargo) VALUES (?,?,?,?,?,?,?,?,?)";
        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(create);
            pstmt.setInt(1, usuario.getId());
            pstmt.setString(2, usuario.getNome());
            pstmt.setString(3, usuario.getCpf());
            pstmt.setString(4, String.valueOf(usuario.getGenero()));
            pstmt.setString(5, usuario.getStatus());
            pstmt.setString(6, usuario.getSenha());
            pstmt.setInt(7, usuario.getIdSetor());
            pstmt.setInt(8, usuario.getIdSupervisor());
            pstmt.setString(9, usuario.getCargo);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir usuário: " + e.getMessage());
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
