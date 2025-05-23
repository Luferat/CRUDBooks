package com.crudbooks.api.util;

import com.crudbooks.api.config.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtil {

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(Config.JDBC_URL, Config.JDBC_USER, Config.JDBC_PASSWORD);
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
                    INSERT INTO books (title, authors, isbn, synopsis, status) VALUES
                    ('Effective Java', 'Joshua Bloch', '9780134685991', 'Best practices for Java programming.', 'ON'),
                    ('Clean Code', 'Robert C. Martin', '9780132350884', 'Guide to writing clean, maintainable code.', 'ON'),
                    ('Design Patterns', 'Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides', '9780201633610', 'Classic book on software design patterns.', 'ON'),
                    ('Refactoring', 'Martin Fowler', '9780201485677', 'Improving the design of existing code.', 'OFF'),
                    ('Java Concurrency in Practice', 'Brian Goetz', '9780321349606', 'Comprehensive guide to Java concurrency.', 'ON')
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
