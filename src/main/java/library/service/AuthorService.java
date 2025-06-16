package library.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class AuthorService {
    private final JdbcTemplate jdbcTemplate;

    public AuthorService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createAuthor(String name) {
        String sql = "INSERT INTO authors (name) VALUES (?)";
        jdbcTemplate.update(sql, name);
    }

    public void updateAuthor(int id, String name) {
        String sql = "UPDATE authors SET name = ? WHERE id = ?";
        int rows = jdbcTemplate.update(sql, name, id);
        if (rows == 0) {
            throw new IllegalArgumentException("Author with ID " + id + " not found");
        }
    }

    public void deleteAuthor(int id) {
        String sql = "DELETE FROM authors WHERE id = ?";
        int rows = jdbcTemplate.update(sql, id);
        if (rows == 0) {
            throw new IllegalArgumentException("Author with ID " + id + " not found");
        }
    }

    public List<Author> getAllAuthors() {
        String sql = "SELECT id, name FROM authors";
        return jdbcTemplate.query(sql, new AuthorRowMapper());
    }

    public Author getAuthorById(int id) {
        String sql = "SELECT id, name FROM authors WHERE id = ?";
        List<Author> authors = jdbcTemplate.query(sql, new AuthorRowMapper(), id);
        if (authors.isEmpty()) {
            throw new IllegalArgumentException("Author with ID " + id + " not found");
        }
        return authors.get(0);
    }

    public boolean authorExists(int id) {
        String sql = "SELECT COUNT(*) FROM authors WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    public static class Author {
        private int id;
        private String name;

        public Author(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "Author{id=" + id + ", name='" + name + "'}";
        }
    }

    private static class AuthorRowMapper implements RowMapper<Author> {
        @Override
        public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Author(rs.getInt("id"), rs.getString("name"));
        }
    }
}