package com.example.example.Controller;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    public Connection conectar() {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            Dotenv dotenv = Dotenv.load();

            String dbHost = dotenv.get("URL");
            String dbUser = dotenv.get("USER");
            String dbPassword = dotenv.get("PASSWORD");
            String dbPass = dotenv.get("PASS");


            conn = DriverManager.getConnection(
                    dbHost,
                    dbUser,dbPassword);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            return conn;
        }

    }
    public void desconectar(Connection conn){
        try{
            if (conn != null && !conn.isClosed()){
                //Desconectando do BD
                conn.close();
            }

        }catch(SQLException e) {
            e.printStackTrace();
        }}}
