package com.crudbooks;

import com.crudbooks.api.model.Book;
import com.crudbooks.api.util.BookDAO;
import com.crudbooks.api.util.DatabaseUtil;
import com.crudbooks.api.util.ResponseUtil;
import io.javalin.Javalin;
import com.crudbooks.api.config.Config;
import com.fasterxml.jackson.databind.ObjectMapper;

public class App {
    public static void main(String[] args) {
        DatabaseUtil.initDatabase(); // Cria tabela e dados de teste
        BookDAO bookDAO = new BookDAO();
        ObjectMapper objectMapper = new ObjectMapper(); // Para serializar objetos em JSON

        Javalin app = Javalin.create().start(Config.httpPort);

        // Página inicial do aplicativo
        app.get("/", ctx -> ctx.result("API CRUD Books rodando!"));

        // Lista todos os livros
        app.get("/books", ctx -> {
            var books = bookDAO.getAllBooksOrderedByCreatedAt();
            if (books != null && !books.isEmpty()) {
                ctx.json(ResponseUtil.success(200, "Lista de livros existentes recuperada com sucesso.", books));
            } else if (books != null && books.isEmpty()) {
                ctx.json(ResponseUtil.success(200, "Nenhum livro existente encontrado.", books));
            } else {
                ctx.status(500).json(ResponseUtil.error(500, "Erro ao buscar livros existentes."));
            }
        });

        // Lista um livro pelo Id
        app.get("/books/{id}", ctx -> {
            int bookId = ctx.pathParamAsClass("id", Integer.class).get();
            Book book = bookDAO.getBookById(bookId);
            if (book != null) {
                ctx.json(ResponseUtil.success(200, "Livro existente encontrado.", book));
            } else {
                ctx.status(404).json(ResponseUtil.error(404, "Livro existente não encontrado (ou foi apagado)."));
            }
        });

        // Apaga um livro pelo Id. Apenas altera para "status=OFF"
        app.delete("/books/{id}", ctx -> {
            int bookId = ctx.pathParamAsClass("id", Integer.class).get();
            if (bookDAO.logicalDeleteBook(bookId)) {
                ctx.json(ResponseUtil.success(200, "Livro apagado com sucesso."));
            } else {
                ctx.status(404).json(ResponseUtil.error(404, "Livro não encontrado para apagar."));
            }
        });
    }
}