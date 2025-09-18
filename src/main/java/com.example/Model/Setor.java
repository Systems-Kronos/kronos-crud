package com.example.Model;

import java.util.List;

public class Setor {
    private int id;
    private Empresa empresa;
    private String nome;
    private String descricao;
    private int qntFuncionarios;
    private List<Usuario> funcionarios;
//    Constructors
    public Setor(int id, Empresa empresa, String nome, String descricao, int qntFuncionarios, List<Usuario> funcionarios) {
        this.id = id;
        this.empresa = empresa;
        this.nome = nome;
        this.descricao = descricao;
        this.qntFuncionarios = qntFuncionarios;
        this.funcionarios = funcionarios;
    }
    public Setor(Empresa empresa, String nome, String descricao, int qntFuncionarios, List<Usuario> funcionarios) {
        this.empresa = empresa;
        this.nome = nome;
        this.descricao = descricao;
        this.qntFuncionarios = qntFuncionarios;
        this.funcionarios = funcionarios;
    }
//    Getters e Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Empresa getEmpresa() {
        return empresa;
    }
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getQntFuncionarios() {
        return qntFuncionarios;
    }
    public void setQntFuncionarios(int qntFuncionarios) {
        this.qntFuncionarios = qntFuncionarios;
    }

    public List<Usuario> getFuncionarios() {
        return funcionarios;
    }
    public void setFuncionarios(List<Usuario> funcionarios) {
        this.funcionarios = funcionarios;
    }
//    toString
    public String toString() {
        return String.format("Setor | Id: %-3d | Empresa: %-20s | Nome: %-20s | Descrição: %-50s | Qnt. Funcionários: %-5d | Funcionários: %-40s",
                this.id = id,
                this.empresa = empresa,
                this.nome = nome,
                this.descricao = descricao,
                this.qntFuncionarios = qntFuncionarios,
                this.funcionarios = funcionarios
        );
    }
}
