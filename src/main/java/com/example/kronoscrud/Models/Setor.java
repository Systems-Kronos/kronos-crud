package com.example.kronoscrud.Models;

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
    private int qntFuncionarios;
    private List<Usuario> funcionarios;

// Métodos Construtores

// As validações de exceções são realizadas pelos métodos set. 
    public Setor(int id, Empresa empresa, String nome, String descricao, int qntFuncionarios, List<Usuario> funcionarios) {
        try {
            this.setId(id);
            this.setEmpresa(empresa);
            this.setNome(nome);
            this.setDescricao(descricao);
            this.setQntFuncionarios(qntFuncionarios);
            this.setFuncionarios(funcionarios);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        
    }
    public Setor(Empresa empresa, String nome, String descricao, int qntFuncionarios, List<Usuario> funcionarios) {
        try {
            this.setEmpresa(empresa);
            this.setNome(nome);
            this.setDescricao(descricao);
            this.setQntFuncionarios(qntFuncionarios);
            this.setFuncionarios(funcionarios);
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

// Para a lista de funcionários
    public List<Usuario> getFuncionarios() {
        return funcionarios;
    }
    public void setFuncionarios(List<Usuario> funcionarios) {
        if (funcionarios == null || funcionarios.isEmpty()) { // Exceção: verifica se a lista de funcionários é nula ou vazia.
            throw new IllegalArgumentException("A lista de funcionários não pode ser nula ou vazia.");
        } 

        for (Usuario funcionario : funcionarios) { // Valida cada objeto de funcionário, um por um.
            if (funcionario == null) { // Exceção: verifica se o objeto de funcionário é nulo.
                throw new IllegalArgumentException("Um objeto de funcionário não pode ser nulo");
            }
        }
        this.funcionarios = funcionarios;
    }

// Método toString
    public String toString() {
        // Como o atributo funcionarios é uma lista, este foi transformado em String para o toString.
        String stringFuncionarios = String.join(", ", this.funcionarios.stream().map(Usuario::getNome).collect(Collectors.toList()));
        return String.format("Setor | Id: %-3d | Empresa: %-20s | Nome: %-20s | Descrição: %-50s | Qnt. Funcionários: %-5d | Funcionários: %-40s",
                this.id,
                this.empresa,
                this.nome,
                this.descricao,
                this.qntFuncionarios,
                stringFuncionarios
        );
    }
}
