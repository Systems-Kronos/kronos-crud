package com.example.kronoscrud.Models;

import java.util.List;

public class Habilidades {
    private int id;
    private String nome;
    private List<String> Tags;
    private String descricao;
//    Constructor

    public Habilidades(int id, String nome, List<String> tags, String descricao) {
        this.id = id;
        this.nome = nome;
        Tags = tags;
        this.descricao = descricao;
    }
    public Habilidades(String nome, List<String> tags, String descricao) {
        this.nome = nome;
        Tags = tags;
        this.descricao = descricao;
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

    public List<String> getTags() {
        return Tags;
    }
    public void setTags(List<String> tags) {
        Tags = tags;
    }

    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
//    toString
    public String toString() {
        return String.format("Habilidades | Id: %-3d | Nome: %-20s | Tags: %-25s | Descrição: %-50s",
                this.id,
                this.nome,
                this.getTags(),
                this.descricao
                );
    }
}
