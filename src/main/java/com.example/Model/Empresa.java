package com.example.Model;

import java.time.LocalTime;
import java.util.List;

public class Empresa {
    private int id;
    private String nome;
    private String cep;
    private String cnpj;
    private String email;
    private String telefone;
    private String porte;
    private Plano planoAssinado;
    private LocalTime horarioAbertura;
    private LocalTime horarioFechamento;
    private String regraDeNegocios;
//    Constructors
    public Empresa(int id, String nome, String cep, String cnpj, String email, String telefone,
                   String porte, Plano planoAssinado, LocalTime horarioAbertura,
                   LocalTime horarioFechamento, String regraDeNegocios) {
        this.id = id;
        this.nome = nome;
        this.cep = cep;
        this.cnpj = cnpj;
        this.email = email;
        this.telefone = telefone;
        this.porte = porte;
        this.planoAssinado = planoAssinado;
        this.horarioAbertura = horarioAbertura;
        this.horarioFechamento = horarioFechamento;
        this.regraDeNegocios = regraDeNegocios;
    }
    public Empresa(String nome, String cep, String cnpj, String email, String telefone, String porte,
                   Plano planoAssinado, LocalTime horarioAbertura, LocalTime horarioFechamento,
                   String regraDeNegocios) {
        this.nome = nome;
        this.cep = cep;
        this.cnpj = cnpj;
        this.email = email;
        this.telefone = telefone;
        this.porte = porte;
        this.planoAssinado = planoAssinado;
        this.horarioAbertura = horarioAbertura;
        this.horarioFechamento = horarioFechamento;
        this.regraDeNegocios = regraDeNegocios;
    }

//    Getters e Setters

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCep() {
        return cep;
    }
    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCnpj() {
        return cnpj;
    }
    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getPorte() {
        return porte;
    }
    public void setPorte(String porte) {
        this.porte = porte;
    }

    public Plano getPlanoAssinado() {
        return planoAssinado;
    }
    public void setPlanoAssinado(Plano planoAssinado) {
        this.planoAssinado = planoAssinado;
    }

    public LocalTime getHorarioAbertura() {
        return horarioAbertura;
    }
    public void setHorarioAbertura(LocalTime horarioAbertura) {
        this.horarioAbertura = horarioAbertura;
    }

    public LocalTime getHorarioFechamento() {
        return horarioFechamento;
    }
    public void setHorarioFechamento(LocalTime horarioFechamento) {
        this.horarioFechamento = horarioFechamento;
    }

    public String getRegraDeNegocios() {
        return regraDeNegocios;
    }
    public void setRegraDeNegocios(String regraDeNegocios) {
        this.regraDeNegocios = regraDeNegocios;
    }
//    toString
    public String toString(){
        return String.format("Empresa | Id: %-3d | Nome: %-20s | Cep: %-9s | Cnpj: %-18s | Email: %-20s | Telefone: %-12s | Porte: %-7s | Plano assinado: %-5s | Horário abertura: %-10s | Horário fechamento: %-10s | Regras de negócios: %-50s",
                this.id,
                this.nome,
                this.cep,
                this.cnpj,
                this.email,
                this.telefone,
                this.porte,
                this.planoAssinado.getNome(),
                this.horarioAbertura,
                this.horarioFechamento,
                this.regraDeNegocios
        );
    }
}