package com.example.Model;
import com.example.Controller.*;
import java.sql.*;

public class AdministracaoDAO {
//    Inserir
    public boolean inserir(Administracao administracao) {
        Conexao conexao = new Conexao();
        Connection conn = null;
        PreparedStatement pstmt = null;