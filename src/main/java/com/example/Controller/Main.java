package com.example.Controller;

import java.sql.Connection;

import com.example.Dao.PlanoDAO;
import com.example.Model.*;

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

            Plano plano = new Plano(2, "2", 21, "ededde", 12);
            PlanoDAO planoDAO = new PlanoDAO();
            planoDAO.inserir(plano);

            conecta.desconectar(conn);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("não foi");
        }

    }
}
