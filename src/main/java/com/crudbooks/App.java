package com.crudbooks;

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

        app.get("/", ctx -> ctx.result("API CRUD Books rodando!"));

        app.get("/books", ctx -> {
            var books = bookDAO.getAllBooksOrderedByCreatedAt();
            if (books != null && !books.isEmpty()) {
                ctx.json(ResponseUtil.success(200, "Lista de livros recuperada com sucesso.", books));
            } else if (books != null) {
                ctx.json(ResponseUtil.success(200, "Nenhum livro encontrado.", books));
            } else {
                ctx.status(500).json(ResponseUtil.error(500, "Erro ao buscar livros."));
            }
        });
    }
}