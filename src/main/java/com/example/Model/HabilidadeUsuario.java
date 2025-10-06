package com.example.Model;

public class HabilidadeUsuario {
    private int usuarioId;
    private int habilidadeId;

    public HabilidadeUsuario(int usuarioId, int habilidadeId) {
        this.usuarioId = usuarioId;
        this.habilidadeId = habilidadeId;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getHabilidadeId() {
        return habilidadeId;
    }

    public void setHabilidadeId(int habilidadeId) {
        this.habilidadeId = habilidadeId;
    }

    public String toString() {
        return "HabilidadUsuario | Id usuário: " + getUsuarioId() + "| Id habilidade: " + getHabilidadeId();
    }
}
