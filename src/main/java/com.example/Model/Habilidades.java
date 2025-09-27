package com.example.Model;

/**
 * Representa uma habilidade de um funcionário: com nome, descrição e uma única tag.
 * Esta classe garante que os dados de uma habilidade sejam sempre válidos e bem estruturados.
 */
public class Habilidades {
    // Atributos
    private int id;
    private String nome;
    private String tag;
    private String descricao;

    // Construtores
    public Habilidades(int id, String nome, String tag, String descricao) {
        try {
            this.setId(id);
            this.setNome(nome);
            this.setTag(tag);
            this.setDescricao(descricao);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public Habilidades(String nome, String tag, String descricao) {
        try {
            this.setNome(nome);
            this.setTag(tag);
            this.setDescricao(descricao);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    // Getters e Setters

    // Para o ID
    public int getId() {
        return id;
    }
    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("O ID (" + id + ") não pode ser zero ou negativo.");
        }
        this.id = id;
    }

    // Para o nome
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome não pode ser nulo ou em branco.");
        }
        this.nome = nome;
    }

    // Para a tag
    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        if (tag == null || tag.trim().isEmpty()) {
            throw new IllegalArgumentException("A tag não pode ser nula ou vazia.");
        }
        this.tag = tag;
    }

    // Para a descrição
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new IllegalArgumentException("A descrição não pode ser nula ou em branco");
        }
        this.descricao = descricao;
    }

    // Método toString
    @Override
    public String toString() {
        return String.format(
                "Habilidades | Id: %-3d | Nome: %-20s | Tag: %-25s | Descrição: %-50s",
                this.id,
                this.nome,
                this.tag,
                this.descricao
        );
    }
}