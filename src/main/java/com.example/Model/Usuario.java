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
    private List<Habilidades> habilidades;

    // Construtores
    public Usuario(int id, Character genero, String nome, String cpf, String senha, String status, Setor setor, List<Habilidades> habilidades) {
        this.setId(id);
        this.setGenero(genero);
        this.setNome(nome);
        this.setCpf(cpf);
        this.setSenha(senha);
        this.setStatus(status);
        this.setSetor(setor);
        this.setHabilidades(habilidades);
    }

    public Usuario(Character genero, String nome, String cpf, String senha, String status, Setor setor, List<Habilidades> habilidades) {
        this.setGenero(genero);
        this.setNome(nome);
        this.setCpf(cpf);
        this.setSenha(senha);
        this.setStatus(status);
        this.setSetor(setor);
        this.setHabilidades(habilidades);
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) {
        if (id <= 0) throw new IllegalArgumentException("O ID (" + id + ") não pode ser zero ou negativo.");
        this.id = id;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) throw new IllegalArgumentException("O nome não pode ser nulo ou em branco.");
        this.nome = nome;
    }

    public String getTelefoneFixo() { return telefoneFixo; }
    public String getTelefonePessoal() { return telefonePessoal; }
    public void setTelefone(String telefone) {
        if (!isValidTelefone(telefone)) throw new IllegalArgumentException("O telefone (" + telefone + ") não é válido.");
    }

    public Character getGenero() { return genero; }
    public void setGenero(Character genero) {
        if (!isValidGender(genero)) throw new IllegalArgumentException("O gênero (" + genero + ") não é valido.");
        this.genero = genero;
    }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) {
        if (!isValidCpf(cpf)) throw new IllegalArgumentException("O CPF (" + cpf + ") não é válido.");
        this.cpf = cpf;
    }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getStatus() { return status; }
    public void setStatus(String status) {
        if (status == null || status.trim().isEmpty()) throw new IllegalArgumentException("O status não pode ser nulo ou em branco");
        this.status = status;
    }

    public Setor getSetor() { return setor; }
    public void setSetor(Setor setor) {
        if (setor == null) throw new IllegalArgumentException("O objeto setor não pode ser nulo.");
        this.setor = setor;
    }

    public List<Habilidades> getHabilidades() { return habilidades; }
    public void setHabilidades(List<Habilidades> habilidades) {
        if (habilidades == null || habilidades.isEmpty()) throw new IllegalArgumentException("A lista de habilidades não pode ser nula ou vazia.");
        for (Habilidades habilidade : habilidades) {
            if (habilidade == null) throw new IllegalArgumentException("Um objeto de habilidade não pode ser nulo");
        }
        this.habilidades = habilidades;
    }

    // toString
    public String toString() {
        return String.format(
                "Usuário | Id: %-3d | Nome: %-20s | Telefone Fixo: %-12s | Telefone Pessoal: %-12s | Gênero: %-1s | Cpf: %-14s | Senha: %-15s | Status: %-7s | Setor: %-15s | Habilidades: %-30s",
                this.id,
                this.nome,
                this.telefoneFixo,
                this.telefonePessoal,
                this.genero,
                this.cpf,
                this.senha,
                this.status,
                this.setor,
                this.habilidades
        );
    }

    // Métodos de validação
    private boolean isValidGender(Character genero) {
        if (genero == null) return false;
        genero = Character.toUpperCase(genero);
        return genero == 'M' || genero == 'F' || genero == 'O' || genero == 'N';
    }

    private boolean isValidCpf(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) return false;
        String regex = "\\b\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}\\b";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cpf.trim());
        if (matcher.matches()) {
            this.cpf = cpf.replaceAll("[^\\d]", "");
            return true;
        }
        return false;
    }

    private boolean isValidTelefone(String telefone) {
        if (telefone == null || telefone.trim().isEmpty()) return false;
        String regex = "\\(?\\d{2}\\)?\\d{4,5}-?\\d{4}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(telefone.trim());
        if (matcher.matches()) {
            String telefoneBanco = telefone.replaceAll("[^\\d]", "");
            if (telefone.equals(this.telefoneFixo)) this.telefoneFixo = telefoneBanco;
            else this.telefonePessoal = telefoneBanco;
            return true;
        }
        return false;
    }

    private boolean isValidSenha(String senha) {
        if (senha == null) throw new IllegalArgumentException("A senha não pode ser nula.");
        if (senha.length() < 8) throw new IllegalArgumentException("A senha deve ter no mínimo 8 caracteres.");
        return true;
    }
}