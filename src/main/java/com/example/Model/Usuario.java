package com.example.Model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
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
    private String telefone;
    private Character genero;
    private String status;
    private String senha;
    private int idSetor;
    private int idSupervisor;
    private String cargo;
    private LinkedList<Habilidades> listaHabilidades = new LinkedList<>();

    // Métodos Construtores

    // As validações de exceções são realizadas pelos métodos setters
    public Usuario(int id, String nome, String cpf, String telefone,
                   Character genero, String status,
                   String senha, int idSetor, int idSupervisor,
                   String cargo) {
        this.setId(id);
        this.setNome(nome);
        this.setCpf(cpf);
        this.setTelefone(telefone);
        this.setGenero(genero);
        this.setStatus(status);
        this.setSenha(senha);
        this.setIdSetor(idSetor);
        this.setIdSupervisor(idSupervisor);
        this.setCargo(cargo);
    }

    public Usuario(String nome, String cpf, String telefone,
                   Character genero, String status,
                   String senha, int idSetor, int idSupervisor,
                   String cargo) {
        this.setNome(nome);
        this.setCpf(cpf);
        this.setTelefone(telefone);
        this.setGenero(genero);
        this.setStatus(status);
        this.setSenha(senha);
        this.setIdSetor(idSetor);
        this.setIdSupervisor(idSupervisor);
        this.setCargo(cargo);
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
        if (genero == null) {
            throw new NullPointerException("O gênero não pode ser nulo.");
        }
        validateGenero(genero); // Chama método que lança exceção
        this.genero = Character.toUpperCase(genero);
    }

    // Para o CPF
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) {
        if (cpf == null) {
            throw new NullPointerException("O CPF não pode ser nulo.");
        }
        String cpfLimpo = cpf.replaceAll("[^\\d]", ""); // <-- CORREÇÃO: Limpa ANTES
        validateCpf(cpfLimpo); // <-- CORREÇÃO: Valida o limpo
        this.cpf = cpfLimpo;
    }

    // Para o telefone
    public String getTelefone() {
        return telefone;
    }
    public void setTelefone(String telefone) {
        if (telefone == null) {
            throw new NullPointerException("O telefone não pode ser nulo.");
        }
        String telefoneLimpo = telefone.replaceAll("[^\\d]", "");
        validateTelefone(telefoneLimpo);
        this.telefone = telefoneLimpo;
    }

    // Para a senha
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        if (senha == null) {
            throw new NullPointerException("A senha do usuário não pode ser nula.");
        }
        if (senha.trim().isEmpty()) {
            throw new IllegalArgumentException("A senha do usuário não pode estar em branco.");
        }
        validateSenha(senha);
        this.senha = senha;
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
        if (id < 0) { // Exceção: verifica se o ID do supervisor é negativo ou igual a zero
            throw new IllegalArgumentException("O ID do supervisor não pode ser zero ou negativo.");
        }
        this.idSupervisor = id;
    }

    // Para o cargo
    public String getCargo() {
        return cargo;
    }
    public void setCargo(String cargo) {
        if (cargo == null) { // Exceção: verifica se o cargo é nulo
            throw new NullPointerException("O cargo não pode ser nulo.");
        }
        if (cargo.trim().isEmpty()) { // Exceção: verifica se o cargo só contém espaço
            throw new IllegalArgumentException("O cargo não pode estar em branco.");
        }
        this.cargo = cargo;
    }

    // Métodos para a lista de habilidades
    public List<Habilidades> getListaHabilidades() {
        return Collections.unmodifiableList(this.listaHabilidades);
    }
    public void adicionarHabilidade(Habilidades habilidade) {
        if (habilidade == null) { // Exceção: verifica se o objeto de habilidade é nulo
            throw new NullPointerException("Não é possível adicionar um objeto de habilidade nulo.");
        }
        if (this.listaHabilidades.contains(habilidade)) { // Exceção: verifica se o objeto já existe na lista
            throw new IllegalArgumentException("Não é permitido ter duas habilidades iguais na lista.");
        }
        this.listaHabilidades.add(habilidade);
    }
    public boolean removerHabilidade(Habilidades habilidade) {
        if (habilidade == null) { // Exceção: verifica se o objeto de habilidade é nulo
            throw new NullPointerException("Não é possível remover uma habilidade nula.");
        }
        return this.listaHabilidades.remove(habilidade); // Retorna se a habilidade foi removida ou não
    }

    // Para o método toString
    public String toString() {
        return String.format("Usuário | Id: %-3d | Nome: %-20s | Cpf: %-14s | Telefone: %-12s | Gênero: %-1s | Status: %-7s | Senha:[PROTEGIDA] | ID Setor: %-3d | ID Supervisor: %-3d | Cargo: %-20s",
                this.id,
                this.nome,
                this.cpf,
                this.genero,
                this.status,
                this.idSetor,
                this.idSupervisor,
                this.cargo
                );
    }
    // Patterns de Regex para as validações de regras complexas

    // Pattern para a senha: verifica se tem, no mínimo, uma letra minúscula
    private static final Pattern PATTERN_MINUSCULA = Pattern.compile("[a-z]");
    // Pattern para a senha: verifica se tem, no mínimo, uma letra maiúscula
    private static final Pattern PATTERN_MAIUSCULA = Pattern.compile("[A-Z]");
    // Pattern para a senha: verifica se tem, no mínimo, um dígito
    private static final Pattern PATTERN_DIGITO = Pattern.compile("\\d");
    // Pattern para a senha: verifica se tem, no mínimo, um caractere especial
    private static final Pattern PATTERN_ESPECIAL = Pattern.compile("[^\\sA-Za-z0-9]"); // Ajustado para aceitar espaço se necessário, senão use [^A-Za-z0-9]

    // Métodos de Validação

    /*
     * Verifica se o gênero é válido
     * Aceitados:
     * 'M' de masculino, 'F' de feminino, 'O' de outro, 'N' de "prefiro não informar"
    */
    private void validateGenero(Character genero) {
        char generoUpper = Character.toUpperCase(genero);
        if (!(generoUpper == 'M' || generoUpper == 'F' || generoUpper == 'O' || generoUpper == 'N')) {
            throw new IllegalArgumentException("Gênero inválido. Use 'M', 'F', 'O' ou 'N'. Encontrado: '" + genero + "'.");
        }
    }


    /*
     * Verifica se o CPF é válido
     */
    private void validateCpf(String cpfLimpo) {
        if (cpfLimpo.length() != 11) {
            throw new IllegalArgumentException("CPF inválido. Deve conter 11 dígitos (após remover formatação). Recebido: '" + cpfLimpo + "'.");
        }
    }

    /*
     * Verifica se o telefone é válido
     */
    private void validateTelefone(String telefoneLimpo) {
        int len = telefoneLimpo.length();
        if (len != 10 && len != 11) {
            throw new IllegalArgumentException("Telefone inválido. Deve conter 10 ou 11 dígitos (com DDD). Recebido: '" + telefoneLimpo + "'");
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
        if (senha.length() < 8) {
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