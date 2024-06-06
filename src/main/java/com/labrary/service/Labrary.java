package com.library.service;

import com.library.model.Book;
import com.library.model.User;
import com.library.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Library {
    private Connection connection;

    public Library() {
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addBook(Book book) {
        String sql = "INSERT INTO Book (isbn, title, author, category, isAvailable) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, book.getIsbn());
            stmt.setString(2, book.getTitle());
            stmt.setString(3, book.getAuthor());
            stmt.setString(4, book.getCategory());
            stmt.setBoolean(5, book.isAvailable());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void registerUser(User user) {
        String sql = "INSERT INTO User (userId, name) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getUserId());
            stmt.setString(2, user.getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void borrowBook(String userId, String isbn) {
        String sqlCheck = "SELECT isAvailable FROM Book WHERE isbn = ?";
        String sqlBorrow = "INSERT INTO BorrowedBooks (userId, isbn, borrowDate) VALUES (?, ?, SYSDATE)";
        String sqlUpdate = "UPDATE Book SET isAvailable = 0 WHERE isbn = ?";
        try (PreparedStatement stmtCheck = connection.prepareStatement(sqlCheck);
             PreparedStatement stmtBorrow = connection.prepareStatement(sqlBorrow);
             PreparedStatement stmtUpdate = connection.prepareStatement(sqlUpdate)) {

            stmtCheck.setString(1, isbn);
            ResultSet rs = stmtCheck.executeQuery();
            if (rs.next() && rs.getBoolean("isAvailable")) {
                stmtBorrow.setString(1, userId);
                stmtBorrow.setString(2, isbn);
                stmtBorrow.executeUpdate();

                stmtUpdate.setString(1, isbn);
                stmtUpdate.executeUpdate();
            } else {
                System.out.println("Book is not available.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void returnBook(String userId, String isbn) {
        String sqlReturn = "DELETE FROM BorrowedBooks WHERE userId = ? AND isbn = ?";
        String sqlUpdate = "UPDATE Book SET isAvailable = 1 WHERE isbn = ?";
        try (PreparedStatement stmtReturn = connection.prepareStatement(sqlReturn);
             PreparedStatement stmtUpdate = connection.prepareStatement(sqlUpdate)) {

            stmtReturn.setString(1, userId);
            stmtReturn.setString(2, isbn);
            stmtReturn.executeUpdate();

            stmtUpdate.setString(1, isbn);
            stmtUpdate.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Book> listAvailableBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM Book WHERE isAvailable = 1";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                books.add(new Book(
                    rs.getString("isbn"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("category"),
                    rs.getBoolean("isAvailable")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public List<Book> listBooksBorrowedByUser(String userId) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT b.* FROM Book b INNER JOIN BorrowedBooks bb ON b.isbn = bb.isbn WHERE bb.userId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                books.add(new Book(
                    rs.getString("isbn"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("category"),
                    rs.getBoolean("isAvailable")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public List<Book> filterBooks(String title, String category, String author) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM Book WHERE title LIKE ? AND category LIKE ? AND author LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + title + "%");
            stmt.setString(2, "%" + category + "%");
            stmt.setString(3, "%" + author + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                books.add(new Book(
                    rs.getString("isbn"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("category"),
                    rs.getBoolean("isAvailable")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public void deleteBook(String isbn) {
        String sql = "DELETE FROM Book WHERE isbn = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, isbn);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBook(Book book) {
        String sql = "UPDATE Book SET title = ?, author = ?, category = ?, isAvailable = ? WHERE isbn = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getCategory());
            stmt.setBoolean(4, book.isAvailable());
            stmt.setString(5, book.getIsbn());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
