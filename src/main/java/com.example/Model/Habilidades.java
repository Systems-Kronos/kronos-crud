package com.example.Model;

import java.util.List;

/**
 * Representa um conjunto de habilidades de um funcionário: com nome, descrição e tags.
 * Esta classe garante que os dados de uma habilidade sejam sempre válidos e bem estruturados.
 */
public class Habilidades {
// Atributos
    private int id;
    private String nome;
    private String tag;
    private String descricao;

// Métodos Construtores

// As validações de exceções são realizadas pelos métodos set. 
    public Habilidades(int id, String nome, List<String> tags, String descricao) {
        try {
            this.setId(id);
            this.setNome(nome);
            this.setTag(tag);
            this.setDescricao(descricao);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
    public Habilidades(String nome, List<String> tags, String descricao) {
        try {
            this.setNome(nome);
            this.setTag(tag);
            this.setDescricao(descricao);
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
        if (id <= 0) { // Exceção: verifica se o ID é negativo ou igual a zero.
            throw new IllegalArgumentException("O ID (" + id + ") não pode ser zero ou negativo.");
        }
        this.id = id;
    }

// Para o nome
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) { // Exceção: verifica se o nome é nulo ou só contém espaço.
            throw new IllegalArgumentException("O nome não pode ser nulo ou em branco.");
        }
        this.nome = nome;
    }

// Para a lista de tags
    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        if (tag == null || tag.trim().isEmpty()) { // Exceção: verifica se a tag é nula ou vazia.
            throw new IllegalArgumentException("A tag não pode ser nula ou vazia.");
        }
        this.tag = tag;
    }

// Para a descrição
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) { 
        if (descricao == null || descricao.trim().isEmpty()) { // Exceção: verifica se a descrição é nula ou em branco.
            throw new IllegalArgumentException("Uma descrição não pode ser nula ou em branco");
        }
        this.descricao = descricao;
    }

// Método toString
    public String toString() {
        return String.format("Habilidades | Id: %-3d | Nome: %-20s | Tag: %-25s | Descrição: %-50s",
                this.id,
                this.nome,
                this.tag,
                this.descricao
                );
    }
}
