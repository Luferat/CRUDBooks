package com.crudbooks.api.util;

import com.crudbooks.api.config.Config;
import com.crudbooks.api.model.Book;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    public List<Book> getAllBooksOrderedByCreatedAt() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT id, created_at, title, authors, isbn, synopsis, status FROM books ORDER BY created_at";

        try (Connection conn = DriverManager.getConnection(Config.JDBC_URL, Config.JDBC_USER, Config.JDBC_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Book book = new Book();
                book.setId(rs.getInt("id"));
                book.setCreatedAt(rs.getString("created_at")); // H2 TIMESTAMP para String
                book.setTitle(rs.getString("title"));
                book.setAuthors(rs.getString("authors"));
                book.setIsbn(rs.getString("isbn"));
                book.setSynopsis(rs.getString("synopsis"));
                book.setStatus(Book.Status.valueOf(rs.getString("status"))); // String para Enum
                books.add(book);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Em um cenário real, você trataria isso melhor
        }
        return books;
    }
}