package com.crudbooks.api.util;

import com.crudbooks.api.config.Config;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtil {

    private static final HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(Config.JDBC_URL);
        config.setUsername(Config.JDBC_USER);
        config.setPassword(Config.JDBC_PASSWORD);
        config.setDriverClassName(Config.JDBC_DRIVER); // Explicitamente definir o driver
        config.setMaximumPoolSize(10); // Defina um tamanho razoável para o pool
        config.setAutoCommit(true); // Ou false, dependendo da sua necessidade

        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void initDatabase() {

        // Esse try-with-resources fecha automaticamente o Connection e o Statement ao final do bloco, mesmo se lançar exceção.
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {

            // Cria a tabela no banco
            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS books (
                            id INTEGER PRIMARY KEY AUTO_INCREMENT,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            title VARCHAR(255),
                            authors VARCHAR(255),
                            cover VARCHAR(255),
                            isbn VARCHAR(20),
                            synopsis TEXT,
                            status VARCHAR(10) DEFAULT 'ON'
                        )
                    """);

            // Se não existem dados na tabela...
            var rs = stmt.executeQuery("SELECT COUNT(*) FROM books");
            rs.next();
            int count = rs.getInt(1);

            if (count == 0) {

                // Cadastra alguns dados para testes
                stmt.execute("""
                            INSERT INTO books (title, authors, cover, isbn, synopsis) VALUES
                            ('Effective Java', 'Joshua Bloch', 'https://picsum.photos/id/1/200/300', '9780134685991', 'Best practices for Java programming.'),
                            ('Clean Code', 'Robert C. Martin', 'https://picsum.photos/id/1/200/300', '9780132350884', 'Guide to writing clean, maintainable code.'),
                            ('Design Patterns', 'Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides', 'https://picsum.photos/id/1/200/300', '9780201633610', 'Classic book on software design patterns.'),
                            ('Refactoring', 'Martin Fowler', 'https://picsum.photos/id/1/200/300', '9780201485677', 'Improving the design of existing code.'),
                            ('Java Concurrency in Practice', 'Brian Goetz', 'https://picsum.photos/id/1/200/300', '9780321349606', 'Comprehensive guide to Java concurrency.')
                        """);
                System.out.println("Dados de teste inseridos.");
            } else {
                System.out.println("Tabela 'books' já possui dados.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
