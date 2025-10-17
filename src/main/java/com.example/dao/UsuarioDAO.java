package com.example.dao;

import com.example.Controller.Conexao;
import com.example.Model.Empresa;
import com.example.Model.Habilidades;
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
        String read = "SELECT u.id AS usuario_id, u.nome AS usuario_nome, u.cpf AS usuario_cpf, u.genero AS usuario_genero, u.status AS usuario_status, u.senha AS usuario_senha, u.fk_setor_id AS usuario_id_setor, u.fk_supervisor_id AS usuario_id_supervisor, u.cargo AS usuario_cargo, h.id AS habilidade_id, h.nome AS habilidade_nome, h.tag AS habilidade_tag, h.descricao AS habilidade_descricao FROM usuario u LEFT JOIN usuario_habilidade uh ON u.id = uh.fk_usuario_id LEFT JOIN habilidade h ON h.id = uh.fk_habilidade_id ORDER BY u.id";
        Usuario usuarioAtual = null;
        List<Usuario> listaUsuario = new LinkedList<>();

        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(read);
            rset = pstmt.executeQuery();

            int idUltimoUsuario = -1;

            while (rset.next()) {
                int idUsuario = rset.getInt("usuario_id");

                // Cria novo usuário se mudou de ID
                if (usuarioAtual == null || idUsuario != idUltimoUsuario) {
                    usuarioAtual = new Usuario(
                            idUsuario,
                            rset.getString("usuario_nome"),
                            rset.getString("usuario_cpf"),
                            rset.getString("usuario_genero").charAt(0),
                            rset.getString("usuario_status"),
                            rset.getString("usuario_senha"),
                            rset.getInt("usuario_id_setor"),
                            rset.getInt("usuario_id_supervisor"),
                            rset.getString("usuario_cargo")
                    );
                    listaUsuario.add(usuarioAtual);
                    idUltimoUsuario = idUsuario;
                }

                // Adiciona habilidade, se existir
                int idHabilidade = rset.getInt("habilidade_id");
                if (idHabilidade > 0) {
                    Habilidades habilidade = new Habilidades(
                            idHabilidade,
                            rset.getString("habilidade_nome"),
                            rset.getString("habilidade_tag"),
                            rset.getString("habilidade_descricao")
                    );
                    usuarioAtual.getListaHabilidades().add(habilidade);
                }
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
//    public Usuario read(int id) {
//        Conexao conexao = new Conexao();
//        Connection conn = null;
//        PreparedStatement pstmt = null;
//        ResultSet rset = null;
//        String readId = "SELECT * FROM usuario WHERE id = ?";
//
//        try {
//            conn = conexao.conectar();
//            pstmt = conn.prepareStatement(readId);
//            pstmt.setInt(1, id);
//            rset = pstmt.executeQuery();
//
//            if (rset.next()) {
//                return new Usuario(rset.getInt("id"),
//                        rset.getString("nome"),
//                        rset.getString("cpf"),
//                        rset.getString("genero").charAt(0),
//                        rset.getString("status"),
//                        rset.getString("senha"),
//                        rset.getInt("fk_setor_id"),
//                        rset.getInt("fk_supervisor_id"),
//                        rset.getString("cargo"));
//            }
//        } catch (SQLException e) {
//            System.err.println("Erro ao buscar usuario por ID: " + e.getMessage());
//        } finally {
//            try {
//                if (rset != null) rset.close();
//                if (pstmt != null) pstmt.close();
//                if (conn != null) conn.close();
//            } catch (SQLException e) {
//                System.err.println("Erro ao fechar recursos ao buscar usuario por ID: " + e.getMessage());
//            }
//        }
//        return null;
//    }

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

//    DELETE By id
    public int delete(int id){
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        String delete = "DELETE FROM usuario WHERE id = ?";
        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(delete);
            pstmt.setInt(1, id);

            if (pstmt.executeUpdate() > 0){
                return 1;
            }
            return 0;
        }catch (SQLException e) {
            System.err.println("Erro ao deletar usuario: " + e.getMessage());
            return -1;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão após deletar usuario: " + e.getMessage());
            }
        }
    }

//    DELETE By nome
public int delete(String nome) {
    Conexao conexao = new Conexao();
    Connection conn = null;
    PreparedStatement pstmt = null;
    String delete = "DELETE FROM usuario WHERE nome = ?";
    try {
        conn = conexao.conectar();
        pstmt = conn.prepareStatement(delete);
        pstmt.setString(1, nome);

        if (pstmt.executeUpdate() > 0){
            return 1;
        }
        return 0;
    }catch (SQLException e) {
        System.err.println("Erro ao deletar usuario: " + e.getMessage());
        return -1;
    } finally {
        try {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Erro ao fechar conexão após deletar usuario: " + e.getMessage());
        }
    }}

//    METODO PARA ASSOCIAR COM HABILIDADE

//    ADD Habilidade to Usuario
    public boolean addHabilidadeToUsuario(int idUsuario, int idHabilidade) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        String createHabilidade = "INSERT INTO usuario_habilidade (fk_usuario_id, fk_habilidade_id) VALUES (?, ?)";

        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(createHabilidade);

            pstmt.setInt(1, idUsuario);
            pstmt.setInt(2, idHabilidade);

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

//    REMOVE Habilidade from Usuario
    public int removeHabilidadeFromUsuario(int idUsuario, int idHabilidade) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;
        String removeHabilidade = "DELETE FROM usuario_habilidade WHERE fk_usuario_id=? AND fk_habilidade_id=?";

        try {
            conn = conexao.conectar();
            pstmt = conn.prepareStatement(removeHabilidade);

            pstmt.setInt(1, idUsuario);
            pstmt.setInt(2, idHabilidade);

            if (pstmt.executeUpdate() > 0){
                return 1;
            }
            return 0;
        }catch (SQLException e) {
            System.err.println("Erro ao deletar usuario: " + e.getMessage());
            return -1;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão após deletar usuario: " + e.getMessage());
            }
        }
    }
}
