package com.example.dao;

import com.example.Controller.Conexao;
import com.example.Model.Empresa;
import com.example.Model.Setor;
import com.example.Model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class UsuarioDAO {
    public boolean create(Usuario usuario) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        String create = "INSERT INTO  usuario (nome, cpf, genero, status, senha, fk_setor_id, fk_supervisor_id, cargo) VALUES (?,?,?,?,?,?,?,?)";
        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(create);
            pstmt.setString(1, usuario.getNome());
            pstmt.setString(2, usuario.getCpf());
            pstmt.setString(3, String.valueOf(usuario.getGenero()));
            pstmt.setString(4, usuario.getStatus());
            pstmt.setString(5, usuario.getSenha());
            pstmt.setInt(6, usuario.getIdSetor());
            pstmt.setInt(7, usuario.getIdSupervisor());
            pstmt.setString(8, usuario.getCargo());

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

//    READ ALL
    public List<Usuario> read() {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String read = "SELECT * FROM usuario";
        List<Usuario> listaUsuario = new LinkedList<>();

        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(read);
            rset = pstmt.executeQuery();

            while (rset.next()) {
                Usuario usuario = new Usuario(rset.getInt("id"),
                        rset.getString("nome"),
                        rset.getString("cpf"),
                        rset.getString("genero").charAt(0),
                        rset.getString("status"),
                        rset.getString("senha"),
                        rset.getInt("fk_setor_id"),
                        rset.getInt("fk_supervisor_id"),
                        rset.getString("cargo"));
                listaUsuario.add(usuario);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuario: " + e.getMessage());
            return null;
        } finally {
            try {
                if (rset != null) rset.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos ao buscar usuario: " + e.getMessage());
            }
        }

        return listaUsuario;
    }

//    READ by id
    public Usuario read(int id) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String readId = "SELECT * FROM usuario WHERE id = ?";

        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(readId);
            pstmt.setInt(1, id);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                return new Usuario(rset.getInt("id"),
                        rset.getString("nome"),
                        rset.getString("cpf"),
                        rset.getString("genero").charAt(0),
                        rset.getString("status"),
                        rset.getString("senha"),
                        rset.getInt("fk_setor_id"),
                        rset.getInt("fk_supervisor_id"),
                        rset.getString("cargo"));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuario por ID: " + e.getMessage());
        } finally {
            try {
                if (rset != null) rset.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos ao buscar usuario por ID: " + e.getMessage());
            }
        }
        return null;
    }

//    UPDATE objeto
    public int update(Usuario usuario) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        String update = "UPDATE usuario SET nome = ?, cpf = ?, genero = ?, status = ?, senha = ?, fk_setor_id = ?, fk_supervisor_id = ?, cargo = ? WHERE id = ?";
        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(update);

            pstmt.setString(1, usuario.getNome());
            pstmt.setString(2, usuario.getCpf());
            pstmt.setString(3, usuario.getGenero().toString());
            pstmt.setString(4, usuario.getStatus());
            pstmt.setString(5, usuario.getSenha());
            pstmt.setInt(6, usuario.getIdSetor());
            pstmt.setInt(7, usuario.getIdSupervisor());
            pstmt.setString(8, usuario.getCargo());
            pstmt.setInt(9, usuario.getId());
            if (pstmt.executeUpdate() > 0){
                return 1;
            }
            return 0;
        }
        catch (SQLException e) {
            System.err.println("Erro ao atualizar usuario: " + e.getMessage());
            return -1;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão após atualizar usuario: " + e.getMessage());
            }
        }
    }

//    UPDATE todos os parametros
    public int update(String nome, String cpf, char genero, String status, String senha, int idSetor, int idSupervisor, String cargo, int id){
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        String update = "UPDATE usuario SET nome = ?, cpf = ?, genero = ?, status = ?, senha = ?, fk_setor_id = ?, fk_supervisor_id = ?, cargo = ? WHERE id = ?";
        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(update);

            pstmt.setString(1, nome);
            pstmt.setString(2, cpf);
            pstmt.setString(3, String.valueOf(genero));
            pstmt.setString(4, status);
            pstmt.setString(5, senha);
            pstmt.setInt(6, idSetor);
            pstmt.setInt(7, idSupervisor);
            pstmt.setString(8, cargo);
            pstmt.setInt(9, id);
            if (pstmt.executeUpdate() > 0){
                return 1;
            }
            return 0;
        }
        catch (SQLException e) {
            System.err.println("Erro ao atualizar usuario: " + e.getMessage());
            return -1;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão após atualizar usuario: " + e.getMessage());
            }
        }
    }
}
