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

    // Métodos Construtores

    // As validações de exceções são realizadas pelos métodos setters
    public Administracao(String nome, String email, String senha) {
        this.setNome(nome);
        this.setEmail(email);
        this.setSenha(senha);
    }

    public Administracao(int id, String nome, String email, String senha){
        this.setId(id);
        this.setNome(nome);
        this.setEmail(email);
        this.setSenha(senha);
    }

    // Métodos Getters e Setters

    // Para o ID
    public int getId(){
        return id;
    }
    public void setId(int id) {
        if (id <= 0) { // Exceção: verifica se o ID é negativo ou igual a zero
            throw new IllegalArgumentException("O ID não pode ser zero ou negativo.");
        }
        this.id = id;
    }

    // Para o nome
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        if (nome == null) { // Exceção: verifica se o nome é nulo
            throw new NullPointerException("O nome não pode ser nulo.");
        }
        if (nome.trim().isEmpty()) { // Exceção: verifica se o nome só contém espaço
            throw new IllegalArgumentException("O nome não pode estar em branco.");
        }
        this.nome = nome;
    }

    // Para o email
    public String getEmail() {
        return email;
    }
    public void setEmail(String email){
        if (email == null) { // Exceção: verifica se o email é nulo
            throw new NullPointerException("O e-mail não pode ser nulo.");
        }
        validateEmail(email);
        this.email = email;
    }

    // Para a senha
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        if (senha == null) { // Exceção: verifica se a senha é nula
            throw new NullPointerException("A senha não pode ser nula.");
        }
        if (senha.trim().isEmpty()) { // Exceção: verifica se a senha só contém espaço
            throw new IllegalArgumentException("A senha não pode estar em branco.");
        }
        validateSenha(senha);
        this.senha = senha;
    }

    // Método toString
    @Override
    public String toString() {
        return String.format("Administração | Id: %-3d | Nome: %-20s | E-mail: %-20s | Senha: [PROTEGIDA]",
                this.id,
                this.nome,
                this.email
        );
    }

    // Patterns de Regex para as validações de regras complexas

    // Pattern para o email: verifica se tem um formato válido
    private static final Pattern PATTERN_EMAIL = Pattern.compile(
            "^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@" +
                    "(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,63}$"
    );

    // Pattern para a senha: verifica se tem, no mínimo, uma letra minúscula
    private static final Pattern PATTERN_MINUSCULA = Pattern.compile("[a-z]");

    // Pattern para a senha: verifica se tem, no mínimo, uma letra maiúscula
    private static final Pattern PATTERN_MAIUSCULA = Pattern.compile("[A-Z]");

    // Pattern para a senha: verifica se tem, no mínimo, um dígito
    private static final Pattern PATTERN_DIGITO = Pattern.compile("\\d");

    // Pattern para a senha: verifica se tem, no mínimo, um caractere especial
    private static final Pattern PATTERN_ESPECIAL = Pattern.compile("[^A-Za-z0-9]");

    // Métodos de Validação

    /*
     * Verifica se o email é válido
     */
    private void validateEmail(String email) {
        Matcher matcher = PATTERN_EMAIL.matcher(email);
        if (!matcher.matches()) { // Exceção: verifica se o e-mail tem formato válido.
            throw new IllegalArgumentException("O formato do e-mail é inválido: '" + email + "'.");
        }
    }

    /*
     * Verifica se a senha é válida
     * Regras de senha:
     * -Mínimo 8 caracteres
     * -Mínimo 1 letra maiúscula
     * -Mínimo 1 letra minúscula
     * -Mínimo 1 caractere especial
     * -Mínimo 1 número
     */
     private void validateSenha(String senha) {
         if (senha.length() < 8) { // Exceção: verifica se a senha tem no mínimo 8 caracteres
             throw new IllegalArgumentException("A senha deve ter no mínimo 8 caracteres.");
         }
         if (!PATTERN_MINUSCULA.matcher(senha).find()) {
             throw new IllegalArgumentException("A senha deve ter no mínimo 1 letra minúscula.");
         }
         if (!PATTERN_MAIUSCULA.matcher(senha).find()) {
             throw new IllegalArgumentException("A senha deve ter no mínimo 1 letra maiúscula.");
         }
         if (!PATTERN_DIGITO.matcher(senha).find()) {
             throw new IllegalArgumentException("A senha deve ter no mínimo 1 dígito.");
         }
         if (!PATTERN_ESPECIAL.matcher(senha).find()) {
             throw new IllegalArgumentException("A senha deve ter no mínimo 1 caractere especial.");
         }
     }
}
