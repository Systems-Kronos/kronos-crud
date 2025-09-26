//package com.example.Model;
//
//import java.util.List;
//
///**
// * Representa um usuário com detalhes pessoais e profissionais.
// * Esta classe armazena informações como nome, CPF, gênero, credenciais de acesso,
// * setor e a lista de habilidades do usuário, garantindo a integridade dos dados.
// */
//public class Usuario {
//// Atributos
//    private int id;
//    private Character genero;
//    private String nome;
//    private String telefoneFixo;
//    private String telefonePessoal;
//    private String cpf;
//    private String senha;
//    private String status;
//    private Setor setor;
//    private List<Habilidades> habilidades;
//
//// Métodos Construtores
//
//// As validações de exceções são realizadas pelos métodos set.
//    public Usuario(int id, Character genero, String nome, String cpf, String senha, String status, Setor setor, List<Habilidades> habilidades) {
//        try {
//            this.setId(id);
//            this.setGenero(genero);
//            this.setNome(nome);
//            this.setCpf(cpf);
//            this.setSenha(senha);
//            this.setStatus(status);
//            this.setSetor(setor);;;
//            this.setHabilidades(habilidades);
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public Usuario(Character genero, String nome, String cpf, String senha, String status, Setor setor, List<Habilidades> habilidades) {
//        try {
//            this.setGenero(genero);
//            this.setNome(nome);
//            this.setCpf(cpf);
//            this.setSenha(senha);
//            this.setStatus(status);
//            this.setSetor(setor);;
//            this.setHabilidades(habilidades);
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        }
//    }
//
//// Métodos Getters e Setters
//
//// Para o ID
//    public int getId() {
//        return id;
//    }
//    public void setId(int id) {
//        if (id <= 0) { // Exceção: verifica se o ID é negativo ou igual a zero.
//            throw new IllegalArgumentException("O ID (" + id + ") não pode ser zero ou negativo.");
//        }
//        this.id = id;
//    }
//
//// Para o nome
//    public String getNome() {
//        return nome;
//    }
//    public void setNome(String nome) {
//        if (nome == null || nome.trim().isEmpty()) { // Exceção: verifica se o nome é nulo ou só contém espaço.
//            throw new IllegalArgumentException("O nome não pode ser nulo ou em branco.");
//        }
//        this.nome = nome;
//    }
//
//// Para o telefone fixo e pessoal
//    public String getTelefoneFixo() {
//        return telefoneFixo;
//    }
//    public String getTelefonePessoal() {
//        return telefonePessoal;
//    }
//    public void setTelefone(String telefone) {
//        if (!isValidTelefone(telefone)) { // Exceção: usa o método isValidTelefone
//            throw new IllegalArgumentException("O telefone (" + telefone + ") não é válido.");
//        }
//    }
//
//// Para o gênero
//    public Character getGenero() {
//        return genero;
//    }
//    public void setGenero(Character genero) {
//        if (!isValidGender(genero)) { // Exceção: verifica se o gênero é válido.
//            throw new IllegalArgumentException("O gênero (" + genero + ") não é valido.");
//        }
//        this.genero = genero;
//    }
//
//// Para o CPF
//    public String getCpf() {
//        return cpf;
//    }
//    public void setCpf(String cpf) {
//        if (!isValidCpf(cpf)) { // Exceção: verifica se o CPF é válido
//            throw new IllegalArgumentException("O CPF (" + cpf + ") não é válido.");
//        }
//        this.cpf = cpf;
//    }
//
//// Para a senha
//    public String getSenha() {
//        return senha;
//    }
//    public void setSenha(String senha) {
//        this.senha = senha;
//    }
//
//// Para o status
//    public String getStatus() {
//        return status;
//    }
//    public void setStatus(String status) {
//        if (status == null || status.trim().isEmpty()) { // Exceção: verifica se o status é nulo ou só contém espaços.
//            throw new IllegalArgumentException("O status não pode ser nula ou em branco");
//        }
//        this.status = status;
//    }
//
//// Para o setor
//    public Setor getSetor() {
//        return setor;
//    }
//    public void setSetor(Setor setor) {
//        if (setor == null) { // Exceção: verifica se o objeto setor é nula.
//            throw new IllegalArgumentException("O objeto setor não pode ser nula.");
//        }
//        this.setor = setor;
//    }
//
//// Para a lista de habilidades
//    public List<Habilidades> getHabilidades() {
//        return habilidades;
//    }
//    public void setHabilidades(List<Habilidades> habilidades) {
//        if (habilidades == null || habilidades.isEmpty()) { // Exceção: verifica se a lista de habilidades é nula ou vazia.
//            throw new IllegalArgumentException("A lista de habilidades não pode ser nula ou vazia.");
//        }
//
//        for (Habilidades habilidade : habilidades) { // Valida cada objeto de habilidade, um por um.
//            if (habilidade == null) { // Exceção: verifica se o objeto de habilidade é nulo.
//                throw new IllegalArgumentException("Um objeto de habilidade não pode ser nulo");
//            }
//        }
//        this.habilidades = habilidades;
//    }
//
//// Para o método toString
//    public String toString() {
//        return String.format("Usuário | Id: %-3d | Nome: %-20s | Telefone Fixo: %-12s | Telefone Pessoal: %-12s | Gênero: %-1s | Cpf: %-14s | Senha: %-15s | Status: %-7s | Setor: %-15s | Habilidades: %-30s",
//                this.id,
//                this.nome,
//                this.telefoneFixo,
//                this.telefonePessoal,
//                this.genero,
//                this.cpf,
//                this.senha,
//                this.status,
//                this.setor,
//                this.habilidades
//                );
//    }
//
//// Métodos de Validação
//
///**
// * Verifica se o gênero é válido
// * Aceitados:
// * 'M' de masculino, 'F' de feminino, 'O' de outro, 'N' de prefiro não informar
//*/
//    private boolean isValidGender(Character genero) {
//        genero = Character.toUpperCase(genero);
//        if (genero == null || genero.equals('M') || !genero.equals('F') || !genero.equals('O') && !genero.equals('N')) {
//            return false;
//        }
//        return true;
//    }
//
///**
// * Verifica se o CPF é válido
// * Exemplos de CPF aceitável:
// * "123.123.123-12", "12312312312"
// */
//    private boolean isValidCpf(String cpf) {
//        if (cpf == null || cpf.trim().isEmpty()) { // Exceção: verifica se o CPF é nulo ou só contém espaços
//            return false;
//        }
//        String regex = "\\b\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}\\b";
//        boolean valido = cpf.trim().matches(regex);
//        if (valido) {
//            String cpfBanco = cpf.replace("[^\\d]", "");
//            this.cpf = cpfBanco;
//        }
//        return cpf.trim().matches(regex);
//    }
//
//
///**
// * Verifica se o telefone é válido
// * Exemplos de telefone aceitável:
// * "(11) 12345-1234", "11123451234"
// */
//    private boolean isValidTelefone(String telefone) {
//        if (telefone == null || telefone.trim().isEmpty()) {  // Exceção: verifica se o telefone é nulo ou só contém espaços
//            return false;
//        }
//        String regex = "\\(?\\d{2}\\)?\\d{4,5}-?\\d{4}";
//        boolean valido = telefone.trim().matches(regex);
//        if (valido) {
//            String telefoneBanco = telefone.replace("[^\\d]", "");
//            if (telefone.equals(this.telefoneFixo)) {
//                this.telefoneFixo = telefoneBanco;
//            } else {
//                this.telefonePessoal = telefoneBanco;
//            }
//        }
//        return telefone.trim().matches(regex);
//    }
//
///**
// * Verifca se a senha é válida baseando nessas regras:
// * -mínimo 8 caracteres
// * -mínimo 1 letra maiúscula
// * -mínimo 1 letra minúscula
// * -mínimo 1 caractere especial
// * -mínimo 1 número
// */
//    private boolean isValidSenha(String senha) {
//        if (senha == null) { // Exceção: verifica se a senha é nulo
//            throw new IllegalArgumentException("A senha não pode ser nula.");
//        } if (senha.length() < 8) {
//            throw new IllegalArgumentException("A senha deve ter no mínimo 8 caracteres");
//        }
//        return true;
//
//    }
//}