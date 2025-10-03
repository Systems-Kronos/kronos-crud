package com.example.Model;

/**
 * Representa um setor da indústria.
 * A classe garante a integridade dos dados relacionados ao setor e sua associação
 * com a empresa e seus funcionários.
 */
public class Setor {
    // Atributos
    private int id;
    private Empresa empresa;
    private String nome;
    private String descricao;
    private String turnos;
    private int qntFuncionarios;
    private int idEmpresa;

    // Métodos Construtores

    // As validações de exceções são realizadas pelos métodos set.
    public Setor(int id, Empresa empresa, String nome, String descricao, String turnos, int qntFuncionarios) {
        this.setId(id);
        this.setEmpresa(empresa);
        this.setNome(nome);
        this.setDescricao(descricao);
        this.setTurnos(turnos);
        this.setQntFuncionarios(qntFuncionarios);
    }
    public Setor(Empresa empresa, String nome, String descricao, String turnos, int qntFuncionarios) {
        this.setEmpresa(empresa);
        this.setNome(nome);
        this.setDescricao(descricao);
        this.setTurnos(turnos);
        this.setQntFuncionarios(qntFuncionarios);
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

    // Para o objeto da empresa
    public Empresa getEmpresa() {
        return empresa;
    }
    public void setEmpresa(Empresa empresa) {
        if (empresa == null) { // Exceção: verifica se o objeto da empresa é nulo
            throw new NullPointerException("O objeto da empresa não pode ser nulo.");
        }
        this.empresa = empresa;
        this.idEmpresa = empresa.getId();  // Define o atributo idEmpresa, que serve como FK no banco de dados
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

    // Para a descrição
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        if (descricao == null) { // Exceção: verifica se a descrição é nula
            throw new NullPointerException("A descrição não pode ser nula.");
        }
        if (descricao.trim().isEmpty()) { // Exceção: verifica se a descrição só contém espaço
            throw new IllegalArgumentException("A descrição não pode ser em branco.");
        }
        this.descricao = descricao;
    }

    // Para os turnos
    public String getTurnos() {
        return turnos;
    }
    public void setTurnos(String turnos) {
        if (turnos == null) { // Exceção: verifica se os turnos é nulo
            throw new NullPointerException("Os turnos não podem ser nulo.");
        }
        if (turnos.trim().isEmpty()) { // Exceção: verifica se os turnos só contém espaço
            throw new IllegalArgumentException("Os turnos não podem ser em branco.");
        }
        this.turnos = turnos;
    }

    // Para a quantidade de funcionários
    public int getQntFuncionarios() {
        return qntFuncionarios;
    }
    public void setQntFuncionarios(int qntFuncionarios) {
        if (qntFuncionarios < 0) { // Exceção: verifica se a quantidade de funcionários é negativa
            throw new IllegalArgumentException("A quantidade de funcionários não pode ser negativa.");
        }
        this.qntFuncionarios = qntFuncionarios;
    }

    // Para id da empresa
    public int getIdEmpresa() {
        return idEmpresa;
    }

    // Método toString
    public String toString() {
        return String.format("Setor | Id: %-3d | Empresa: %-20s | Nome: %-20s | Descrição: %-50s | Qnt. Funcionários: %-5d | ID Empresa: %-3d",
                this.id,
                this.empresa,
                this.nome,
                this.descricao,
                this.qntFuncionarios,
                this.idEmpresa
        );
    }
}
