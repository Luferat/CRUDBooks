package com.crudbooks.api.controller;

import com.crudbooks.api.model.Book;
import com.crudbooks.api.util.DatabaseUtil;
import com.crudbooks.api.util.ResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@jakarta.servlet.annotation.WebServlet(name = "BookListServlet", urlPatterns = {"/api/books"})
public class BookListServlet extends jakarta.servlet.http.HttpServlet {

    @Override
    protected void doGet(jakarta.servlet.http.HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Book> books = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT * FROM books ORDER BY created_at DESC");

            while (rs.next()) {
                Book book = new Book();
                book.setId(rs.getInt("id"));
                book.setCreatedAt(rs.getString("created_at"));
                book.setTitle(rs.getString("title"));
                book.setAuthors(rs.getString("authors"));
                book.setIsbn(rs.getString("isbn"));
                book.setSynopsis(rs.getString("synopsis"));
                book.setStatus(Book.Status.valueOf(rs.getString("status")));
                books.add(book);
            }

            var response = ResponseUtil.success(200, "Lista de livros obtida com sucesso.", books);
            writeJsonResponse(resp, response, 200);

        } catch (Exception e) {
            var response = ResponseUtil.error(500, "Erro ao listar livros.", e.getMessage());
            writeJsonResponse(resp, response, 500);
        }
    }

    private void writeJsonResponse(HttpServletResponse resp, Object response, int status) throws IOException {
        resp.setContentType("application/json");
        resp.setStatus(status);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getWriter(), response);
    }
}
