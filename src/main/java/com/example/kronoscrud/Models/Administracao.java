package com.example.kronoscrud.Models;

public class Administracao {
    private int ID;
    private String nome;
    private String email;
    private String senha;
    private String codigoAcesso;

    // Constructors
    public Administracao (String nome, String email, String senha, String codigoAcesso) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.codigoAcesso = codigoAcesso;
    }
    public Administracao (int ID, String nome, String email, String senha, String codigoAcesso){
        this.ID = ID;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.codigoAcesso = codigoAcesso;
    }
    //Getters e Setters
    public void setID (int ID) {
        this.ID = ID;
    }
    public int getID (){
        return ID;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getNome () {
        return nome;
    }

    public void setEmail (String email){
        this.email = email;
    }
    public String getEmail () {
        return email;
    }

    public void setSenha (String senha) {
        this.senha = senha;
    }
    public String getSenha () {
        return senha;
    }

    public void getCodigoAcesso (String codigoAcesso) {
        this.codigoAcesso = codigoAcesso;
    }
    public String setCodigoAcesso () {
        return codigoAcesso;
    }
}
