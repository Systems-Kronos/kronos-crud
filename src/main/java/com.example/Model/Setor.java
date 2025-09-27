package com.example.Model;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Representa um setor da indústria.
 * A classe garante a integridade dos dados relacionados ao setor e sua associação
 * com a empresa e seus funcionários.
 */
public class Setor {
// Atributos
    private int id;
    private Empresa empresa;
    private String nome;
    private String descricao;
    private String turnos;
    private int qntFuncionarios;
    private int idEmpresa;

// Métodos Construtores

// As validações de exceções são realizadas pelos métodos set.
    public Setor(int id, Empresa empresa, String nome, String descricao, String turnos, int qntFuncionarios, int idEmpresa) {
        try {
            this.setId(id);
            this.setEmpresa(empresa);
            this.setNome(nome);
            this.setDescricao(descricao);
            this.setTurnos(turnos);
            this.setQntFuncionarios(qntFuncionarios);
            this.setIdEmpresa(idEmpresa);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

    }
    public Setor(Empresa empresa, String nome, String descricao, String turnos, int qntFuncionarios, int idEmpresa) {
        try {
            this.setEmpresa(empresa);
            this.setNome(nome);
            this.setDescricao(descricao);
            this.setTurnos(turnos);
            this.setQntFuncionarios(qntFuncionarios);
            this.setIdEmpresa(idEmpresa);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

// Métodos Getters e Setters

// Para o ID
    public int getId() {
        return id;
    }
    public void setId(int id) {
        if (id <= 0) { // Exceção: verifica se o ID é negativo ou igual a zero.
            throw new IllegalArgumentException("O ID (" + id + ") não pode ser zero ou negativo.");
        }
        this.id = id;
    }

// Para o objeto da empresa
    public Empresa getEmpresa() {
        return empresa;
    }
    public void setEmpresa(Empresa empresa) {
        if (empresa == null) { // Exceção: verifica se o objeto da empresa é nula.
            throw new IllegalArgumentException("O objeto da empresa não pode ser nula.");
        }
        this.empresa = empresa;
    }

// Para o nome
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) { // Exceção: verifica se o nome é nulo ou só contém espaço.
            throw new IllegalArgumentException("O nome não pode ser nulo ou em branco.");
        }
        this.nome = nome;
    }

// Para a descrição
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        if (descricao == null || descricao.trim().isEmpty()) { // Exceção: verifica se a descrição é nula ou só contém espaço.
            throw new IllegalArgumentException("A descrição não pode ser nula ou em branco.");
        }
        this.descricao = descricao;
    }

    // Para os turnos
    public String getTurnos() {
        return turnos;
    }
    public void setTurnos(String turnos) {
        if (turnos == null || turnos.trim().isEmpty()) { // Exceção: verifica se turnos é nulo ou só contém espaço.
            throw new IllegalArgumentException("Os turnos não podem ser nulo ou em branco.");
        }
        this.turnos = turnos;
    }

// Para a quantidade de funcionários
    public int getQntFuncionarios() {
        return qntFuncionarios;
    }
    public void setQntFuncionarios(int qntFuncionarios) {
        if (qntFuncionarios <= 0) { // Exceção: verifica se a quantidade de funcionários é negativo ou igual a zero.
            throw new IllegalArgumentException("A quantidade de funcionários (" + qntFuncionarios + ") não pode ser negativo ou igual a zero.");
        }
        this.qntFuncionarios = qntFuncionarios;
    }
//    Para id da empresa
      public int getIdEmpresa() {
        return idEmpresa;
    }
    public void setIdEmpresa(int idEmpresa) {
        if (idEmpresa <= 0) { // Exceção: verifica se a quantidade de funcionários é negativo ou igual a zero.
            throw new IllegalArgumentException("O id da empresa (" + idEmpresa + ") não pode ser negativa ou igual a zero.");
        }
        this.idEmpresa = idEmpresa;
    }

// Método toString
    public String toString() {
        // Como o atributo funcionarios é uma lista, este foi transformado em String para o toString.
        return String.format("Setor | Id: %-3d | Empresa: %-20s | Nome: %-20s | Descrição: %-50s | Qnt. Funcionários: %-5d | Funcionários: %-40s",
                this.id,
                this.empresa,
                this.nome,
                this.descricao,
                this.qntFuncionarios,
                this.idEmpresa
        );
    }
}
