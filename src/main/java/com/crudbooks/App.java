package com.crudbooks;

import com.crudbooks.api.model.Book;
import com.crudbooks.api.util.BookDAO;
import com.crudbooks.api.util.DatabaseUtil;
import com.crudbooks.api.util.ResponseUtil;
import io.javalin.Javalin;
import com.crudbooks.api.config.Config;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.plugin.bundled.CorsPluginConfig;

import java.util.List;

public class App {
    public static void main(String[] args) {
        DatabaseUtil.initDatabase(); // Cria tabela e dados de teste
        BookDAO bookDAO = new BookDAO();
        ObjectMapper objectMapper = new ObjectMapper(); // Para serializar objetos em JSON

        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(CorsPluginConfig.CorsRule::anyHost);
            });
        }).start(Config.httpPort);

        // Página inicial do aplicativo
        app.get("/", ctx -> ctx.result("API CRUD Books rodando!"));

        // Lista todos os livros existentes
        app.get("/api/books", ctx -> {
            List<Book> books = bookDAO.getAllBooksOrderedByCreatedAt();
            if (books != null && !books.isEmpty()) {
                ctx.json(ResponseUtil.success(200, "Lista de livros existentes obtida com sucesso.", books));
            } else if (books != null) {
                ctx.json(ResponseUtil.success(200, "Nenhum livro existente encontrado.", books));
            } else {
                ctx.status(500).json(ResponseUtil.error(500, "Erro ao buscar livros existentes."));
            }
        });

        // Lista um livro existente pelo Id
        app.get("/api/books/{id}", ctx -> {
            int bookId = ctx.pathParamAsClass("id", Integer.class).get();
            Book book = bookDAO.getBookById(bookId);
            if (book != null) {
                ctx.json(ResponseUtil.success(200, "Livro existente encontrado.", book));
            } else {
                ctx.status(404).json(ResponseUtil.error(404, "Livro existente não encontrado (ou foi apagado)."));
            }
        });

        // Apaga um livro pelo Id (atualiza status para OFF)
        app.delete("/api/books/{id}", ctx -> {
            int bookId = ctx.pathParamAsClass("id", Integer.class).get();
            if (bookDAO.logicalDeleteBook(bookId)) {
                ctx.json(ResponseUtil.success(200, "Livro apagado com sucesso."));
            } else {
                ctx.status(404).json(ResponseUtil.error(404, "Livro não encontrado para apagar."));
            }
        });

        // Cria um novo livro
        app.post("/api/books", ctx -> {
            try {
                Book newBook = ctx.bodyAsClass(Book.class);
                Book createdBook = bookDAO.createBook(newBook);
                if (createdBook != null) {
                    ctx.status(201).json(ResponseUtil.success(201, "Livro criado com sucesso.", createdBook));
                } else {
                    ctx.status(500).json(ResponseUtil.error(500, "Erro ao criar o livro."));
                }
            } catch (Exception e) {
                ctx.status(400).json(ResponseUtil.error(400, "Dados de livro inválidos.", e.getMessage()));
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
                    ctx.status(404).json(ResponseUtil.error(404, "Livro não encontrado para atualizar."));
                }
            } catch (Exception e) {
                ctx.status(400).json(ResponseUtil.error(400, "Dados de livro inválidos para atualização.", e.getMessage()));
            }
        });
    }
}