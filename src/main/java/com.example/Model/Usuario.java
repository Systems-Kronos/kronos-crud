package com.example.Model;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Representa um usuário com detalhes pessoais e profissionais.
 * Esta classe armazena informações como nome, CPF, gênero, credenciais de acesso,
 * setor e a lista de habilidades do usuário, garantindo a integridade dos dados.
 */
public class Usuario {
    // Atributos
    private int id;
    private String nome;
    private String cpf;
    private Character genero;
    private String status;
    private String senha;
    private int idSetor;
    private int idSupervisor;

    // Métodos Construtores

    // As validações de exceções são realizadas pelos métodos set.
    public Usuario(int id, String nome, String cpf,
                   Character genero, String status,
                   String senha, int idSetor, int idSupervisor) {
        this.setId(id);
        this.setNome(nome);
        this.setCpf(cpf);
        this.setGenero(genero);
        this.setStatus(status);
        this.setSenha(senha);
        this.setIdSetor(idSetor);
        this.setIdSupervisor(idSupervisor);
    }

    public Usuario(String nome, String cpf,
                   Character genero, String status,
                   String senha,  int idSetor, int idSupervisor) {
        this.setNome(nome);
        this.setCpf(cpf);
        this.setGenero(genero);
        this.setStatus(status);
        this.setSenha(senha);
        this.setIdSetor(idSetor);
        this.setIdSupervisor(idSupervisor);
    }

    // Métodos Getters e Setters

    // Para o ID
    public int getId() {
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

    // Para o gênero
    public Character getGenero() {
        return genero;
    }
    public void setGenero(Character genero) {
        if (genero == null) { // Exceção: verifica se o gênero é nulo
            throw new NullPointerException("O gênero não pode ser nulo.");
        }
        if (!isValidGender(genero)) { // Exceção: verifica se o gênero é válido pelo método isValidGender
            throw new IllegalArgumentException("O gênero não é válido. Use 'M', 'F', 'O' ou 'N'.");
        }
        this.genero = Character.toUpperCase(genero);
    }

    // Para o CPF
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) {
        if (cpf == null) { // Exceção: verifica se o CPF é nulo
            throw new NullPointerException("O CPF não pode ser nulo.");
        }
        if (!isValidCpf(cpf)) { // Exceção: verifica se o CPF é válido pelo método isValidCpf
            throw new IllegalArgumentException("O formato do CPF é inválido: '" + cpf + "'.");
        }
        this.cpf = cpf.replaceAll("[^\\d]", "");
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
        if (isValidSenha(senha)) { // Exceção: verifica se a senha é válida pelo método isValidSenha
            this.senha = senha;
        }
    }

    // Para o status
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        if (status == null) { // Exceção: verifica se o status é nulo
            throw new NullPointerException("O status não pode ser nulo.");
        }
        if (status.trim().isEmpty()) { // Exceção: verifica se o status só contém espaço
            throw new IllegalArgumentException("O status não pode ser em branco.");
        }
        this.status = status;
    }

    // Para o ID de setor
    public int getIdSetor() {
        return idSetor;
    }
    public void setIdSetor(int id) {
        if (id <= 0) { // Exceção: verifica se o ID do setor é negativo ou igual a zero
            throw new IllegalArgumentException("O ID do setor não pode ser zero ou negativo.");
        }
        this.idSetor = id;
    }

    // Para o ID de supervisor
    public int getIdSupervisor() {
        return idSupervisor;
    }
    public void setIdSupervisor(int id) {
        if (id <= 0) { // Exceção: verifica se o ID do supervisor é negativo ou igual a zero
            throw new IllegalArgumentException("O ID do supervisor não pode ser zero ou negativo.");
        }
        this.idSupervisor = id;
    }

    // Para o método toString
    public String toString() {
        return String.format("Usuário | Id: %-3d | Nome: %-20s | Cpf: %-14s | Gênero: %-1s | Status: %-7s | Senha:[PROTEGIDA] | ID Setor: %-3d | ID Supervisor: %-3d",
                this.id,
                this.nome,
                this.cpf,
                this.genero,
                this.status,
                this.idSetor,
                this.idSupervisor
                );
    }

    // Métodos de Validação

    /*
     * Verifica se o gênero é válido
     * Aceitados:
     * 'M' de masculino, 'F' de feminino, 'O' de outro, 'N' de "prefiro não informar"
    */
    private boolean isValidGender(Character genero) {
        char generoUpper = Character.toUpperCase(genero);
        if (generoUpper == 'M' || generoUpper == 'F' || generoUpper == 'O' || generoUpper == 'N') {
            return true;
        }
        return false;
    }


    /*
     * Verifica se o CPF é válido
     * Exemplos de CPF aceitável:
     * "123.123.123-12", "12312312312"
     */
    private boolean isValidCpf(String cpf) {
        String regex = "^\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cpf.trim());
        return matcher.matches();
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

    private boolean isValidSenha(String senha) {
        if (senha.length() < 8) { // Exceção: verifica se a senha tem no mínimo 8 caracteres
            throw new IllegalArgumentException("A senha deve ter no mínimo 8 caracteres");
        }
        String regex = "[a-z]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(senha);
        if (!matcher.find()) { // Exceção: verifica se a senha tem no mínimo 1 letra minúscula
            throw new IllegalArgumentException("A senha deve ter no mínimo 1 letra minúscula");
        }
        regex = "[A-Z]";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(senha);
        if (!matcher.find()) { // Exceção: verifica se a senha tem no mínimo 1 letra maiúscula
            throw new IllegalArgumentException("A senha deve ter no mínimo 1 letra maiúscula");
        }
        regex = "\\d";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(senha);
        if (!matcher.find()) { // Exceção: verifica se a senha tem no mínimo 1 dígito
            throw new IllegalArgumentException("A senha deve ter no mínimo 1 dígito");
        }
        regex = "[^A-Za-z0-9]";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(senha);
        if (!matcher.find()) { // Exceção: verifica se a senha tem no mínimo 1 caractere especial
            throw new IllegalArgumentException("A senha deve ter no mínimo 1 caractere especial");
        }
        return true;
    }
}