package com.example.dao;
import com.example.Controller.*;
import com.example.Model.Administracao;
import com.example.Model.Empresa;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class AdministracaoDAO {
//    Create
    public boolean create(Administracao administracao) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        String create = "INSERT INTO administracao (nome, email, senha) VALUES (?,?,?)";
        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(create);
            pstmt.setString(1, administracao.getNome());
            pstmt.setString(2, administracao.getEmail());
            pstmt.setString(3, administracao.getSenha());

            return pstmt.executeUpdate() > 0; // true se inseriu
        } catch (SQLException e) {
            System.err.println("Erro ao inserir admnistracao: " + e.getMessage());
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

    public List<Administracao> read() {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String read = "SELECT * FROM administracao";
        List<Administracao> listaAdministracao = new LinkedList<>();

        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(read);
            rset = pstmt.executeQuery();

            while (rset.next()) {
            Administracao administracao = new Administracao(rset.getInt("id"),
                    rset.getString("nome"),
                    rset.getString("email"),
                    rset.getString("senha"));
            listaAdministracao.add(administracao);
            }
        }catch (SQLException e) {
            System.err.println("Erro ao buscar administracao: " + e.getMessage());
            return null;
        } finally {
            try {
                if (rset != null) rset.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos ao buscar administracao: " + e.getMessage());
            }
        }

        return listaAdministracao;
    }

//    READ By Id
// READ com filtro por nome e ordenação
public List<Administracao> read(String nome, String orderBy, String direction) {
    Conexao conexao = new Conexao();
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    List<Administracao> listaAdministracao = new LinkedList<>();

    String sql = "SELECT * FROM administracao";

    if (nome != null && !nome.isEmpty()) {
        sql += " WHERE nome ILIKE '%" + nome + "%'";
    }

    String colunaOrdenacao = "id";
    if (orderBy != null) {
        if (orderBy.equals("nome")) {
            colunaOrdenacao = "nome";
        } else if (orderBy.equals("email")) {
            colunaOrdenacao = "email";
        } else if (orderBy.equals("senha")) {
            colunaOrdenacao = "senha";
        }
    }

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
            Administracao administracao = new Administracao(
                    rset.getInt("id"),
                    rset.getString("nome"),
                    rset.getString("email"),
                    rset.getString("senha")
            );
            listaAdministracao.add(administracao);
        }

    } catch (SQLException e) {
        System.err.println("Erro ao buscar administracao com filtro: " + e.getMessage());
        return null;
    } finally {
        try {
            if (rset != null) rset.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Erro ao fechar recursos ao buscar administracao com filtro: " + e.getMessage());
        }
    }

    return listaAdministracao;
}

    //    READ By Id
    public Administracao read(int id) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String readId = "SELECT * FROM administracao WHERE id = ?";

        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(readId);
            pstmt.setInt(1, id);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                return new Administracao(rset.getInt("id"),
                        rset.getString("nome"),
                        rset.getString("email"),
                        rset.getString("senha"));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar administracao com filtro: " + e.getMessage());
            return null;
        } finally {
            try {
                if (rset != null) rset.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos ao buscar administracao com filtro: " + e.getMessage());
            }
        }
        return null;
    }

    public Administracao read(String email, String senha) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String readEmail = "SELECT * FROM administracao WHERE email = ? and senha = ?";
        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(readEmail);
            pstmt.setString(1, email);
            pstmt.setString(2, senha);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                return new Administracao(rset.getInt("id"),
                        rset.getString("nome"),
                        rset.getString("email"),
                        rset.getString("senha"));
            }

        }catch (SQLException e) {
            System.err.println("Erro ao buscar administracao por email e senha: " + e.getMessage());
        } finally {
            try {
                if (rset != null) rset.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos ao buscar administracao por ID: " + e.getMessage());
            }
        }
        return null;
    }

//    Update pelo objeto
    public int update(Administracao administracao) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        String update = "UPDATE administracao SET nome = ?, email = ?, senha = ? WHERE id = ?";
        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(update);

            pstmt.setString(1, administracao.getNome());
            pstmt.setString(2, administracao.getEmail());
            pstmt.setString(3, administracao.getSenha());
            pstmt.setInt(4, administracao.getId());
            if (pstmt.executeUpdate() > 0){
                return 1;
            }
            return 0;
        }
        catch (SQLException e) {
            System.err.println("Erro ao atualizar administracao: " + e.getMessage());
            return -1;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão após atualizar administracao: " + e.getMessage());
            }
        }
    }

//    Update com os parametros
public int update(String nome, String email, String senha, int id) {
    Conexao conexao = new Conexao();
    Connection conn = null;
    PreparedStatement pstmt = null;
    String update = "UPDATE administracao SET nome = ?, email = ?, senha = ? WHERE id = ?";
    try {
        conn = conexao.conectar();
        pstmt = conn.prepareStatement(update);

        pstmt.setString(1, nome);
        pstmt.setString(2, email);
        pstmt.setString(3, senha);
        pstmt.setInt(4, id);
        if (pstmt.executeUpdate() > 0){
            return 1;
        }
        return 0;
    }
    catch (SQLException e) {
        System.err.println("Erro ao atualizar administracao: " + e.getMessage());
        return -1;
    } finally {
        try {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Erro ao fechar conexão após atualizar administracao: " + e.getMessage());
        }
    }
}

//Delete By Id
    public int delete(int id) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        String delete = "DELETE FROM administracao WHERE id = ?";
        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(delete);
            pstmt.setInt(1, id);

            if (pstmt.executeUpdate() > 0){
                return 1;
            }
            return 0;
        }catch (SQLException e) {
            System.err.println("Erro ao deletar adiministracao: " + e.getMessage());
            return -1;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão após deletar adiministracao: " + e.getMessage());
            }
        }
    }

//    Delete By Nome
public int delete(String nome) {
    Conexao conexao = new Conexao();
    Connection conn = null;
    PreparedStatement pstmt = null;
    String delete = "DELETE FROM administracao WHERE nome = ?";
    try {
        conn = conexao.conectar();
        pstmt = conn.prepareStatement(delete);
        pstmt.setString(1, nome);

        if (pstmt.executeUpdate() > 0){
            return 1;
        }
        return 0;
    }catch (SQLException e) {
        System.err.println("Erro ao deletar adiministracao: " + e.getMessage());
        return -1;
    } finally {
        try {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Erro ao fechar conexão após deletar administracao: " + e.getMessage());
        }
    }}
   }
