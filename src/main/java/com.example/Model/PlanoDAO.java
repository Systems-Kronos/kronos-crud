package com.example.Model;

import com.example.Controller.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PlanoDAO {
     //    Inserir
        public boolean inserir(Plano plano) {
            Conexao conexao = new Conexao();
            Connection conn = null;
            PreparedStatement pstmt = null;
            String inserir = "INSERT INTO  planos (id,nomeplano, custo, descricao, qnt_max_funcionario) VALUES (?,?,?,?,?)";
            try {
                conn = conexao.conectar();
                pstmt = conn.prepareStatement(inserir);
                pstmt.setInt(1, plano.getId());
                pstmt.setString(2, plano.getNome());
                pstmt.setFloat(3, plano.getCusto());
                pstmt.setString(4, plano.getDescricao());
                pstmt.setInt(5, plano.getMaxFuncionarios());

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

