package com.example.Model;

import com.example.Controller.*;
import com.example.Model.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalTime;

public class EmpresaDAO {
//    Inserir
public boolean inserir(Empresa empresa) {
    Conexao conexao = new Conexao();
    Connection conn = null;
    PreparedStatement pstmt = null;
    String inserir = "INSERT INTO empresa (id,nome, cep, cnpj, email, telefone_fixo, telefone_pessoal, porte, horario_abertura, horario_encerramento, regradenegocio) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
    try {
        conn = conexao.conectar();
        pstmt = conn.prepareStatement(inserir);
        pstmt.setInt(1,empresa.getId());
        pstmt.setString(2, empresa.getNome());
        pstmt.setString(3, empresa.getCep());
        pstmt.setString(4, empresa.getCnpj());
        pstmt.setString(5, empresa.getEmail());
        pstmt.setString(6, empresa.getTelefoneFixo());
        pstmt.setString(7, empresa.getTelefonePessoal());
        pstmt.setString(8, empresa.getPorte());
        pstmt.setObject(9, empresa.getHorarioAbertura());
        pstmt.setObject(10, empresa.getHorarioFechamento());
        pstmt.setString(11, empresa.getRegraDeNegocios());

        return pstmt.executeUpdate() > 0;
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
}
