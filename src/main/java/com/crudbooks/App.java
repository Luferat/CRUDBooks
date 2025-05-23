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
