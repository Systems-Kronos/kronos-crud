package com.example.kronoscrud.Models;

import java.util.List;

public class Plano {
    private int id;
    private String nome;
    private float custo;
    private String descricao;
    private int maxFuncionarios;
//    Constructors
    public Plano (int id, String nome, float custo, String descricao, int maxFuncionarios){
        this.id = id;
        this.nome = nome;
        this.custo = custo;
        this.descricao = descricao;
        this.maxFuncionarios = maxFuncionarios;
    }
    public Plano (String nome, float custo, String descricao, int maxFuncionarios){
        this.nome = nome;
        this.custo = custo;
        this.descricao = descricao;
        this.maxFuncionarios = maxFuncionarios;
    }
//    Getters e Setters

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public float getCusto() {
        return custo;
    }
    public void setCusto(float custo) {
        this.custo = custo;
    }

    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getMaxFuncionarios() {
        return maxFuncionarios;
    }
    public void setMaxFuncionarios(int maxFuncionarios) {
        this.maxFuncionarios = maxFuncionarios;
    }
//    toString
    public String toString(){
        return String.format("Plano | Id: %-3d | Custo: %-7f | Descrição: %-60s | Max. funcionários: %-5d",
                this.id,
                this.custo,
                this.descricao,
                this.maxFuncionarios
        );
    }
}

