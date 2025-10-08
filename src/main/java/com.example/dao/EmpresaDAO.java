package com.example.dao;

import com.example.Controller.*;
import com.example.Model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class
EmpresaDAO {
//    Create
public boolean create(Empresa empresa) {
    Conexao conexao = new Conexao();
    Connection conn = null;
    PreparedStatement pstmt = null;
    String create = "INSERT INTO empresa (id,nome, cep, cnpj, email, telefone_fixo, telefone_pessoal, porte, horario_abertura, horario_encerramento, regradenegocio) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
    try {
        conn = conexao.conectar();
        pstmt = conn.prepareStatement(create);
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

    // READ ALL
    public List<Empresa> read() {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String read = "SELECT * FROM empresa";
        List<Empresa> listaEmpresa = new LinkedList<>();

        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(read);
            rset = pstmt.executeQuery();

            while (rset.next()) {
                Empresa empresa = new Empresa(
                        rset.getInt("id"),
                        rset.getString("nome"),
                        rset.getString("cep"),
                        rset.getString("cnpj"),
                        rset.getString("email"),
                        rset.getString("telefone_fixo"),
                        rset.getString("telefone_pessoal"),
                        rset.getString("porte"),
                        rset.getTime("horario_abertura").toLocalTime(),
                        rset.getTime("horario_encerramento").toLocalTime(),
                        rset.getString("regradenegocio")
                );
                listaEmpresa.add(empresa);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inserir departamento: " + e.getMessage());
            return null;
        } finally {
            try {
                if (rset != null) rset.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao inserir departamento: " + e.getMessage());
            }
        }

        return listaEmpresa;
    }
    // READ BY ID
    public Empresa read(int id) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String readId = "SELECT * FROM empresa WHERE id = ?";

        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(readId);
            pstmt.setInt(1, id);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                return new Empresa(
                        rset.getInt("id"),
                        rset.getString("nome"),
                        rset.getString("cep"),
                        rset.getString("cnpj"),
                        rset.getString("email"),
                        rset.getString("telefone_fixo"),
                        rset.getString("telefone_pessoal"),
                        rset.getString("porte"),
                        rset.getTime("horario_abertura").toLocalTime(),
                        rset.getTime("horario_encerramento").toLocalTime(),
                        rset.getString("regradenegocio")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inserir departamento: " + e.getMessage());
        } finally {
            try {
                if (rset != null) rset.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao inserir departamento: " + e.getMessage());
            }
        }
        return null;
    }
}