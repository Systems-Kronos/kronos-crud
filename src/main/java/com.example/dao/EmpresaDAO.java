package com.example.dao;

import com.example.Controller.*;
import com.example.Model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

public class
EmpresaDAO {
//    Create
public boolean create(Empresa empresa) {
    Conexao conexao = new Conexao();
    Connection conn = null;
    PreparedStatement pstmt = null;
    String create = "INSERT INTO empresa (id,nome, cep, cnpj, email, telefone, porte, horario_abertura, horario_encerramento, regradenegocio) VALUES (?,?,?,?,?,?,?,?,?,?)";
    try {
        conn = conexao.conectar();
        pstmt = conn.prepareStatement(create);
        pstmt.setInt(1,empresa.getId());
        pstmt.setString(2, empresa.getNome());
        pstmt.setString(3, empresa.getCep());
        pstmt.setString(4, empresa.getCnpj());
        pstmt.setString(5, empresa.getEmail());
        pstmt.setString(6, empresa.getTelefone());
        pstmt.setString(7, empresa.getPorte());
        pstmt.setObject(8, empresa.getHorarioAbertura());
        pstmt.setObject(9, empresa.getHorarioFechamento());
        pstmt.setString(10, empresa.getRegraDeNegocios());

        return pstmt.executeUpdate() > 0;
    } catch (SQLException e) {
        System.err.println("Erro ao inserir empresa: " + e.getMessage());
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
                        rset.getString("telefone"),
                        rset.getString("porte"),
                        rset.getTime("horario_abertura").toLocalTime(),
                        rset.getTime("horario_encerramento").toLocalTime(),
                        rset.getString("regradenegocio")
                );
                listaEmpresa.add(empresa);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar empresas: " + e.getMessage());
            return null;
        } finally {
            try {
                if (rset != null) rset.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos ao buscar empresas: " + e.getMessage());
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
                        rset.getString("telefone"),
                        rset.getString("porte"),
                        rset.getTime("horario_abertura").toLocalTime(),
                        rset.getTime("horario_encerramento").toLocalTime(),
                        rset.getString("regradenegocio")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar empresa por ID: " + e.getMessage());
        } finally {
            try {
                if (rset != null) rset.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos ao buscar empresa por ID: " + e.getMessage());
            }
        }
        return null;
    }

//  UPDATE da empresa pelo objeto Empresa
    public int update(Empresa empresa) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        String update = "UPDATE empresa SET nome = ?, cep = ?, cnpj = ?, email = ?, telefone = ?, porte = ?, horario_abertura = ?, horario_encerramento = ?, regradenegocio = ? WHERE id = ?";
        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(update);

            pstmt.setString(1, empresa.getNome());
            pstmt.setString(2, empresa.getCep());
            pstmt.setString(3,empresa.getCnpj());
            pstmt.setString(4, empresa.getEmail());
            pstmt.setString(5, empresa.getTelefone());
            pstmt.setString(6, empresa.getPorte());
            pstmt.setObject(7, empresa.getHorarioAbertura());
            pstmt.setObject(8, empresa.getHorarioFechamento());
            pstmt.setString(9, empresa.getRegraDeNegocios());
            pstmt.setInt(10, empresa.getId());

            if (pstmt.executeUpdate() > 0){
                return 1;
            }
            return 0;
        }
        catch (SQLException e) {
            System.err.println("Erro ao atualizar empresa: " + e.getMessage());
            return -1;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão após atualizar empresa: " + e.getMessage());
            }
        }
    }
//  UPDATE da empresa por todos os parametros
    public int update(int id, String nome, String cep, String cnpj, String email, String telefone,
                      String porte, LocalTime horarioAbertura,
                      LocalTime horarioFechamento, String regraDeNegocios) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        String update = "UPDATE empresa SET nome = ?, cep = ?, cnpj = ?, email = ?, telefone = ?, porte = ?, horario_abertura = ?, horario_encerramento = ?, regradenegocio = ? WHERE id = ?";
        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(update);

            pstmt.setString(1, nome);
            pstmt.setString(2, cep);
            pstmt.setString(3, cnpj);
            pstmt.setString(4, email);
            pstmt.setString(5, telefone);
            pstmt.setString(6, porte);
            pstmt.setObject(7, horarioAbertura);
            pstmt.setObject(8, horarioFechamento);
            pstmt.setString(9, regraDeNegocios);
            pstmt.setInt(10, id);

            if (pstmt.executeUpdate() > 0){
                return 1;
            }
            return 0;
        }
        catch (SQLException e) {
            System.err.println("Erro ao atualizar empresa: " + e.getMessage());
            return -1;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão após atualizar empresa: " + e.getMessage());
            }
        }
    }

//  DELETE por id
    public int delete(int id) {
    Conexao conexao = new Conexao();
    Connection conn = null;
    PreparedStatement pstmt = null;
    String delete = "DELETE FROM empresa WHERE id = ?";
    try {
        conn = conexao.conectar();
        pstmt = conn.prepareStatement(delete);
        pstmt.setInt(1, id);

        if (pstmt.executeUpdate() > 0){
            return 1;
        }
        return 0;
    }catch (SQLException e) {
        System.err.println("Erro ao deletar empresa: " + e.getMessage());
        return -1;
    } finally {
        try {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Erro ao fechar conexão após deletar empresa: " + e.getMessage());
        }
    }}

//  DELETE por cnpj
    public int delete(String cnpj) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        String delete = "DELETE FROM empresa WHERE cnpj = ?";
        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(delete);
            pstmt.setString(1, cnpj);

            if (pstmt.executeUpdate() > 0){
                return 1;
            }
            return 0;
        }catch (SQLException e) {
            System.err.println("Erro ao deletar empresa: " + e.getMessage());
            return -1;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão após deletar empresa: " + e.getMessage());
            }
        }}
}