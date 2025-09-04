package com.example.kronoscrud.Models;

import java.time.LocalTime;
import java.util.List;

public class Empresa {
    private String nome;
    private String CEP;
    private String regraDeNegocios;
    private String email;
    private String CNPJ;
    private String porte;
    private String telefone;
    private LocalTime horarioAbertura;
    private LocalTime horarioFechamento;
    private Endereco endereco;
    private List<Setor> setor;
}
