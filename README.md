# kronos-crud

## Índice
- [📓 Sobre](#-sobre)
- [🚀 Tecnologias](#-tecnologias)
- [✨ Funcionalidades](#-funcionalidades)
- [⚙️ Instalação](#-instalação)
- [🧱 Estrutura do Projeto](#-estrutura-do-projeto)
- [📄 Licença](#-licença)
- [💻 Autores](#-autores)

## 📓 Sobre
kronos-crud é uma aplicação web completa desenvolvida em Java, que inclui um painel administrativo (CRUD) e uma landing
page de apresentação, complementares para o sistema **Kronos**.
O painel administrativo é construído seguindo a arquitetura MVC (Model-View-Controller), utilizando Java Servlets como
controladores, DAOs (Data Access Objects) para a lógica de banco de dados, e JSPs (JavaServer Pages) para a camada de
visualização (View).

## 🚀 Tecnologias
- Back-end:
    - Java 19
    - Java Servlets
    - JavaServer Pages (JSP)
    - JDBC (Java Database Connectivity)
- Front-end:
    - HTML5
    - CSS3
    - JavaScript
- Banco de Dados:
    - PostgreSQL
- Servidor de Aplicação:
    - Apache Tomcat 11.0.11.
- Build & Dependência:
    - Apache Maven

## ✨ Funcionalidades
- Painel Administrativo (CRUD)
    - Tela de Login para autenticação segura do administrador.
    - CRUD completo (Create, Read, Update, Delete) para 6 módulos:
        - Administradores
        - Usuários
        - Empresas
        - Setores
        - Planos
        - Habilidades
    - Busca e filtragem de dados nas tabelas.
    - Ordenação de dados por colunas (crescente ou decrescente).
    - Interface de popup para criação, edição e exclusão de itens.

- Back-end (Arquitetura)
    - Arquitetura MVC (Model-View-Controller).
    - Padrão DAO (Data Access Object).
    - Servlets para controlar o fluxo de requisições.
    - Validação de dados nos Models (Java) antes de salvar.
    - Validação de regras complexas com Regex (CPF, CNPJ, senha, e-mail, etc.).
    - Padrão Post-Redirect-Get para evitar reenvio de formulários.
    - Tratamento de exceções de validação e de SQL nos Servlets.
    - Carregamento de credenciais de banco (via .env) para segurança.

- Landing Page
    - Website de apresentação (HTML e CSS) separado do painel.
    - Link de acesso para a área de login do CRUD.

## ⚙️ Instalação
É necessário ter o Java JDK (11 ou superior), um Servidor (Apache Tomcat) e um Banco de Dados (PostgreSQL) instalados.
```
# Clonar o repositório
git clone https://github.com/Systems-Kronos/kronos-crud.git

# Entrar no diretório
cd kronos-crud

# Configurar o Banco de Dados (Passo Manual)
# 1. Crie um banco de dados (ex: "kronos_db") no seu PostgreSQL. 
# 2. Importe o script SQL do projeto para criar as tabelas.

# Criar o arquivo de variáveis de ambiente
cp src/main/resources/.env.example src/main/resources/.env

# Configurar o .env (Passo Manual)
# 1. Abra o arquivo 'src/main/resources/.env'
# 2. Preencha com suas credenciais do banco: 
# DB_URL=jdbc:postgresql://localhost:5432/kronos_db
# DB_USER=seu_usuario_postgres
# DB_PASSWORD=sua_senha_postgres

# Buildar o projeto com Maven (Execute apenas o comando correspondente ao seu sistema operacional abaixo)
# No Windows (use o .cmd): 
mvnw.cmd clean package
# No macOS/Linux (use o ./mvnw): 
./mvnw clean package

# Rodar o projeto (Passo Manual)
# 1. Inicie seu servidor Apache Tomcat.
# 2. Faça o deploy do arquivo .war (gerado na pasta /target/) no Tomcat.
# 3. Acesse a aplicação (ex: http://localhost:8080/kronos-crud/login-crud)
```

## 🧱 Estrutura do Projeto
```
kronos-crud
├── /.mvn           # Configuração do Maven Wrapper
├── README.md           # Este arquivo
├── .gitignore           # Arquivos ignorados pelo Git
├── LICENSE           # Licença do projeto
├── mvnw & mvnw.cmd          # Scripts do Maven Wrapper
├── pom.xml           # Dependências e build do Maven
└── /src            # Código-fonte principal
  └── /main
    ├── /java           # Código Java (Back-end)
    │  └── /com.example 
    │    ├── /Controller           # Guarda a classe de conexão com o BD
    │    ├── /dao           # Data Access Objects (Lógica SQL)
    │    ├── /Model           # Classes de modelo
    │    └── /Servlet           # Servlets (Controladores)
    ├── /resources           # Arquivo de configuração (ex: .env.example)
    └── /webapp           # Código Web (Front-end)
      ├── /assets           # Arquivos estáticos (CSS, JS, Imagens)
      ├── /crud           # Páginas HTML específicas do CRUD
      ├── /landingpage           # Páginas HTML da Landing Page
      └── /WEB-INF
        ├── /pages           # Arquivos JSP (Views)
        └── web.xml           # Mapeamento de Servlets
```

## 📄 Licença
Este projeto está licenciado sob a licença MIT — veja o arquivo LICENSE para mais detalhes.

## 💻 Autores
- [Breno Gomes](https://github.com/Brenoz001)
- [Gabriel Vigna](https://github.com/bielvigna)
- [Henrique Akira](https://github.com/Akira-132)
- [João Prado](https://github.com/JonesPrado)
- [Matheus Orestes](https://github.com/matheus-orestes)
- [Rebecca Sarah](https://github.com/sarahstxs)