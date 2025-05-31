package com.crudbooks;

import io.javalin.Javalin;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.plugin.bundled.CorsPluginConfig;

import java.util.List;

public class App {
    public static void main(String[] args) {
        DatabaseUtil.initDatabase(); // Cria tabela e dados de teste
        BookDAO bookDAO = new BookDAO();
        ObjectMapper objectMapper = new ObjectMapper(); // Para serializar objetos em JSON

        // Criação do aplicativo Javalin
        Javalin app = Javalin.create(config -> {
            // Configuração de CORS
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(CorsPluginConfig.CorsRule::anyHost);
            });
        }).start(Config.httpPort);

        // Página inicial do aplicativo
        app.get("/", ctx -> ctx.json(ResponseUtil.success(200, Config.APP_NAME + " rodando!")));

        // Lista todos os livros existentes
        app.get("/api/books", ctx -> {
            List<Book> books = bookDAO.getAllBooksOrderedByCreatedAt();
            if (books != null && !books.isEmpty()) {
                ctx.json(ResponseUtil.success(200, "Lista de livros obtida com sucesso.", books));
            } else if (books != null) {
                ctx.json(ResponseUtil.success(200, "Nenhum livro encontrado.", books));
            } else {
                ctx.status(500).json(ResponseUtil.error(500, "Erro ao listar livros."));
            }
        });

        // Lista um livro existente pelo Id
        app.get("/api/books/{id}", ctx -> {
            int bookId = ctx.pathParamAsClass("id", Integer.class).get();
            Book book = bookDAO.getBookById(bookId);
            if (book != null) {
                ctx.json(ResponseUtil.success(200, "Livro encontrado.", book));
            } else {
                ctx.status(404).json(ResponseUtil.error(404, "Livro não encontrado."));
            }
        });

        // Apaga um livro pelo Id (atualiza status para OFF)
        app.delete("/api/books/{id}", ctx -> {
            int bookId = ctx.pathParamAsClass("id", Integer.class).get();
            if (bookDAO.logicalDeleteBook(bookId)) {
                ctx.json(ResponseUtil.success(200, "Livro apagado com sucesso."));
            } else {
                ctx.status(404).json(ResponseUtil.error(404, "Livro não encontrado."));
            }
        });

        // Cria um novo livro
        app.post("/api/books", ctx -> {
            try {
                Book newBook = ctx.bodyAsClass(Book.class);
                Book createdBook = bookDAO.createBook(newBook);
                if (createdBook != null) {
                    ctx.status(201).json(ResponseUtil.success(201, "Livro cadastrado com sucesso.", createdBook));
                } else {
                    ctx.status(500).json(ResponseUtil.error(500, "Erro ao cadastrar livro."));
                }
            } catch (Exception e) {
                ctx.status(400).json(ResponseUtil.error(400, "Dados do livro inválidos.", e.getMessage()));
            }
        });

        // Atualiza um livro existente pelo Id
        app.put("/api/books/{id}", ctx -> {
            int bookId = ctx.pathParamAsClass("id", Integer.class).get();
            try {
                Book updatedBook = ctx.bodyAsClass(Book.class);
                if (bookDAO.updateBook(bookId, updatedBook)) {
                    ctx.json(ResponseUtil.success(200, "Livro atualizado com sucesso."));
                } else {
                    ctx.status(404).json(ResponseUtil.error(404, "Livro não encontrado."));
                }
            } catch (Exception e) {
                ctx.status(400).json(ResponseUtil.error(400, "Dados do livro inválidos.", e.getMessage()));
            }
        });
    }
}