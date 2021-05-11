package com.codecool.bookdb.model;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDaoJdbc implements BookDao {
    private DataSource dataSource;

    public BookDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(Book book) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO book (author_id, title) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1, book.getAuthor().getId());
                statement.setString(2, book.getTitle());

                statement.executeUpdate();

                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    int bookId = resultSet.getInt(1);
                    book.setId(bookId);
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void update(Book book) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE book SET author_id = ?, title = ? WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, book.getAuthor().getId());
                statement.setString(2, book.getTitle());
                statement.setInt(3, book.getId());

                statement.executeUpdate();
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public Book get(int id) {
        Book book = null;

        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT b.id AS book_id, b.title, a.id AS author_id, a.first_name, a.last_name, a.birth_date "
                + "FROM book AS b INNER JOIN author AS a ON b.author_id = a.id "
                + "WHERE b.id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    int bookId = resultSet.getInt("book_id");
                    String title = resultSet.getString("title");
                    int authorId = resultSet.getInt("author_id");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    Date birthDate = resultSet.getDate("birth_date");

                    Author author = new Author(firstName, lastName, birthDate);
                    author.setId(authorId);
                    book = new Book(author, title);
                    book.setId(bookId);
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }

        return book;
    }

    @Override
    public List<Book> getAll() {
        List<Book> books = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT b.id as book_id, b.title, a.id as author_id, a.first_name, a.last_name, a.birth_date "
                    + "FROM book AS b INNER JOIN author AS a on b.author_id = a.id";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    int bookId = resultSet.getInt("book_id");
                    String title = resultSet.getString("title");
                    int authorId = resultSet.getInt("author_id");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    Date birthDate = resultSet.getDate("birth_date");

                    Author author = new Author(firstName, lastName, birthDate);
                    author.setId(authorId);
                    Book book = new Book(author, title);
                    book.setId(bookId);
                    books.add(book);
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
        return books;
    }
}
