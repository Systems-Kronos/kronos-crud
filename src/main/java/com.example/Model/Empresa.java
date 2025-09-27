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
    public Empresa(String nome, String cep, String cnpj, String email, String telefone, String porte, LocalTime horarioAbertura, LocalTime horarioFechamento,
                   String regraDeNegocios) {
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

// Métodos Getters e Setters

// Para o ID
    public int getId() {
        return id;
    }
    public void setId(int id) {
        if (id <= 0) { // Exceção: verifica se o ID é negativo ou igual a zero
            throw new IllegalArgumentException("O ID (" + id + ") não pode ser zero ou negativo.");
        }
        this.id = id;
    }

// Para o nome
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) { // Exceção: verifica se o nome é nulo ou só contém espaço
            throw new IllegalArgumentException("O nome não pode ser nulo ou em branco.");
        }
        this.nome = nome;
    }

// Para o CEP
    public String getCep() {
        return cep;
    }
    public void setCep(String cep) {
        if (!isValidCep(cep)) { // Exceção: usa o método isValidCep
            throw new IllegalArgumentException("O CEP (" + cep + ") não é válido.");
        }
        this.cep = cep;
    }

// Para o CNPJ
    public String getCnpj() {
        return cnpj;
    }
    public void setCnpj(String cnpj) {
        if (!isValidCnpj(cnpj)) { // Exceção: usa o método isValidCnpj
            throw new IllegalArgumentException("O CNPJ (" + cnpj + ") não é válido.");
        }
        this.cnpj = cnpj;
    }

// Para o email
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

// Para o telefone fixo e pessoal
    public String getTelefoneFixo() {
        return telefoneFixo;
    }
    public String getTelefonePessoal() {
        return telefonePessoal;
    }
    public void setTelefone(String telefone) {
        if (!isValidTelefone(telefone)) { // Exceção: usa o método isValidTelefone
            throw new IllegalArgumentException("O telefone (" + telefone + ") não é válido.");
        }
    }

// Para o porte
    public String getPorte() {
        return porte;
    }
    public void setPorte(String porte) {
        if (porte == null || porte.trim().isEmpty()) { // Exceção: verifica se o porte é nulo ou só contém espaço
            throw new IllegalArgumentException("O porte não pode ser nulo ou em branco.");
        }
        this.porte = porte;
    }

// Para o horário de abertura
    public LocalTime getHorarioAbertura() {
        return horarioAbertura;
    }
    public void setHorarioAbertura(LocalTime horarioAbertura) {
        if (horarioAbertura == null) { // Exceção: verifica se o horário de abertura é nulo
            throw new IllegalArgumentException("O horário de abertura não pode ser nulo.");
        }
        this.horarioAbertura = horarioAbertura;
    }

// Para o horário de fechamento
    public LocalTime getHorarioFechamento() {
        return horarioFechamento;
    }
    public void setHorarioFechamento(LocalTime horarioFechamento) {
        if (horarioFechamento == null) { // Exceção: verifica se o horário de fechamento é nulo
            throw new IllegalArgumentException("O horário de fechamento (" + horarioFechamento + ") não pode ser nulo.");
        } 
        else if (horarioFechamento.isBefore(horarioAbertura)) { // Exceção: verifica se o horário de fechamento é anterior ou igual ao de abertura
            throw new IllegalArgumentException("O horário de fechamento (" + horarioFechamento + ") deve ser posterior ao de abertura (" + horarioAbertura + ").");
        }
        this.horarioFechamento = horarioFechamento;
    }

// Para as regras de negócios
    public String getRegraDeNegocios() {
        return regraDeNegocios;
    }
    public void setRegraDeNegocios(String regraDeNegocios) {
        if (regraDeNegocios == null || regraDeNegocios.trim().isEmpty()) { // Exceção: verifica se a regra de negócios é nula ou vazia
            throw new IllegalArgumentException("A regra de negócios não pode ser nula ou vazia.");
        }
        this.regraDeNegocios = regraDeNegocios;
    }

// Método toString
    public String toString(){
        return String.format("Empresa | Id: %-3d | Nome: %-20s | CEP: %-9s | CNPJ: %-18s | Email: %-20s | Telefone Fixo: %-12s | Telefone Pessoal: %-12s | Porte: %-7s | Plano Assinado: %-5s | Horário Abertura: %-10s | Horário Fechamento: %-10s | Regras de Negócios: %-50s",
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

/**
* Verifica se o CEP é válido
* Exemplos de CEP aceitável:
* "12345-123", "12345123"
*/
    private boolean isValidCep(String cep) {
        if (cep == null || cep.trim().isEmpty()) { // Exceção: verifica se o CEP é nulo ou só contém espaços
            return false;
        }
        String regex = "\\b\\d{5}-?\\d{3}\\b";
        boolean valido = cep.trim().matches(regex);
        if (valido) {
            String cepBanco = cep.replace("[^\\d]", "");
            this.cep = cepBanco;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cep.trim());
        return matcher.matches();
    }


/**
 * Verifica se o CNPJ é válido
 * Exemplos de CNPJ aceitável:
 * "12.345.678/1234-56", "12345678123456"
 */
    private boolean isValidCnpj(String cnpj) {
        if (cnpj == null || cnpj.trim().isEmpty()) { // Exceção: verifica se o CNPJ é nulo ou só contém espaços
            return false;
        }
        String regex = "\\b\\d{2}\\.?\\d{3}\\.?\\d{3}/?\\.?\\d{4}-?\\d{2}\\b";
        boolean valido = cnpj.trim().matches(regex);
        if (valido) {
            String cnpjBanco = cnpj.replace("[^\\d]", "");
            this.cnpj = cnpjBanco;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cnpj.trim());
        return matcher.matches();    }

/**
 * Verifica se o telefone é válido
 * Exemplos de telefone aceitável:
 * "(11) 12345-1234", "11123451234"
 */
    private boolean isValidTelefone(String telefone) {
        if (telefone == null || telefone.trim().isEmpty()) {  // Exceção: verifica se o telefone é nulo ou só contém espaços
            return false;
        }
        String regex = "\\(?\\d{2}\\)?\\d{4,5}-?\\d{4}";
        boolean valido = telefone.trim().matches(regex);
        if (valido) {
            String telefoneBanco = telefone.replace("[^\\d]", "");
            if (telefone.equals(this.telefoneFixo)) {
                this.telefoneFixo = telefoneBanco;
            } else {
                this.telefonePessoal = telefoneBanco;
            }
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(telefone.trim());
        return matcher.matches();
    }
}