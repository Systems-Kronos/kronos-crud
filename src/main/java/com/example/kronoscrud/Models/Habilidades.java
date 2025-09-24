package com.example.kronoscrud.Models;

import java.util.List;

/**
 * Representa um conjunto de habilidades de um funcionário: com nome, descrição e tags.
 * Esta classe garante que os dados de uma habilidade sejam sempre válidos e bem estruturados.
 */
public class Habilidades {
// Atributos
    private int id;
    private String nome;
    private List<String> tags;
    private String descricao;

// Métodos Construtores

// As validações de exceções são realizadas pelos métodos set. 
    public Habilidades(int id, String nome, List<String> tags, String descricao) {
        try {
            this.setId(id);
            this.setNome(nome);
            this.setTags(tags);
            this.setDescricao(descricao);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
    public Habilidades(String nome, List<String> tags, String descricao) {
        try {
            this.setNome(nome);
            this.setTags(tags);
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
    public List<String> getTags() {
        return tags;
    }
    public void setTags(List<String> tags) {
        if (tags == null || tags.isEmpty()) { // Exceção: verifica se a lista de tags é nula ou vazia.
            throw new IllegalArgumentException("A lista de tags não pode ser nula ou vazia.");
        } 

        for (String tag : tags) { // Valida cada tag, uma por uma.
            if (tag == null || tag.trim().isEmpty()) { // Exceção: verifica se a tag é nula ou em branco.
                throw new IllegalArgumentException("Uma tag não pode ser nula ou em branco");
            }
        }
        this.tags = tags;
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
        // Como o atributo tags é uma lista, este foi transformado em String para o toString.
        String stringTags = String.join(", ", this.tags);
        return String.format("Habilidades | Id: %-3d | Nome: %-20s | Tags: %-25s | Descrição: %-50s",
                this.id,
                this.nome,
                stringTags,
                this.descricao
                );
    }
}
