package com.example.Model;

import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Representa uma empresa com informações detalhadas como
 * nome, localização, contatos, horário de funcionamento e plano assinado
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

    // Métodos Construtores

    // As validações de exceções são realizadas pelos métodos set
    public Empresa(int id, String nome, String cep, String cnpj, String email, String telefoneFixo, String telefonePessoal,
                   String porte, LocalTime horarioAbertura,
                   LocalTime horarioFechamento, String regraDeNegocios) {
        this.setId(id);
        this.setNome(nome);
        this.setCep(cep);
        this.setCnpj(cnpj);
        this.setEmail(email);
        this.setTelefoneFixo(telefoneFixo);
        this.setTelefonePessoal(telefonePessoal);
        this.setPorte(porte);
        this.setHorarioAbertura(horarioAbertura);
        this.setHorarioFechamento(horarioFechamento);
        this.setRegraDeNegocios(regraDeNegocios);
    }

    public Empresa(String nome, String cep, String cnpj, String email, String telefoneFixo, String telefonePessoal,
                   String porte, LocalTime horarioAbertura,
                   LocalTime horarioFechamento, String regraDeNegocios) {
        this.setNome(nome);
        this.setCep(cep);
        this.setCnpj(cnpj);
        this.setEmail(email);
        this.setTelefoneFixo(telefoneFixo);
        this.setTelefonePessoal(telefonePessoal);
        this.setPorte(porte);
        this.setHorarioAbertura(horarioAbertura);
        this.setHorarioFechamento(horarioFechamento);
        this.setRegraDeNegocios(regraDeNegocios);
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

    // Para o CEP
    public String getCep() {
        return cep;
    }
    public void setCep(String cep) {
        if (cep == null) { // Exceção: verifica se o CEP é nulo
            throw new NullPointerException("O CEP não pode ser nulo.");
        }
        if (!isValidCep(cep)) { // Exceção: verifica se o CEP é válido pelo método isValidCep
            throw new IllegalArgumentException("O formato do CEP é inválido: '" + cep + "'");
        }
    }

    // Para o CNPJ
    public String getCnpj() {
        return cnpj;
    }
    public void setCnpj(String cnpj) {
        if (cnpj == null) { // Exceção: verifica se o CNPJ é nulo
            throw new NullPointerException("O CNPJ não pode ser nulo.");
        }
        if (!isValidCnpj(cnpj)) { // Exceção: verifica se o CNPJ é válido pelo método isValidCnpj
            throw new IllegalArgumentException("O formato do CNPJ é inválido: '" + cnpj + "'");
        }
    }

    // Para o email
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        if (email == null) { // Exceção: verifica se o email é nulo
            throw new NullPointerException("O email não pode ser nulo.");
        }
        if (!isValidEmail(email)) { // Exceção: verifica se o email é válido pelo método isValidEmail
            throw new IllegalArgumentException("O formato do e-mail é inválido: '" + email + "'");
        }
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

    // Para o porte
    public String getPorte() {
        return porte;
    }
    public void setPorte(String porte) {
        if (porte == null) { // Exceção: verifica se o porte é nulo
            throw new NullPointerException("O porte não pode ser nulo.");
        }
        if (porte.trim().isEmpty()) { // Exceção: verifica se o nome só contém espaço
            throw new IllegalArgumentException("O porte não pode estar em branco.");
        }
        this.porte = porte;
    }

    // Para o horário de abertura
    public LocalTime getHorarioAbertura() {
        return horarioAbertura;
    }
    public void setHorarioAbertura(LocalTime horarioAbertura) {
        if (horarioAbertura == null) { // Exceção: verifica se o horario de abertura é nulo
            throw new NullPointerException("O horário de abertura não pode ser nulo.");
        }
        // Exceção: verifica se o horário de abertura é depois do horário de fechamento
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
        if (horarioFechamento == null) { // Exceção: verifica se o horario de fechamento é nulo
            throw new NullPointerException("O horário de fechamento não pode ser nulo.");
        }
        // Exceção: verifica se o horário de fechamento é antes do horário de abertura
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
        if (regraDeNegocios == null) { // Exceção: verifica se a regra de negócios é nula
            throw new NullPointerException("A regra de negócios não pode ser nula.");
        }
        if (regraDeNegocios.trim().isEmpty()) { // Exceção: verifica se a regra de negócios só contém espaço
            throw new IllegalArgumentException("A regra de negócios não pode estar vazia.");
        }
        this.regraDeNegocios = regraDeNegocios;
    }

    // Método toString
    public String toString() {
        return String.format("Empresa | Id: %-3d | Nome: %-20s | CEP: %-9s | CNPJ: %-18s | Email: %-20s | Telefone Fixo: %-12s | Telefone Pessoal: %-12s | Porte: %-7s | Horário Abertura: %-10s | Horário Fechamento: %-10s | Regras de Negócios: %-50s",
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

    // Métodos de Validação

    /*
     * Verifica se o CEP é válido
     * Exemplos de CEP aceitável:
     * "12345-123", "12345123"
     */
    private boolean isValidCep(String cep) {
        String regex = "\\b\\d{5}-?\\d{3}\\b";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cep.trim());
        if (matcher.matches()) {
            this.cep = cep.replaceAll("[^\\d]", "");
            return true;
        }
        return false;
    }

    /*
     * Verifica se o CNPJ é válido
     * Exemplos de CNPJ aceitável:
     * "12.345.678/1234-56", "12345678123456"
     */
    private boolean isValidCnpj(String cnpj) {
        String regex = "\\b\\d{2}\\.?\\d{3}\\.?\\d{3}/?\\.?\\d{4}-?\\d{2}\\b";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cnpj.trim());
        if (matcher.matches()) {
            this.cnpj = cnpj.replaceAll("[^\\d]", "");
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
}