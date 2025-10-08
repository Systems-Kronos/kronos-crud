package com.example.dao;
import com.example.Controller.Conexao;
import com.example.Model.Setor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SetorDAO {
    public boolean inserir(Setor setor) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        String inserir = "INSERT INTO  setor (id,nome, descricao, turnos, qnt_funcionarios, fk_empresa_id) VALUES (?,?,?,?,?,?)";
        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(inserir);
            pstmt.setInt(1, setor.getId());
            pstmt.setString(2, setor.getNome());
            pstmt.setNString(3, setor.getDescricao());
            pstmt.setString(4, setor.getTurnos());
            pstmt.setInt(5, setor.getQntFuncionarios());
            pstmt.setInt(6, setor.getIdEmpresa());

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
