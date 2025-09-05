package com.example.kronoscrud.Models;

import java.time.LocalTime;
import java.util.List;

public class Empresa {
    private int ID;
    private String nome;
    private String CEP;
    private String CNPJ;
    private String email;
    private String telefone;
    private String porte;
    private Plano planoAssinado;
    private LocalTime horarioAbertura;
    private LocalTime horarioFechamento;
    private String regraDeNegocios;
}
