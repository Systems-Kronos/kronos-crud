package com.example.Model;

/**
 * Representa um setor da indústria.
 * A classe garante a integridade dos dados relacionados ao setor e sua associação
 * com a empresa e seus funcionários.
 */
public class Setor {
    // Atributos
    private int id;
    private String nome;
    private String descricao;
    private String turnos;
    private int qntFuncionarios;
    private Empresa empresa; // Nova associação

    // Métodos Construtores

    // Construtor com ID
    public Setor(int id, String nome, String descricao, String turnos, int qntFuncionarios, Empresa empresa) {
        this.setId(id);
        this.setNome(nome);
        this.setDescricao(descricao);
        this.setTurnos(turnos);
        this.setQntFuncionarios(qntFuncionarios);
        this.setEmpresa(empresa);
    }

    // Construtor sem ID (novo registro)
    public Setor(String nome, String descricao, String turnos, int qntFuncionarios, Empresa empresa) {
        this.setNome(nome);
        this.setDescricao(descricao);
        this.setTurnos(turnos);
        this.setQntFuncionarios(qntFuncionarios);
        this.setEmpresa(empresa);
    }

    // Métodos Getters e Setters

    public int getId() {
        return id;
    }
    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("O ID não pode ser zero ou negativo.");
        }
        this.id = id;
    }

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

    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        if (descricao == null) {
            throw new NullPointerException("A descrição não pode ser nula.");
        }
        if (descricao.trim().isEmpty()) {
            throw new IllegalArgumentException("A descrição não pode ser em branco.");
        }
        this.descricao = descricao;
    }

    public String getTurnos() {
        return turnos;
    }
    public void setTurnos(String turnos) {
        if (turnos == null) {
            throw new NullPointerException("Os turnos não podem ser nulo.");
        }
        if (turnos.trim().isEmpty()) {
            throw new IllegalArgumentException("Os turnos não podem ser em branco.");
        }
        this.turnos = turnos;
    }

    public int getQntFuncionarios() {
        return qntFuncionarios;
    }
    public void setQntFuncionarios(int qntFuncionarios) {
        if (qntFuncionarios < 0) {
            throw new IllegalArgumentException("A quantidade de funcionários não pode ser negativa.");
        }
        this.qntFuncionarios = qntFuncionarios;
    }

    public Empresa getEmpresa() {
        return empresa;
    }
    public void setEmpresa(Empresa empresa) {
        if (empresa == null) {
            throw new NullPointerException("A empresa não pode ser nula.");
        }
        this.empresa = empresa;
    }

    // Método toString
    public String toString() {
        return String.format(
                "Setor | Id: %-3d | Nome: %-20s | Descrição: %-50s | Turnos: %-50s | Qnt. Funcionários: %-5d | ID Empresa: %-3d | Empresa: %-20s",
                this.id,
                this.nome,
                this.descricao,
                this.turnos,
                this.qntFuncionarios,
                this.empresa != null ? this.empresa.getNome() : "Nenhuma"
        );
    }
}
