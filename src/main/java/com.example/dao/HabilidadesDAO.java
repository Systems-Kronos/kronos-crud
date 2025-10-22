package com.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.example.Controller.*;
import com.example.Model.Empresa;
import com.example.Model.Habilidades;

public class HabilidadesDAO {

//    CREATE
    public boolean create(Habilidades habilidade) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        String create = "INSERT INTO habilidade (nome, tag, descricao) VALUES (?,?,?)";
        try {
            // Tem que ver a lista com o Breno
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(create);
            pstmt.setString(1, habilidade.getNome());
            pstmt.setString(2, habilidade.getTag());
            pstmt.setString(3, habilidade.getDescricao());

            return pstmt.executeUpdate() > 0; // true se inseriu
        } catch (SQLException e) {
            System.err.println("Erro ao inserir habilidade: " + e.getMessage());
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

//    READ ALL
    public List<Habilidades> read() {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String read = "SELECT * FROM habilidade";
        List<Habilidades> listaHabilidade = new LinkedList<>();

        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(read);
            rset = pstmt.executeQuery();
            while (rset.next()) {
                Habilidades habilidade = new Habilidades(rset.getInt("id"),
                        rset.getString("nome"),
                        rset.getString("tag"),
                        rset.getString("descricao")
                );
                listaHabilidade.add(habilidade);
            }
        }
        catch (SQLException e) {
            System.err.println("Erro ao buscar habilidades: " + e.getMessage());
            return null;
        } finally {
            try {
                if (rset != null) rset.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos ao buscar habilidades: " + e.getMessage());
            }
        }

        return listaHabilidade;
    }

//    READ BY ID

    public List<Habilidades> read(String nome, String orderBy, String direction) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        List<Habilidades> listaHabilidades = new LinkedList<>();

        String sql = "SELECT * FROM habilidade";

        // Filtro por nome
        if (nome != null && !nome.isEmpty()) {
            sql += " WHERE nome ILIKE '%" + nome + "%'";
        }

        // Definir coluna de ordenação
        String colunaOrdenacao = "id";
        if (orderBy != null) {
            if (orderBy.equals("nome")) {
                colunaOrdenacao = "nome";
            } else if (orderBy.equals("tag")) {
                colunaOrdenacao = "tag";
            } else if (orderBy.equals("descricao")) {
                colunaOrdenacao = "descricao";
            }
        }

        // Definir direção da ordenação
        String dir = "ASC";
        if (direction != null && direction.equalsIgnoreCase("DESC")) {
            dir = "DESC";
        }

        sql += " ORDER BY " + colunaOrdenacao + " " + dir;

        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(sql);
            rset = pstmt.executeQuery();

            while (rset.next()) {
                Habilidades habilidade = new Habilidades(
                        rset.getInt("id"),
                        rset.getString("nome"),
                        rset.getString("tag"),
                        rset.getString("descricao")
                );
                listaHabilidades.add(habilidade);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar habilidades com filtro: " + e.getMessage());
            return null;
        } finally {
            try {
                if (rset != null) rset.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos ao buscar habilidades com filtro: " + e.getMessage());
            }
        }

        return listaHabilidades;
    }

    //    READ BY ID

    public Habilidades read(int id) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String readId = "SELECT * FROM habilidade WHERE id = ?";

        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(readId);
            pstmt.setInt(1, id);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                return new Habilidades(rset.getInt("id"),
                        rset.getString("nome"),
                        rset.getString("tag"),
                        rset.getString("descricao"));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar habilidades com filtro: " + e.getMessage());
            return null;
        } finally {
            try {
                if (rset != null) rset.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos ao buscar habilidades com filtro: " + e.getMessage());
            }
        }

        return null;
    }

    //    Update pelo objeto
    public int update(Habilidades habilidade) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        String update = "UPDATE habilidade SET nome = ?, tag = ?, descricao = ? WHERE id = ?";
        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(update);

            pstmt.setString(1, habilidade.getNome());
            pstmt.setString(2, habilidade.getTag());
            pstmt.setString(3, habilidade.getDescricao());
            pstmt.setInt(4, habilidade.getId());

            if (pstmt.executeUpdate() > 0){
                return 1;
            }
            return 0;
    }
        catch (SQLException e) {
            System.err.println("Erro ao atualizar habilidade: " + e.getMessage());
            return -1;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão após atualizar habilidade: " + e.getMessage());
            }
        }
    }

//    Update pelos parametros
    public int update(int id, String nome, String tag, String descricao) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        String update = "UPDATE habilidade SET nome = ?, tag = ?, descricao = ? WHERE id = ?";
        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(update);

            pstmt.setString(1, nome);
            pstmt.setString(2, tag);
            pstmt.setString(3, descricao);
            pstmt.setInt(4, id);
            if (pstmt.executeUpdate() > 0){
                return 1;
            }
            return 0;
        }
        catch (SQLException e) {
            System.err.println("Erro ao atualizar habilidade: " + e.getMessage());
            return -1;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão após atualizar habilidade: " + e.getMessage());
            }
        }
    }

//    Delete por Id
public int delete(int id) {
    Conexao conexao = new Conexao();
    Connection conn = null;
    PreparedStatement pstmt = null;
    String delete = "DELETE FROM habilidade WHERE id = ?";
    try {
        conn = conexao.conectar();
        pstmt = conn.prepareStatement(delete);
        pstmt.setInt(1, id);

        if (pstmt.executeUpdate() > 0){
            return 1;
        }
        return 0;
    }catch (SQLException e) {
        System.err.println("Erro ao deletar habilidade: " + e.getMessage());
        return -1;
    } finally {
        try {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Erro ao fechar conexão após deletar habilidade: " + e.getMessage());
        }
    }
    }

//    Delete by nome
    public int delete(String nome){
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        String delete = "DELETE FROM habilidade WHERE nome = ?";
        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(delete);
            pstmt.setString(1, nome);

            if (pstmt.executeUpdate() > 0){
                return 1;
            }
            return 0;
        }catch (SQLException e) {
            System.err.println("Erro ao deletar habilidade: " + e.getMessage());
            return -1;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão após deletar habilidade: " + e.getMessage());
            }
        }
    }
}
