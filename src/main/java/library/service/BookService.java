package library.service;

import library.model.Book;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {
    private final JdbcTemplate jdbcTemplate;

    public BookService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createBook(Book book) {
        // Insert book
        String sql = "INSERT INTO books (title, author_id, description) VALUES (?, ?, ?) RETURNING id";
        Integer bookId = jdbcTemplate.queryForObject(sql, Integer.class, book.getTitle(), book.getAuthorId(), book.getDescription());
        if (bookId == null) {
            throw new IllegalStateException("Failed to create book");
        }

        // Insert genres
        for (String genreName : book.getGenres()) {
            // Get or create genre
            String genreSql = "INSERT INTO genres (name) VALUES (?) ON CONFLICT (name) DO UPDATE SET name = EXCLUDED.name RETURNING id";
            Integer genreId = jdbcTemplate.queryForObject(genreSql, Integer.class, genreName);
            if (genreId != null) {
                jdbcTemplate.update("INSERT INTO book_genres (book_id, genre_id) VALUES (?, ?)", bookId, genreId);
            }
        }
    }

    public void updateBook(int id, Book book) {
        // Update book
        String sql = "UPDATE books SET title = ?, author_id = ?, description = ? WHERE id = ?";
        int rows = jdbcTemplate.update(sql, book.getTitle(), book.getAuthorId(), book.getDescription(), id);
        if (rows == 0) {
            throw new IllegalArgumentException("Book with ID " + id + " not found");
        }

        // Clear existing genres
        jdbcTemplate.update("DELETE FROM book_genres WHERE book_id = ?", id);

        // Insert new genres
        for (String genreName : book.getGenres()) {
            String genreSql = "INSERT INTO genres (name) VALUES (?) ON CONFLICT (name) DO UPDATE SET name = EXCLUDED.name RETURNING id";
            Integer genreId = jdbcTemplate.queryForObject(genreSql, Integer.class, genreName);
            if (genreId != null) {
                jdbcTemplate.update("INSERT INTO book_genres (book_id, genre_id) VALUES (?, ?)", id, genreId);
            }
        }
    }

    public void deleteBook(int id) {
        // book_genres deleted via ON DELETE CASCADE
        String sql = "DELETE FROM books WHERE id = ?";
        int rows = jdbcTemplate.update(sql, id);
        if (rows == 0) {
            throw new IllegalArgumentException("Book with ID " + id + " not found");
        }
    }

    public List<Book> getAllBooks() {
        String sql = "SELECT b.id, b.title, b.author_id, a.name AS author_name, b.description " +
                "FROM books b JOIN authors a ON b.author_id = a.id";
        List<Book> books = jdbcTemplate.query(sql, new BookRowMapper());

        // Fetch genres for each book
        for (Book book : books) {
            List<String> genres = jdbcTemplate.query(
                    "SELECT g.name FROM genres g JOIN book_genres bg ON g.id = bg.genre_id WHERE bg.book_id = ?",
                    (rs, rowNum) -> rs.getString("name"), book.getId());
            book.setGenres(genres);
        }
        return books;
    }

    public Book getBookById(int id) {
        String sql = "SELECT b.id, b.title, b.author_id, a.name AS author_name, b.description " +
                "FROM books b JOIN authors a ON b.author_id = a.id WHERE b.id = ?";
        List<Book> books = jdbcTemplate.query(sql, new BookRowMapper(), id);
        if (books.isEmpty()) {
            throw new IllegalArgumentException("Book with ID " + id + " not found");
        }
        Book book = books.get(0);

        // Fetch genres
        List<String> genres = jdbcTemplate.query(
                "SELECT g.name FROM genres g JOIN book_genres bg ON g.id = bg.genre_id WHERE bg.book_id = ?",
                (rs, rowNum) -> rs.getString("name"), id);
        book.setGenres(genres);
        return book;
    }

    public boolean bookExists(int id) {
        String sql = "SELECT COUNT(*) FROM books WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    private static class BookRowMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Book(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getInt("author_id"),
                    rs.getString("author_name"),
                    rs.getString("description"),
                    new ArrayList<>()
            );
        }
    }
}
/** Custom exception for book not found scenarios */
class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(int id) {
        super("Book with ID " + id + " not found");
    }
}