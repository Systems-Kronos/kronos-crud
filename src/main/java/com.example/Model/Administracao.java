package com.example.Model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /*
     * Vale ressaltar que nos métodos setters, acontece as validações de exceções
     * Por esse motivo, os métodos setters estão sendo usados nos métodos construtores
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

    // Métodos Getters e Setters

    // Para o ID
    public void setId(int id) {
        if (id <= 0) { // Exceção: verifica se o ID é negativo ou igual a zero
            throw new IllegalArgumentException("O ID não pode ser zero ou negativo.");
        }
        this.id = id;
    }
    public int getId (){
        return id;
    }

    // Para o nome
    public void setNome(String nome) {
        if (nome == null) { // Exceção: verifica se o nome é nulo
            throw new NullPointerException("O nome não pode ser nulo.");
        }
        if (nome.trim().isEmpty()) { // Exceção: verifica se o nome só contém espaço
            throw new IllegalArgumentException("O nome não pode estar em branco.");
        }
        this.nome = nome;
    }
    public String getNome () {
        return nome;
    }

    // Para o email
    public void setEmail (String email){
        if (email == null) { // Exceção: verifica se o email é nulo
            throw new NullPointerException("O e-mail não pode ser nulo.");
        }
        if (!isValidEmail(email)) { // Exceção: verifica se o email é válido pelo método isValidEmail
            throw new IllegalArgumentException("O formato do e-mail é inválido: '" + email + "'");
        }
    }
    public String getEmail () {
        return email;
    }

    // Para a senha
    public void setSenha (String senha) {
        if (senha == null) { // Exceção: verifica se a senha é nula
            throw new NullPointerException("A senha não pode ser nula.");
        }
        /** if (!isValidSenha(senha)) { // Exceção: verifica se a senha é válida pelo método isValidSenha
         *   throw new IllegalArgumentException("O formato da senha é inválida: '" + senha + "'");
         *}
         */
    }
    public String getSenha () {
        return senha;
    }

    // Para o código de acesso
    public void setCodigoAcesso(String codigoAcesso) {
        if (codigoAcesso == null) { // Exceção: verifica se o código de acesso é nulo
            throw new NullPointerException("O código de acesso não pode ser nulo.");
        }
        if (codigoAcesso.trim().isEmpty()) { // Exceção: verifica se o código de acesso só contém espaço
            throw new IllegalArgumentException("O código de acesso não pode estar em branco");
        }
        this.codigoAcesso = codigoAcesso;
    }
    public String getCodigoAcesso() {
        return codigoAcesso;
    }

    // Método toString
    public String toString () {
        return String.format("Administração | Id: %-3d | Nome: %-20s | E-mail: %-20s | Senha: [PROTEGIDO] | Código de acesso: %-10s",
                this.id,
                this.nome,
                this.email,
                this.codigoAcesso
        );
    }

    // Métodos de Validação

    /* ---------PLACEHOLDER---------
     * Verifica se o email é válido
     * Exemplos de email aceitável:
     *
     */
    private boolean isValidEmail(String email) {
        String regex = "";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            this.email = email;
            return true;
        }
        return false;
    }

    /* ---------PLACEHOLDER---------
     * Verifica se a senha é válida
     * Regras de senha:
     * -Mínimo 8 caracteres
     * -Mínimo 1 letra maiúscula
     * -Mínimo 1 letra minúscula
     * -Mínimo 1 caractere especial
     * -Mínimo 1 número
     */

    /**
     * private boolean isValidSenha(String senha) {
     *
     * }
     */
}
