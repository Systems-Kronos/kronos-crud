package com.example.Model;

/**
 * Representa um conjunto de habilidades do usuário: com nome, descrição e tag.
 */
public class HabilidadeUsuario {
    private int usuarioId;
    private int habilidadeId;

    public HabilidadeUsuario(int usuarioId, int habilidadeId) {
        this.setUsuarioId(usuarioId);
        this.setHabilidadeId(habilidadeId);
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int id) {
        if (id <= 0) { // Exceção: verifica se o ID é negativo ou igual a zero
            throw new IllegalArgumentException("O ID do usuário não pode ser zero ou negativo.");
        }
        this.usuarioId = id;
    }
    public int getHabilidadeId() {
        return habilidadeId;
    }

    public void setHabilidadeId(int id) {
        if (id <= 0) { // Exceção: verifica se o ID é negativo ou igual a zero
            throw new IllegalArgumentException("O ID de habilidades não pode ser zero ou negativo.");
        }
        this.habilidadeId = id;
    }
    public String toString() {
        return "HabilidadUsuario | Id usuário: " + getUsuarioId() + "| Id habilidade: " + getHabilidadeId();
    }
}
