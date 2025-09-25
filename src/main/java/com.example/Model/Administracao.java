package com.example.Model;

/**
 * Representa um usuário com o cargo de administração.
 * Esta classe armazena informações essenciais de um administrador,
 * incluindo credenciais de acesso e dados pessoais, 
 * garantindo a integridade dos dados através de validações.
 */
public class Administracao {
// Atributos
    private int id;
    private String nome;
    private String email;
    private String senha;
    private String codigoAcesso;

// Métodos Construtores

/**
 * Vale ressaltar que nos métodos setters, acontece as validações de exceções
 * Por esse motivo, os métodos setters estão sendo usados nos métodos construtores
 */
    public Administracao (String nome, String email, String senha, String codigoAcesso) {
        try {
            this.setNome(nome);
            this.setEmail(email);
            this.setSenha(senha);
            this.setCodigoAcesso(codigoAcesso);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public Administracao (int id, String nome, String email, String senha, String codigoAcesso){
        try {
            this.setId(id);
            this.setNome(nome);
            this.setEmail(email);
            this.setSenha(senha);
            this.setCodigoAcesso(codigoAcesso);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        
    }

// Métodos Getters e Setters

// Para o ID
    public void setId (int id) {
        if (id <= 0) { // Exceção: verifica se o ID é negativo ou igual a zero.
            throw new IllegalArgumentException("O ID (" + id + ") não pode ser zero ou negativo.");
        }
        this.id = id;
    }
    public int getId (){
        return id;
    }

// Para o nome
    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) { // Exceção: verifica se o nome é nulo ou só contém espaço
            throw new IllegalArgumentException("O nome não pode ser nulo ou vazio.");
        }
        this.nome = nome;
    }
    public String getNome () {
        return nome;
    }

// Para o email
    public void setEmail (String email){
        if (email == null || email.trim().isEmpty()) { // Exceção: verificar se o email é válido
            throw new IllegalArgumentException("O e-mail (" + email + ") fornecido não é válido.");
        }
        this.email = email;
    }
    public String getEmail () {
        return email;
    }

// Para a senha
    public void setSenha (String senha) {
        if (senha == null || senha.trim().isEmpty()) { // Exceção: verificar se a senha é nula ou só contém espaço
            throw new IllegalArgumentException("A senha (" + senha + ") não pode ser nula ou vazia.");
        }
        this.senha = senha;
    }
    public String getSenha () {
        return senha;
    }

// Para o código de acesso
    public void setCodigoAcesso(String codigoAcesso) {
        if (codigoAcesso == null || codigoAcesso.trim().isEmpty()) { // Exceção: verificar se o código de acesso é nulo ou só contém espaço
            throw new IllegalArgumentException("O código de acesso não pode ser nulo ou vazio.");            
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
