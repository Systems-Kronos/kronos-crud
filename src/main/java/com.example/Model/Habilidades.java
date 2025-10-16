package com.example.Model;

/**
 * Representa um conjunto de habilidades de um funcionário: com nome, descrição e tag.
 */
public class Habilidades {
    // Atributos
    private int id;
    private String nome;
    private String tag;
    private String descricao;

    // Métodos Construtores

    // As validações de exceções são realizadas pelos métodos setters
    public Habilidades(int id, String nome, String tag, String descricao) {
        this.setId(id);
        this.setNome(nome);
        this.setTag(tag);
        this.setDescricao(descricao);
    }

    public Habilidades(String nome, String tag, String descricao) {
        this.setNome(nome);
        this.setTag(tag);
        this.setDescricao(descricao);
    }

    // Getters e Setters

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

    // Para o tag
    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        if (tag == null) { // Exceção: verifica se a tag é nula
            throw new NullPointerException("A tag não pode ser nula.");
        }
        if (tag.trim().isEmpty()) { // Exceção: verifica se o nome só contém espaço
            throw new IllegalArgumentException("O nome não pode estar em branco.");
        }
        this.tag = tag;
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
            throw new IllegalArgumentException("A descrição não pode estar em branco.");
        }
        this.descricao = descricao;
    }

    // Método toString
    @Override
    public String toString() {
        return String.format("Habilidades | Id: %-3d | Nome: %-20s | Tag: %-25s | Descrição: %-50s",
                id, nome, tag, descricao);
    }
}