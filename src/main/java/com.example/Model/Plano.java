package com.example.Model;

/**
 * Representa um plano de serviço,
 * com detalhes como nome, custo e limite de funcionários.
 * A classe garante a integridade dos dados do plano através de validações.
 */
public class Plano {
// Atributos
    private int id;
    private String nome;
    private float custo;
    private String descricao;
    private int maxFuncionarios;

// Métodos Construtores
    
// As validações de exceções são realizadas pelos métodos set. 
    public Plano (int id, String nome, float custo, String descricao, int maxFuncionarios){
        try {
            this.setId(id);
            this.setNome(nome);
            this.setCusto(custo);
            this.setDescricao(descricao);
            this.setMaxFuncionarios(maxFuncionarios);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public Plano (String nome, float custo, String descricao, int maxFuncionarios){
        try {
            this.setNome(nome);
            this.setCusto(custo);
            this.setDescricao(descricao);
            this.setMaxFuncionarios(maxFuncionarios);
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

// Para o custo
    public float getCusto() {
        return custo;
    }
    public void setCusto(float custo) {
        if (custo <= 0) { // Exceção: verifica se o custo é negativo ou igual a zero
            throw new IllegalArgumentException("O custo (" + custo + ") não pode ser zero ou negativo.");
        }
        this.custo = custo;
    }

// Para a descrição
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) { // Exceção: verifica se a descrição é nula ou só contém espaço
        if (descricao == null || descricao.trim().isEmpty()) { 
            throw new IllegalArgumentException("A descrição não pode ser nula ou em branco.");
        }
        this.descricao = descricao;
    }

// Para o número máximo de funcionários
    public int getMaxFuncionarios() {
        return maxFuncionarios;
    }
    public void setMaxFuncionarios(int maxFuncionarios) {
        if (maxFuncionarios <= 0) { // Exceção: verifica se o número máximo de funcionários é negativo ou igual a zero
            throw new IllegalArgumentException("O número máximo de funcionários (" + maxFuncionarios + ") não pode ser negativo ou igual a zero.");
        }
        this.maxFuncionarios = maxFuncionarios;
    }

// Método toString
    public String toString(){
        return String.format("Plano | Id: %-3d | Nome: %-20s | Custo: R$ %-7.2f | Descrição: %-60s | Max. funcionários: %-5d",
                this.id,
                this.custo,
                this.descricao,
                this.maxFuncionarios
        );
    }
}

