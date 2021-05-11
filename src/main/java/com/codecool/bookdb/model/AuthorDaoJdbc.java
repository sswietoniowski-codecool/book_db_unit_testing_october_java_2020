package com.codecool.bookdb.model;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorDaoJdbc implements AuthorDao {
    private DataSource dataSource;

    public AuthorDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(Author author) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO author (first_name, last_name, birth_date) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, author.getFirstName());
                statement.setString(2, author.getLastName());
                statement.setDate(3, author.getBirthDate());

                statement.executeUpdate();

                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    int authorId = resultSet.getInt(1);
                    author.setId(authorId);
                }
                resultSet.close();
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void update(Author author) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE author SET first_name = ?, last_name = ?, birth_date = ? WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, author.getFirstName());
                statement.setString(2, author.getLastName());
                statement.setDate(3, author.getBirthDate());
                statement.setInt(4, author.getId());

                statement.executeUpdate();
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public Author get(int id) {
        Author author = null;

        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT id, first_name, last_name, birth_date FROM author WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    Date birthDate = resultSet.getDate("birth_date");

                    author = new Author(firstName, lastName, birthDate);
                    author.setId(id);
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }

        return author;
    }

    @Override
    public List<Author> getAll() {
        List<Author> authors = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT id, first_name, last_name, birth_date FROM author";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    Date birthDate = resultSet.getDate("birth_date");

                    Author author = new Author(firstName, lastName, birthDate);
                    author.setId(id);
                    authors.add(author);
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
        return authors;
    }
}
