package com.example;

import java.sql.Connection;

import com.example.example.Controller.Conexao;


public class Main {
    public static void main(String[] args) {
        try {
            Conexao conecta = new Conexao();
            Connection conn = conecta.conectar();
            conecta.desconectar(conn);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("não foi");
        }

    }
}
