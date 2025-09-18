package com.example.Model;

/**
 * Representa um usuário com nível de administração
 * Inclui informações como nome, email, senha e um código de acesso
 */
public class Administracao {
    // Atributos
    private int id;
    private String nome;
    private String email;
    private String senha;
    private String codigoAcesso;

    //Métodos Construtores
    /**
     * Vale ressaltar que nos método Setters, acontece as validações de exceções
     * Por esse motivo, os métodos Setters estão sendo usados nos método construtores
     */
    public Administracao (String nome, String email, String senha, String codigoAcesso) {
        this.setNome(nome);
        this.setEmail(email);
        this.setSenha(senha);
        this.setCodigoAcesso(codigoAcesso);
    }

    public Administracao (int id, String nome, String email, String senha, String codigoAcesso){
        this.setId(id);
        this.setNome(nome);
        this.setEmail(email);
        this.setSenha(senha);
        this.setCodigoAcesso(codigoAcesso);
    }

    //Getters e Setters
    public void setId (int id) {
        if (id <= 0) { // Exceção: verifica se o ID é negativo ou igual a zero
            throw new IllegalArgumentException("O ID não pode ser zero ou negativo.");
        }
        this.id = id;
    }
    public int getId (){
        return id;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) { // Exceção: verifica se o nome é nulo ou só contém espaço
            throw new IllegalArgumentException("O nome não pode ser nulo ou vazio.");
        }
        this.nome = nome;
    }
    public String getNome () {
        return nome;
    }

    public void setEmail (String email){
        if (email == null || email.isBlank()) { // Exceção: verificar se o email é válido
            throw new IllegalArgumentException("O e-mail fornecido não é válido.");
        }
        this.email = email;
    }
    public String getEmail () {
        return email;
    }

    public void setSenha (String senha) {
        if (senha == null || senha.isBlank()) { // Exceção: verificar se a senha é nula ou só contém espaço
            throw new IllegalArgumentException("A senha não pode ser nula ou vazia.");
        }
        this.senha = senha;
    }
    public String getSenha () {
        return senha;
    }

    public void setCodigoAcesso(String codigoAcesso) {
        if (codigoAcesso == null || codigoAcesso.isBlank()) { // Exceção: verificar se o código de acesso é nulo ou só contém espaço
            throw new IllegalArgumentException("O código de acesso não pode ser nula ou vazia.");            
        }
        this.codigoAcesso = codigoAcesso;
    }
    public String getCodigoAcesso() {
        return codigoAcesso;
    }

    // Método toString
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
