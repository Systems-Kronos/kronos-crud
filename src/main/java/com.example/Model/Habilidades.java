package com.example.Model;

/**
 * Representa um conjunto de habilidades de um funcionário: com nome, descrição e tag.
 */
public class Habilidades {
    private int id;
    private String nome;
    private String tag;
    private String descricao;

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
    public int getId() { return id; }
    public void setId(int id) {
        if (id <= 0) throw new IllegalArgumentException("O ID não pode ser zero ou negativo.");
        this.id = id;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty())
            throw new IllegalArgumentException("O nome não pode ser nulo ou em branco.");
        this.nome = nome;
    }

    public String getTag() { return tag; }
    public void setTag(String tag) {
        if (tag == null || tag.trim().isEmpty())
            throw new IllegalArgumentException("A tag não pode ser nula ou vazia.");
        this.tag = tag;
    }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) {
        if (descricao == null || descricao.trim().isEmpty())
            throw new IllegalArgumentException("Uma descrição não pode ser nula ou em branco");
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return String.format("Habilidades | Id: %-3d | Nome: %-20s | Tag: %-25s | Descrição: %-50s",
                id, nome, tag, descricao);
    }
}