package com.example.Controller;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe utilitária para estabelecer e fechar conexões JDBC com o banco de dados PostgreSQL.
 * Carrega as credenciais de forma eficiente a partir de um arquivo .env na inicialização.
 */
public class Conexao {

    // Credenciais do banco carregadas de forma estática (apenas uma vez)
    private static final String DB_URL;
    private static final String DB_USER;
    private static final String DB_PASSWORD;

    /*
     * Bloco estático: é executado APENAS UMA VEZ quando a classe é carregada pela JVM.
     * Carrega o driver e as credenciais do .env de forma eficiente.
     */
    static {
        try {
            // 1. Carrega o driver JDBC
            Class.forName("org.postgresql.Driver");

            // 2. Carrega as variáveis do .env (apenas uma vez)
            Dotenv dotenv = Dotenv.configure()
                    .ignoreIfMissing()
                    .load();

            DB_URL = dotenv.get("DB_URL");
            DB_USER = dotenv.get("DB_USER");
            DB_PASSWORD = dotenv.get("DB_PASSWORD");

            // 3. Valida se as variáveis foram carregadas
            if (DB_URL == null || DB_USER == null || DB_PASSWORD == null) {
                // Lança uma exceção clara se as variáveis estiverem faltando
                throw new NullPointerException("Variáveis de ambiente (DB_URL, DB_USER, DB_PASSWORD) não encontradas no .env.");
            }

        } catch (ClassNotFoundException e) {
            // Erro fatal: O driver .jar do PostgreSQL não está no classpath.
            System.err.println("Erro Crítico: Driver JDBC PostgreSQL não encontrado no classpath.");
            throw new ExceptionInInitializerError(e); // Para a aplicação (fail-fast)

        } catch (DotenvException | NullPointerException e) {
            // Erro fatal: O arquivo .env está ausente ou incompleto.
            System.err.println("Erro ao carregar configurações do .env: " + e.getMessage());
            throw new ExceptionInInitializerError(e); // Para a aplicação (fail-fast)
        }
    }

    /*
     * Tenta estabelecer uma nova conexão física com o banco de dados.
     * Utiliza as credenciais carregadas estaticamente.
     */
    public Connection conectar() throws SQLException {
        // Agora, o método apenas usa as variáveis estáticas.
        // Ele não trata mais ClassNotFoundException ou DotenvException,
        // pois isso foi tratado no bloco estático.
        return DriverManager.getConnection(
                DB_URL,
                DB_USER,
                DB_PASSWORD
        );
    }

    /*
     * Tenta fechar a conexão JDBC fornecida, se ela estiver aberta e não for nula.
     */
    public void desconectar(Connection conn) throws SQLException {
        // Se a conexão for válida, fecha.
        // Se conn.close() lançar uma SQLException, o método simplesmente a propaga.
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
}