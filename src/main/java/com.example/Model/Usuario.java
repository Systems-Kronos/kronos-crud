package com.example.Model;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Representa um usuário com detalhes pessoais e profissionais.
 */
public class Usuario {
    private int id;
    private Character genero;
    private String nome;
    private String telefoneFixo;
    private String telefonePessoal;
    private String cpf;
    private String senha;
    private String status;
    private Setor setor;
    private List<Habilidades> habilidades;

    // construtores, getters, setters, validações...
}