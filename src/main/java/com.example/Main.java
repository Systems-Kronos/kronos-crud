package com.example;

import java.sql.Connection;
import java.time.LocalTime;

import com.example.Controller.*;
import com.example.Model.*;
import com.example.dao.*;
import java.util.List;

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

//            Plano plano = new Plano(2, "2", 21, "ededde", 12);
//            PlanoDAO planoDAO = new PlanoDAO();
//            planoDAO.inserir(plano);

//  ************************************
//  ********* Teste EmpresaDAO *********
//  ************************************
//            EmpresaDAO dao = new EmpresaDAO();
//
//            Empresa empresa = new Empresa(
//                    15,
//                    "Kronos Enterprise",
//                    "12345-123",
//                    "12.345.678/1234-56",
//                    "teste@email.com",
//                    "(11)12345-6789",
//                    "Médio",
//                    LocalTime.of(8, 0),
//                    LocalTime.of(18, 0),
//                    "Regra de Negócio X"
//            );
//
////            Create-Empresa
//            boolean criado = dao.create(empresa);
//            System.out.println("CREATE: " + criado);
//
////            Read-Empresa
//            List<Empresa> empresas = dao.read();
//            System.out.println("\nREAD ALL:");
//            empresas.forEach(System.out::println);
//
////            ReadID-Empresa
//            Empresa ePorId = dao.read(15);
//            System.out.println("\nREAD BY ID:");
//            System.out.println(ePorId);
//
////            UpdateEmpresa-Empresa
//            ePorId.setNome("Empresa Atualizada");
//            ePorId.setEmail("novoemail@email.com");
//            int atualizado = dao.update(ePorId);
//            System.out.println("\nUPDATE (objeto): " + atualizado);
//
////            UpdateParametro-Empresa
//            int atualizadoParams = dao.update(
//                    15,
//                    "Empresa Params",
//                    "54321-321",
//                    "65.432.109/8765-43",
//                    "params@email.com",
//                    "(22)98765-4321",
//                    "Grande",
//                    LocalTime.of(9, 0),
//                    LocalTime.of(17, 0),
//                    "Nova regra"
//            );
//            System.out.println("UPDATE (params): " + atualizadoParams);
//
////            DeleteId-Empresa
//            int deletadoId = dao.delete(15);
//            System.out.println("\nDELETE by ID: " + deletadoId);
//
////            DeleteCnpj-Empresa
//            dao.create(empresa);
//            int deletadoCnpj = dao.delete("12345678123456");
//            System.out.println("DELETE by CNPJ: " + deletadoCnpj);


            conecta.desconectar(conn);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("não foi");
        }

    }
}