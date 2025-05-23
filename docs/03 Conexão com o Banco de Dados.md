# Conexão com o Banco de Dados

Diferentemente do Spring Boot, o **H2 Database** é uma entidade à parte da aplicação Javalin.
Vamos precisar instalar o **H2 Console" separadamente para acessar o banco de dados e fazer a conexão "manualmente".

- Baixe o **H2 Database** de https://h2database.com/, na versão 'All Platforms (zip)'
- Descompacte na pasta descompactada
- Acesse a pasta "h2\bin"
- Selecione e copie o conteúdo dela
- Cole na pasta raiz do projeto → `~\Documents\java\CRUDBooks`
- Para testar no Windows, abra um terminal no IntelliJ e comande `h2.bat`
- Se estiver no Linux ou MacOX, comande `h2.sh`

> Deve abrir o login do "H2 Console" no navegador padrão.

- Altere o campo 'JDBC URL:' para `jdbc:h2:./data/crudbooks`
- Clique em `[Connect]` para criar/conectar no banco de dados

> Para acessar o "H2 Console", o endereço é `http://localhost:8082/`, mas lembre-se que o serviço dele deve estar rodando em um terminal.

## Configuração da Conexão

Crie uma classe `com.crudbooks.api.config.Config` para salvar os dados de conexão ao banco de dados e outros setup úteis:

`config.Config`
```java
package com.crudbooks.api.config;

public class Config {
    public static final String JDBC_URL = "jdbc:h2:./data/crudbooks";
    public static final String JDBC_USER = "sa";
    public static final String JDBC_PASSWORD = "";
    public static final int httpPort = 7000;
}
```

Crie ainda um utilitário que automatiza a criação da tabela e ainda insere alguns dados no banco de dados para testes:

`util.DatabaseUtil`
```java
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
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
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

            var rs = stmt.executeQuery("SELECT COUNT(*) FROM books");
            rs.next();
            int count = rs.getInt(1);

            if (count == 0) {
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
```

Aproveitando o momento, crie também o 'model':

`model.Book`
```java
package com.crudbooks.api.model;

import lombok.Data;

@Data
public class Book {
    private Integer id;
    private String createdAt;
    private String title;
    private String authors;
    private String isbn;
    private String synopsis;
    private Status status = Status.ON;

    public enum Status {
        ON, OFF
    }
}
```

## Modificando o Main

Como já temos uma estrutura básica para nossa API, podemos modificar `App.java` para rodar um aplicativo "Javalin":

`App`
```java
package com.crudbooks;

import com.crudbooks.api.util.DatabaseUtil;
import io.javalin.Javalin;
import com.crudbooks.api.config.Config;

public class App {
    public static void main(String[] args) {
        DatabaseUtil.initDatabase();  // Cria tabela e dados de teste

        Javalin app = Javalin.create().start(Config.httpPort);

        app.get("/", ctx -> ctx.result("API CRUD Books rodando!"));
    }
}
```

Agora, rode `App` e verifique se o banco de dados (`data\crudbooks.mv.db`) é criado.
Acesse o H2 Console (`http://localhosr:8082`) e veriique se a tabela `BOOKS` foi criada e contém alguns dados.

Para testar o **HTTP**, no navegador, acesse `http://localhost:7000/` para ver uma página com algo como:

```cmd
API CRUD Books rodando!
```
---
[↑ Topo](#file-01-javalin-md)
