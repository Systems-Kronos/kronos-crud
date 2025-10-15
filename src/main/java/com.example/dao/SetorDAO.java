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
}
