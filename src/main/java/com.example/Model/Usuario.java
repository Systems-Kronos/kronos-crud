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
    private Character genero;
    private String nome;
    private String telefoneFixo;
    private String telefonePessoal;
    private String cpf;
    private String senha;
    private String status;
    private Setor setor;
    private int idSetor;

    // Métodos Construtores

    // As validações de exceções são realizadas pelos métodos set.
    public Usuario(int id, Character genero, String nome, String telefoneFixo,
                   String telefonePessoal, String cpf, String senha, String status,
                   Setor setor) {
        this.setId(id);
        this.setGenero(genero);
        this.setNome(nome);
        this.setTelefoneFixo(telefoneFixo);
        this.setTelefonePessoal(telefonePessoal);
        this.setCpf(cpf);
        this.setSenha(senha);
        this.setStatus(status);
        this.setSetor(setor);
    }

    public Usuario(Character genero, String nome, String telefoneFixo,
                   String telefonePessoal, String cpf, String senha, String status,
                   Setor setor) {
        this.setGenero(genero);
        this.setNome(nome);
        this.setTelefoneFixo(telefoneFixo);
        this.setTelefonePessoal(telefonePessoal);
        this.setCpf(cpf);
        this.setSenha(senha);
        this.setStatus(status);
        this.setSetor(setor);
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

    // Para o telefone fixo
    public String getTelefoneFixo() {
        return telefoneFixo;
    }
    public void setTelefoneFixo(String telefoneFixo) {
        if (telefoneFixo == null) { // Exceção: verifica se o telefone fixo é nulo
            throw new NullPointerException("O telefone fixo não pode ser nulo.");
        }
        if (!isValidTelefoneFixo(telefoneFixo)) { // Exceção: verifica se o telefone fixo é válido
            throw new IllegalArgumentException("O formato do telefone fixo é inválido: '" + telefoneFixo + "'");
        }
    }

    // Para o telefone pessoal
    public String getTelefonePessoal() {
        return telefonePessoal;
    }
    public void setTelefonePessoal(String telefonePessoal) {
        if (telefonePessoal == null) { // Exceção: verifica se o telefone pessoal é nulo
            throw new NullPointerException("O telefone pessoal não pode ser nulo.");
        }
        if (!isValidTelefonePessoal(telefonePessoal)) { // Exceção: verifica se o telefone pessoal é válido
            throw new IllegalArgumentException("O formato do telefone pessoal é inválido: '" + telefonePessoal + "'");
        }
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
    }

    // Para o CPF
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) {
        if (cpf == null) { // Exceção: verifica se o CPF é nulo
            throw new NullPointerException("O CPF não pode ser nulo.");
        }
        if (!isValidCpf(cpf)) { // Exceção: verifica se o CPF é válido pelo método isValidCpf
            throw new IllegalArgumentException("O formato do CPF é inválido: '" + cpf + "'");
        }
    }

    // Para a senha
    public String getSenha() {
        return senha;
    }
    public void setSenha (String senha) {
        if (senha == null) { // Exceção: verifica se a senha é nula
            throw new NullPointerException("A senha não pode ser nula.");
        }
        if (senha.trim().isEmpty()) { // Exceção: verifica se a senha só contém espaço
            throw new IllegalArgumentException("A senha não pode estar em branco");
        }
        /** if (!isValidSenha(senha)) { // Exceção: verifica se a senha é válida pelo método isValidSenha
         *   throw new IllegalArgumentException("O formato da senha é inválida: '" + senha + "'");
         *}
         */
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

    // Para o setor
    public Setor getSetor() {
        return setor;
    }
    public void setSetor(Setor setor) {
        if (setor == null) { // Exceção: verifica se o objeto setor é nula.
            throw new NullPointerException("O objeto setor não pode ser nula.");
        }
        this.setor = setor;
        this.idSetor = setor.getId(); // Define o atributo idSetor, que serve como FK no banco de dados
    }

    // Para id do setor
    public int getIdSetor() {
        return idSetor;
    }

    // Para o método toString
    public String toString() {
        return String.format("Usuário | Id: %-3d | Nome: %-20s | Telefone Fixo: %-12s | Telefone Pessoal: %-12s | Gênero: %-1s | Cpf: %-14s | Senha:[PROTEGIDO] | Status: %-7s | Setor: %-15s | ID Setor: %-3d",
                this.id,
                this.nome,
                this.telefoneFixo,
                this.telefonePessoal,
                this.genero,
                this.cpf,
                this.status,
                this.setor,
                this.idSetor
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
            this.genero = generoUpper;
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
        if (matcher.matches()) {
            this.cpf = cpf.replaceAll("[^\\d]", "");
            return true;
        }
        return false;
    }


    /*
     * Verifica se o telefone fixo é válido
     * Exemplos de telefone fixo aceitável:
     * "(11) 12345-1234", "11123451234"
     */
    private boolean isValidTelefoneFixo(String telefone) {
        String regex = "\\(?\\d{2}\\)?\\d{4,5}-?\\d{4}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(telefone.trim());
        if (matcher.matches()) {
            this.telefoneFixo = telefone.replaceAll("[^\\d]", "");
            return true;
        }
        return false;
    }

    /*
     * Verifica se o telefone pessoal é válido
     * Exemplos de telefone pessoal aceitável:
     * "(11) 12345-1234", "11123451234"
     */
    private boolean isValidTelefonePessoal(String telefone) {
        String regex = "\\(?\\d{2}\\)?\\d{4,5}-?\\d{4}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(telefone.trim());
        if (matcher.matches()) {
            this.telefonePessoal = telefone.replaceAll("[^\\d]", "");
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