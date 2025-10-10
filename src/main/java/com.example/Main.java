package com.example;

import java.sql.Connection;
import java.util.List;

import com.example.Controller.*;
import com.example.Model.*;
import com.example.dao.AdministracaoDAO;
import com.example.dao.PlanoDAO;

public class Main {
    public static void main(String[] args) {
        try {
            Conexao conecta = new Conexao();
            Connection conn = conecta.conectar();

//            Administracao adm = new Administracao(1, "2", "3", "4", "5");
//            AdministracaoDAO admDAO = new AdministracaoDAO();
//            admDAO.inserir(adm);

//            Empresa empresa = new Empresa(
//                    9,
//                    "90",
//                    "99999-999",
//                    "12.345.678/0001-95",
//                    "4",
//                    "11956775122",
//                    "6",
//                    LocalTime.of(9,0),
//                    LocalTime.of(16,30),
//                    "12");
//            EmpresaDAO empresaDAO = new EmpresaDAO();
//            empresaDAO.inserir(empresa);

            AdministracaoDAO dao = new AdministracaoDAO();

            System.out.println("📘 Lendo registros da tabela 'administracao'...");

            List<Administracao> administradores = dao.read();

            if (administradores.isEmpty()) {
                System.out.println("⚠️ Nenhum registro encontrado.");
            } else {
                System.out.println("✅ Registros encontrados:");
                for (Administracao admin : administradores) {
                    System.out.println(admin);
                }
            }
            conecta.desconectar(conn);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("não foi");
        }

    }
}
