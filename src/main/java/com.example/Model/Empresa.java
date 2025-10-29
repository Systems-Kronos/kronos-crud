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
    private String telefone;
    private String porte;
    private LocalTime horarioAbertura;
    private LocalTime horarioFechamento;
    private String regraDeNegocios;
    private int idPlano;
    private String planoNome;

    // Métodos Construtores

    // As validações de exceções são realizadas pelos métodos setters
    public Empresa(int id, String nome, String cep, String cnpj, String email, String telefone,
                   String porte, LocalTime horarioAbertura,
                   LocalTime horarioFechamento, String regraDeNegocios, int idPlano) {
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
        this.setIdPlano(idPlano);
    }

    public Empresa(String nome, String cep, String cnpj, String email, String telefone,
                   String porte, LocalTime horarioAbertura,
                   LocalTime horarioFechamento, String regraDeNegocios, int idPlano) {
        this.setNome(nome);
        this.setCep(cep);
        this.setCnpj(cnpj);
        this.setEmail(email);
        this.setTelefone(telefone);
        this.setPorte(porte);
        this.setHorarioAbertura(horarioAbertura);
        this.setHorarioFechamento(horarioFechamento);
        this.setRegraDeNegocios(regraDeNegocios);
        this.setIdPlano(idPlano);
    }

    // Métodos Getters e Setters

    // Para o ID
    public int getId() {
        return id;
    }
    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("O ID não pode ser zero ou negativo.");
        }
        this.id = id;
    }

    // Para o nome
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        if (nome == null) {
            throw new NullPointerException("O nome não pode ser nulo.");
        }
        if (nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome não pode estar em branco.");
        }
        this.nome = nome;
    }

    // Para o CEP
    public String getCep() {
        return cep;
    }
    public void setCep(String cep) {
        if (cep == null) {
            throw new NullPointerException("O CEP não pode ser nulo.");
        }
        if (!isValidCep(cep)) {
            throw new IllegalArgumentException("O formato do CEP é inválido: '" + cep + "'.");
        }
        this.cep = cep.replaceAll("[^\\d]", "");
    }

    // Para o CNPJ
    public String getCnpj() {
        return cnpj;
    }
    public void setCnpj(String cnpj) {
        if (cnpj == null) {
            throw new NullPointerException("O CNPJ não pode ser nulo.");
        }
        if (!isValidCnpj(cnpj)) {
            throw new IllegalArgumentException("O formato do CNPJ é inválido: '" + cnpj + "'.");
        }
        this.cnpj = cnpj.replaceAll("[^\\d]", "");
    }

    // Para o email
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        if (email == null) {
            throw new NullPointerException("O email não pode ser nulo.");
        }
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("O formato do e-mail é inválido: '" + email + "'.");
        }
        this.email = email;
    }

    // Para o telefone
    public String getTelefone() {
        return telefone;
    }
    public void setTelefone(String telefone) {
        if (telefone == null) {
            throw new NullPointerException("O telefone não pode ser nulo.");
        }
        if (!isValidTelefone(telefone)) {
            throw new IllegalArgumentException("O formato do telefone é inválido: '" + telefone + "'.");
        }
        this.telefone = telefone.replaceAll("[^\\d]", "");
    }

    // Para o porte
    public String getPorte() {
        return porte;
    }
    public void setPorte(String porte) {
        if (porte == null) {
            throw new NullPointerException("O porte não pode ser nulo.");
        }
        if (porte.trim().isEmpty()) {
            throw new IllegalArgumentException("O porte não pode estar em branco.");
        }
        this.porte = porte;
    }

    // Para o horário de abertura
    public LocalTime getHorarioAbertura() {
        return horarioAbertura;
    }
    public void setHorarioAbertura(LocalTime horarioAbertura) {
        if (horarioAbertura == null) {
            throw new NullPointerException("O horário de abertura não pode ser nulo.");
        }
        if (this.horarioFechamento != null && horarioAbertura.isAfter(this.horarioFechamento)) {
            throw new IllegalStateException("O horário de abertura não pode ser posterior ao de fechamento.");
        }
        this.horarioAbertura = horarioAbertura;
    }

    // Para o horário de fechamento
    public LocalTime getHorarioFechamento() {
        return horarioFechamento;
    }
    public void setHorarioFechamento(LocalTime horarioFechamento) {
        if (horarioFechamento == null) {
            throw new NullPointerException("O horário de fechamento não pode ser nulo.");
        }
        if (this.horarioAbertura != null && horarioFechamento.isBefore(this.horarioAbertura)) {
            throw new IllegalStateException("O horário de fechamento não pode ser anterior ao de abertura.");
        }
        this.horarioFechamento = horarioFechamento;
    }

    // Para as regras de negócios
    public String getRegraDeNegocios() {
        return regraDeNegocios;
    }
    public void setRegraDeNegocios(String regraDeNegocios) {
        if (regraDeNegocios == null) {
            throw new NullPointerException("A regra de negócios não pode ser nula.");
        }
        if (regraDeNegocios.trim().isEmpty()) {
            throw new IllegalArgumentException("A regra de negócios não pode estar vazia.");
        }
        this.regraDeNegocios = regraDeNegocios;
    }

    // Para o ID do plano
    public int getIdPlano() {
        return idPlano;
    }
    public void setIdPlano(int idPlano) {
        if (idPlano <= 0) {
            throw new IllegalArgumentException("O ID do plano não pode ser zero ou negativo.");
        }
        this.idPlano = idPlano;
    }

    // Para o nome do plano
    public String getPlanoNome() {
        return planoNome;
    }
    public void setPlanoNome(String planoNome) {
        if (planoNome == null) {
            throw new NullPointerException("O nome do plano não pode ser nulo.");
        }
        if (planoNome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do plano não pode estar em branco.");
        }
        this.planoNome = planoNome;
    }

    // Método toString
    public String toString() {
        return String.format("Empresa | Id: %-3d | Nome: %-20s | CEP: %-9s | CNPJ: %-18s | Email: %-20s | Telefone: %-12s | Porte: %-7s | Horário Abertura: %-10s | Horário Fechamento: %-10s | Regras de Negócios: %-50s | IdPlano: %-5d",
                this.id,
                this.nome,
                this.cep,
                this.cnpj,
                this.email,
                this.telefone,
                this.porte,
                this.horarioAbertura,
                this.horarioFechamento,
                this.regraDeNegocios,
                this.idPlano
        );
    }

    // Métodos de Validação
    private boolean isValidCep(String cep) {
        String regex = "\\b\\d{5}-?\\d{3}\\b";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cep.trim());
        return matcher.matches();
    }

    private boolean isValidCnpj(String cnpj) {
        String regex = "\\b\\d{2}\\.?\\d{3}\\.?\\d{3}/?\\.?\\d{4}-?\\d{2}\\b";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cnpj.trim());
        return matcher.matches();
    }

    private boolean isValidTelefone(String telefone) {
        String regex = "\\(?\\d{2}\\)?\\s?\\d{4,5}-?\\d{4}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(telefone.trim());
        return matcher.matches();
    }

    private boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*" +
                "@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,63}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
