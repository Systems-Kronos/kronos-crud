package com.example.Model;

import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Representa uma empresa com informações detalhadas como
 * nome, localização, contatos, horário de funcionamento e plano assinado.
 * A classe garante a integridade dos dados da empresa através de validações rigorosas.
 */
public class Empresa {
    // Atributos
    private int id;
    private String nome;
    private String cep;
    private String cnpj;
    private String email;
    private String telefoneFixo;
    private String telefonePessoal;
    private String porte;
    private LocalTime horarioAbertura;
    private LocalTime horarioFechamento;
    private String regraDeNegocios;

    // Construtores
    public Empresa(int id, String nome, String cep, String cnpj, String email, String telefone,
                   String porte, LocalTime horarioAbertura,
                   LocalTime horarioFechamento, String regraDeNegocios) {
        try {
            this.setId(id);
            this.setNome(nome);
            this.setCep(cep);
            this.setCnpj(cnpj);
            this.setEmail(email);
            this.setTelefone(telefone);
            this.setPorte(porte);
            this.setHorarioAbertura(horarioAbertura);
            this.setHorarioFechamento(horarioFechamento);
            this.setRegraDeNegocios(regraDeNegocios);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public Empresa(String nome, String cep, String cnpj, String email, String telefone,
                   String porte, LocalTime horarioAbertura,
                   LocalTime horarioFechamento, String regraDeNegocios) {
        try {
            this.setNome(nome);
            this.setCep(cep);
            this.setCnpj(cnpj);
            this.setEmail(email);
            this.setTelefone(telefone);
            this.setPorte(porte);
            this.setHorarioAbertura(horarioAbertura);
            this.setHorarioFechamento(horarioFechamento);
            this.setRegraDeNegocios(regraDeNegocios);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    // Getters e Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("O ID (" + id + ") não pode ser zero ou negativo.");
        }
        this.id = id;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome não pode ser nulo ou em branco.");
        }
        this.nome = nome;
    }

    public String getCep() {
        return cep;
    }
    public void setCep(String cep) {
        if (!isValidCep(cep)) {
            throw new IllegalArgumentException("O CEP (" + cep + ") não é válido.");
        }
        this.cep = cep.replaceAll("[^\\d]", "");
    }

    public String getCnpj() {
        return cnpj;
    }
    public void setCnpj(String cnpj) {
        if (!isValidCnpj(cnpj)) {
            throw new IllegalArgumentException("O CNPJ (" + cnpj + ") não é válido.");
        }
        this.cnpj = cnpj.replaceAll("[^\\d]", "");
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("O email não pode ser nulo ou em branco.");
        }
        this.email = email;
    }

    public String getTelefoneFixo() {
        return telefoneFixo;
    }
    public String getTelefonePessoal() {
        return telefonePessoal;
    }
    public void setTelefone(String telefone) {
        if (!isValidTelefone(telefone)) {
            throw new IllegalArgumentException("O telefone (" + telefone + ") não é válido.");
        }
        String telBanco = telefone.replaceAll("[^\\d]", "");
        if (telefoneFixo == null) {
            this.telefoneFixo = telBanco;
        } else {
            this.telefonePessoal = telBanco;
        }
    }

    public String getPorte() {
        return porte;
    }
    public void setPorte(String porte) {
        if (porte == null || porte.trim().isEmpty()) {
            throw new IllegalArgumentException("O porte não pode ser nulo ou em branco.");
        }
        this.porte = porte;
    }

    public LocalTime getHorarioAbertura() {
        return horarioAbertura;
    }
    public void setHorarioAbertura(LocalTime horarioAbertura) {
        if (horarioAbertura == null) {
            throw new IllegalArgumentException("O horário de abertura não pode ser nulo.");
        }
        this.horarioAbertura = horarioAbertura;
    }

    public LocalTime getHorarioFechamento() {
        return horarioFechamento;
    }
    public void setHorarioFechamento(LocalTime horarioFechamento) {
        if (horarioFechamento == null) {
            throw new IllegalArgumentException("O horário de fechamento não pode ser nulo.");
        } else if (horarioFechamento.isBefore(horarioAbertura)) {
            throw new IllegalArgumentException("O horário de fechamento deve ser posterior ao de abertura.");
        }
        this.horarioFechamento = horarioFechamento;
    }

    public String getRegraDeNegocios() {
        return regraDeNegocios;
    }
    public void setRegraDeNegocios(String regraDeNegocios) {
        if (regraDeNegocios == null || regraDeNegocios.trim().isEmpty()) {
            throw new IllegalArgumentException("A regra de negócios não pode ser nula ou vazia.");
        }
        this.regraDeNegocios = regraDeNegocios;
    }

    @Override
    public String toString() {
        return String.format(
                "Empresa | Id: %-3d | Nome: %-20s | CEP: %-9s | CNPJ: %-18s | Email: %-20s | Telefone Fixo: %-12s | Telefone Pessoal: %-12s | Porte: %-7s | Horário Abertura: %-10s | Horário Fechamento: %-10s | Regras de Negócios: %-50s",
                this.id,
                this.nome,
                this.cep,
                this.cnpj,
                this.email,
                this.telefoneFixo,
                this.telefonePessoal,
                this.porte,
                this.horarioAbertura,
                this.horarioFechamento,
                this.regraDeNegocios
        );
    }

    // Validações
    private boolean isValidCep(String cep) {
        if (cep == null || cep.trim().isEmpty()) return false;
        String regex = "\\b\\d{5}-?\\d{3}\\b";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cep.trim());
        return matcher.matches();
    }

    private boolean isValidCnpj(String cnpj) {
        if (cnpj == null || cnpj.trim().isEmpty()) return false;
        String regex = "\\b\\d{2}\\.?\\d{3}\\.?\\d{3}/?\\d{4}-?\\d{2}\\b";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cnpj.trim());
        return matcher.matches();
    }

    private boolean isValidTelefone(String telefone) {
        if (telefone == null || telefone.trim().isEmpty()) return false;
        String regex = "\\(?\\d{2}\\)?\\d{4,5}-?\\d{4}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(telefone.trim());
        return matcher.matches();
    }
}