package com.example.Model;

/**
 * Representa um conjunto de habilidades do usuário: com nome, descrição e tag.
 */
public class HabilidadeUsuario {
    // Atributos
    private int usuarioId;
    private int habilidadeId;
    private Habilidades habilidades;
    private Usuario usuario;

    // As validações de exceções são realizadas pelos métodos setters
    public HabilidadeUsuario(int usuarioId, int habilidadeId,
                             Habilidades habilidades, Usuario usuario) {
        this.setUsuarioId(usuarioId);
        this.setHabilidadeId(habilidadeId);
        this.setHabilidades(habilidades);
        this.setUsuario(usuario);
    }

    // Métodos Getters e Setters

    // Para o ID usuário
    public int getUsuarioId() {
        return usuarioId;
    }
    public void setUsuarioId(int id) {
        if (id <= 0) { // Exceção: verifica se o ID é negativo ou igual a zero
            throw new IllegalArgumentException("O ID do usuário não pode ser zero ou negativo.");
        }
        this.usuarioId = id;
    }

    // Para o ID habilidade
    public int getHabilidadeId() {
        return habilidadeId;
    }
    public void setHabilidadeId(int id) {
        if (id <= 0) { // Exceção: verifica se o ID é negativo ou igual a zero
            throw new IllegalArgumentException("O ID de habilidades não pode ser zero ou negativo.");
        }
        this.habilidadeId = id;
    }

    // Para o objeto habilidades
    public Habilidades getHabilidades() {
        return habilidades;
    }
    public void setHabilidades(Habilidades habilidades) {
        if (habilidades == null) { // Exceção: verifica se o objeto habilidades é nulo
            throw new NullPointerException("O objeto de habilidades não pode ser nulo.");
        }
        if (this.habilidadeId != habilidades.getId()) { // Exceção: verifica se o atributo habilidadeId é diferente do ID do objeto
            throw new IllegalStateException(
                    "Conflito de IDs: O ID das habilidades (" + this.habilidadeId +
                            ") é diferente do ID do objeto Habilidade fornecido (" + habilidades.getId() + ")."
            );
        }
        this.habilidades = habilidades;
        this.habilidadeId = habilidades.getId();
    }

    // Para o objeto usuario
    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        if (usuario == null) { // Exceção: verifica se o objeto usuario é nulo
            throw new NullPointerException("O objeto de usuário não pode ser nulo.");
        }
        if (this.usuarioId != usuario.getId()) { // Exceção: verifica se o atributo usuarioId é diferente do ID do objeto
            throw new IllegalStateException(
                    "Conflito de IDs: O ID do usuário (" + this.usuarioId +
                            ") é diferente do ID do objeto Usuário fornecido (" + usuario.getId() + ")."
            );
        }
        this.usuario = usuario;
        this.usuarioId = usuario.getId();
    }

    public String toString() {
        return String.format("HabilidadeUsuario | Habilidade: %-20s | ID Habilidade: %-3d | Usuario: %-20s | ID Usuário: %-3d",
                this.habilidades.getNome(),
                this.habilidadeId,
                this.usuario.getNome(),
                this.usuarioId
                );
    }
}
