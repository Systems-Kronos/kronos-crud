package com.example.dao;

import com.example.Controller.Conexao;
import com.example.Model.Empresa;
import com.example.Model.Plano;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class PlanoDAO {
     //    Create
        public boolean create(Plano plano) {
            Conexao conexao = new Conexao();
            Connection conn = null;
            PreparedStatement pstmt = null;
            String create = "INSERT INTO  planos (id,nomeplano, custo, descricao, qnt_max_funcionario) VALUES (?,?,?,?,?)";
            try {
                conn = conexao.conectar();
                pstmt = conn.prepareStatement(create);
                pstmt.setInt(1, plano.getId());
                pstmt.setString(2, plano.getNome());
                pstmt.setFloat(3, plano.getCusto());
                pstmt.setString(4, plano.getDescricao());
                pstmt.setInt(5, plano.getMaxFuncionarios());

                return pstmt.executeUpdate() > 0; // true se inseriu
            } catch (SQLException e) {
                System.err.println("Erro ao inserir plano: " + e.getMessage());
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

//        READ ALL
        public List<Plano> read() {
            Conexao conexao = new Conexao();
            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet rset = null;
            String read = "SELECT * FROM planos";
            List<Plano> listaPlano = new LinkedList<>();

            try {
                conn = conexao.conectar();
                pstmt = conn.prepareStatement(read);
                rset = pstmt.executeQuery();

                while (rset.next()) {
                    Plano plano = new Plano(rset.getInt("id"),
                            rset.getString("nomeplano"),
                            rset.getFloat("custo"),
                            rset.getString("descricao"),
                            rset.getInt("qnt_max_funcionario"));
                    listaPlano.add(plano);
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

            return listaPlano;
            }
    }

