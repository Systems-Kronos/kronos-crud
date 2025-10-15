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







            // ************************************
            // ********* Teste HabilidadesDAO ******
            // ************************************

//            HabilidadesDAO dao = new HabilidadesDAO();
//
//            Habilidades habilidade = new Habilidades(
//                    10,
//                    "Manutenção Preventiva",
//                    "Segurança_Industrial",
//                    "Capacidade de realizar manutenções rotineiras em equipamentos de forma preventiva."
//            );
//
//            // CREATE - Habilidade
//            boolean criado = dao.create(habilidade);
//            System.out.println("CREATE: " + criado);
//
//            // READ ALL - Habilidade
//            List<Habilidades> habilidades = dao.read();
//            System.out.println("\nREAD ALL:");
//            habilidades.forEach(System.out::println);
//
//            // READ BY ID - Habilidade
//            Habilidades hPorId = dao.read(10);
//            System.out.println("\nREAD BY ID:");
//            System.out.println(hPorId);
//
//            // UPDATE (objeto)
//            if (hPorId != null) {
//                hPorId.setNome("Manutenção Atualizada");
//                hPorId.setTag("Atualização_Sistema");
//                hPorId.setDescricao("Habilidade voltada à atualização e revisão de sistemas industriais.");
//                int atualizado = dao.update(hPorId);
//                System.out.println("\nUPDATE (objeto): " + atualizado);
//            }
//
//            // UPDATE (parâmetros)
//            int atualizadoParams = dao.update(
//                    10,
//                    "Gestão de Riscos",
//                    "Segurança_Trabalho",
//                    "Capacidade de identificar e mitigar riscos operacionais no ambiente industrial."
//            );
//            System.out.println("UPDATE (params): " + atualizadoParams);
//
//            // DELETE by ID
//            int deletadoId = dao.delete(10);
//            System.out.println("\nDELETE by ID: " + deletadoId);
//
//            // DELETE by Nome
//            dao.create(habilidade);
//            int deletadoNome = dao.delete("Manutenção Preventiva");
//            System.out.println("DELETE by Nome: " + deletadoNome);








//
//            // ************************************
//            // ********* Teste PlanoDAO   *********
//            // ************************************
//
//            PlanoDAO planoDAO = new PlanoDAO();
//
//            // ===== CREATE =====
//            // Criação de um novo plano com valores de exemplo
//            Plano planoNovo = new Plano(10, "Plano Premium", 149.90f, "Acesso completo a todas as funcionalidades", 50);
//            boolean criado = planoDAO.create(planoNovo);
//            System.out.println("CREATE: " + criado);
//
//            // ===== READ ALL =====
//            // Busca todos os planos cadastrados
//            System.out.println("\n===== LISTA DE PLANOS =====");
//            for (Plano p : planoDAO.read()) {
//                System.out.println(p);
//            }
//
//            // ===== READ BY ID =====
//            // Busca um plano específico pelo ID
//            System.out.println("\n===== BUSCA POR ID =====");
//            Plano planoBuscado = planoDAO.read(10);
//            if (planoBuscado != null) {
//                System.out.println(planoBuscado);
//            } else {
//                System.out.println("Plano não encontrado!");
//            }
//
//            // ===== UPDATE =====
//            // Atualiza os dados do plano existente
//            System.out.println("\n===== UPDATE =====");
//            Plano planoAtualizado = new Plano(10, "Plano Empresarial", 199.90f, "Plano voltado para empresas com até 100 funcionários", 100);
//            int updateResult = planoDAO.update(planoAtualizado);
//            System.out.println("UPDATE: " + (updateResult > 0 ? "sucesso" : "falhou"));
//
//            // ===== DELETE BY ID =====
//            // Exclui o plano pelo ID
//            System.out.println("\n===== DELETE POR ID =====");
//            int deleteId = planoDAO.delete(10);
//            System.out.println("DELETE ID: " + (deleteId > 0 ? "sucesso" : "falhou"));
//
//            // ===== DELETE BY NOME =====
//            // Exclui o plano pelo nome
//            System.out.println("\n===== DELETE POR NOME =====");
//            planoDAO.create(planoAtualizado);
//            int deleteNome = planoDAO.delete("Plano Empresarial");
//            System.out.println("DELETE NOME: " + (deleteNome > 0 ? "sucesso" : "falhou"));
//








//             ************************************
//             ********* Teste administracaoDAO   *********
//             ************************************

//
//            AdministracaoDAO administracaoDAO = new AdministracaoDAO();
//
//            // ===== CREATE =====
//            // Criação de um novo administrador com valores de exemplo
//            Administracao adminNovo = new Administracao(15, "Carlos Andrade", "carlos.andrade@email.com", "Senha@123", "ACESSO123");
//            boolean criado = administracaoDAO.create(adminNovo);
//            System.out.println("CREATE: " + criado);
//
//            // ===== READ ALL =====
//            // Lista todos os administradores cadastrados no banco
//            System.out.println("\n===== LISTA DE ADMINISTRAÇÕES =====");
//            for (Administracao a : administracaoDAO.read()) {
//                System.out.println(a);
//            }
//
//            // ===== READ BY ID =====
//            // Busca um administrador específico pelo ID
//            System.out.println("\n===== BUSCA POR ID =====");
//            Administracao adminBuscado = administracaoDAO.read(15);
//            if (adminBuscado != null) {
//                System.out.println(adminBuscado);
//            } else {
//                System.out.println("Administrador não encontrado!");
//            }
//
//            // ===== UPDATE =====
//            // Atualiza os dados do administrador existente
//            System.out.println("\n===== UPDATE =====");
//            Administracao adminAtualizado = new Administracao(15, "Carlos Almeida", "carlos.almeida@email.com", "NovaSenha@456", "NOVOACESSO");
//            int updateResult = administracaoDAO.update(adminAtualizado);
//            System.out.println("UPDATE: " + (updateResult > 0 ? "sucesso" : "falhou"));
//
//            // ===== DELETE BY ID =====
//            // Exclui o administrador pelo ID
//            System.out.println("\n===== DELETE POR ID =====");
//            int deleteId = administracaoDAO.delete(15);
//            System.out.println("DELETE ID: " + (deleteId > 0 ? "sucesso" : "falhou"));
//
//            // ===== DELETE BY NOME =====
//            // Exclui o administrador pelo nome
//            System.out.println("\n===== DELETE POR NOME =====");
//            administracaoDAO.create(adminAtualizado);
//            int deleteNome = administracaoDAO.delete("Carlos Almeida");
//            System.out.println("DELETE NOME: " + (deleteNome > 0 ? "sucesso" : "falhou"));








//             ************************************
//             ********* Teste SetorDAO   *********
//             ************************************

//            SetorDAO setorDAO = new SetorDAO();

            // ===================== CREATE =====================
//            Setor novoSetor = new Setor(
//                    "Manutenção",
//                    "Responsável pelos reparos e máquinas",
//                    "Diurno e Noturno",
//                    25,
//                    1
//            );
//
//            if (setorDAO.create(novoSetor)) {
//                System.out.println("Setor criado com sucesso!");
//            } else {
//                System.out.println("Erro ao criar setor.");
//            }

            // ===================== READ ALL =====================
//            List<Setor> setores = setorDAO.read();
//            if (setores != null) {
//                for (Setor setor : setores) {
//                    System.out.println(setor);
//                }
//            } else {
//                System.out.println("Erro ao buscar setores.");
//            }

            // ===================== READ BY ID =====================
//            Setor setorEncontrado = setorDAO.read(1);
//            if (setorEncontrado != null) {
//                System.out.println("Setor encontrado: " + setorEncontrado);
//            } else {
//                System.out.println("Nenhum setor encontrado com esse ID.");
//            }

            // ===================== UPDATE (objeto) =====================
//            Setor setorAtualizado = new Setor(
//                    1,
//                    "Produção",
//                    "Área principal de fabricação",
//                    "Turnos alternados",
//                    40,
//                    1
//            );
//
//            int resultadoUpdate = setorDAO.update(setorAtualizado);
//            if (resultadoUpdate == 1) {
//                System.out.println("Setor atualizado com sucesso!");
//            } else if (resultadoUpdate == 0) {
//                System.out.println("Nenhum setor encontrado para atualizar.");
//            } else {
//                System.out.println("Erro ao atualizar setor.");
//            }

            // ===================== DELETE BY ID =====================
//            int resultadoDeleteId = setorDAO.delete(2);
//            if (resultadoDeleteId == 1) {
//                System.out.println("Setor deletado com sucesso!");
//            } else if (resultadoDeleteId == 0) {
//                System.out.println("Nenhum setor encontrado com esse ID.");
//            } else {
//                System.out.println("Erro ao deletar setor.");
//            }

            // ===================== DELETE BY NOME =====================
//            setorDAO.create(novoSetor);
//            int resultadoDeleteNome = setorDAO.delete("Manutenção");
//            if (resultadoDeleteNome == 1) {
//                System.out.println("Setor deletado com sucesso!");
//            } else if (resultadoDeleteNome == 0) {
//                System.out.println("Nenhum setor encontrado com esse nome.");
//            } else {
//                System.out.println("Erro ao deletar setor.");
//            }


            conecta.desconectar(conn);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("não foi");
        }

    }
}