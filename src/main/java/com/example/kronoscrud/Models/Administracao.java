package com.example.kronoscrud.Models;

public class Administracao {
    private int id;
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
    public Administracao (int id, String nome, String email, String senha, String codigoAcesso){
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.codigoAcesso = codigoAcesso;
    }
    //Getters e Setters
    public void setId (int id) {
        this.id = id;
    }
    public int getId (){
        return id;
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
    // toString
    public String toString () {
        return String.format("Administração | Id: %-3d | Nome: %-20s | E-mail: %-20s | Senha: %-25s | Código de acesso: %-10s",
                this.id,
                this.nome,
                this.email,
                this.senha,
                this.codigoAcesso
        );
    }
}
