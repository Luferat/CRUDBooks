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
        String sql = "SELECT id, created_at, title, authors, isbn, synopsis, status FROM books WHERE status = 'ON' ORDER BY created_at";

        try (Connection conn = DriverManager.getConnection(Config.JDBC_URL, Config.JDBC_USER, Config.JDBC_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

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

        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Em um cenário real, você trataria isso melhor
        }
        return books;
    }

    public Book getBookById(int id) {
        String sql = "SELECT id, created_at, title, authors, isbn, synopsis, status FROM books WHERE id = ? AND status = 'ON'";
        Book book = null;

        try (Connection conn = DriverManager.getConnection(Config.JDBC_URL, Config.JDBC_USER, Config.JDBC_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                book = new Book();
                book.setId(rs.getInt("id"));
                book.setCreatedAt(rs.getString("created_at"));
                book.setTitle(rs.getString("title"));
                book.setAuthors(rs.getString("authors"));
                book.setIsbn(rs.getString("isbn"));
                book.setSynopsis(rs.getString("synopsis"));
                book.setStatus(Book.Status.valueOf(rs.getString("status")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Em um cenário real, você trataria isso melhor
        }
        return book;
    }

    public boolean logicalDeleteBook(int id) {
        String sql = "UPDATE books SET status = 'OFF' WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(Config.JDBC_URL, Config.JDBC_USER, Config.JDBC_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // Retorna true se a atualização foi bem-sucedida (encontrou e atualizou um livro)

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Retorna false em caso de erro
        }
    }
}