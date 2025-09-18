package com.example.Model;

import java.util.List;

public class Usuario {
    private int id;
    private Character genero;
    private String nome;
    private String cpf;
    private String senha;
    private String status;
    private Setor setor;
    private List<Habilidades> habilidades;
//    Constructors

    public Usuario(int id, Character genero, String nome, String cpf, String senha, String status, Setor setor, List<Habilidades> habilidades) {
        this.id = id;
        this.genero = genero;
        this.nome = nome;
        this.cpf = cpf;
        this.senha = senha;
        this.status = status;
        this.setor = setor;
        this.habilidades = habilidades;
    }
    public Usuario(Character genero, String nome, String cpf, String senha, String status, Setor setor, List<Habilidades> habilidades) {
        this.genero = genero;
        this.nome = nome;
        this.cpf = cpf;
        this.senha = senha;
        this.status = status;
        this.setor = setor;
        this.habilidades = habilidades;
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

    public Character getGenero() {
        return genero;
    }
    public void setGenero(Character genero) {
        this.genero = genero;
    }

    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public Setor getSetor() {
        return setor;
    }
    public void setSetor(Setor setor) {
        this.setor = setor;
    }

    public List<Habilidades> getHabilidades() {
        return habilidades;
    }
    public void setHabilidades(List<Habilidades> habilidades) {
        this.habilidades = habilidades;
    }
//    toString
    public String toString() {
        return String.format("Usuário | Id: %-3d | Nome: %-20s | Gênero: %-1s | Cpf: %-14s | Senha: %-15s | Status: %-7s | Setor: %-15s | Habilidades: %-30s",
                this.id = id,
                this.nome = nome,
                this.genero = genero,
                this.cpf = cpf,
                this.senha = senha,
                this.status = status,
                this.setor = setor,
                this.habilidades = habilidades
                );
        //Precisa criar o getNome do setor
    }
}
