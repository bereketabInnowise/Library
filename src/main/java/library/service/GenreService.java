package library.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class GenreService {
    private final JdbcTemplate jdbcTemplate;

    public GenreService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createGenre(String name) {
        String sql = "INSERT INTO genres (name) VALUES (?) ON CONFLICT (name) DO NOTHING";
        jdbcTemplate.update(sql, name);
    }

    public void updateGenre(int id, String name) {
        String sql = "UPDATE genres SET name = ? WHERE id = ?";
        int rows = jdbcTemplate.update(sql, name, id);
        if (rows == 0) {
            throw new IllegalArgumentException("Genre with ID " + id + " not found");
        }
    }

    public void deleteGenre(int id) {
        String sql = "DELETE FROM genres WHERE id = ?";
        int rows = jdbcTemplate.update(sql, id);
        if (rows == 0) {
            throw new IllegalArgumentException("Genre with ID " + id + " not found");
        }
    }

    public List<Genre> getAllGenres() {
        String sql = "SELECT id, name FROM genres";
        return jdbcTemplate.query(sql, new GenreRowMapper());
    }

    public Genre getGenreById(int id) {
        String sql = "SELECT id, name FROM genres WHERE id = ?";
        List<Genre> genres = jdbcTemplate.query(sql, new GenreRowMapper(), id);
        if (genres.isEmpty()) {
            throw new IllegalArgumentException("Genre with ID " + id + " not found");
        }
        return genres.get(0);
    }

    public boolean genreExists(int id) {
        String sql = "SELECT COUNT(*) FROM genres WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    public static class Genre {
        private int id;
        private String name;

        public Genre(int id, String name) {
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
            return "Genre{id=" + id + ", name='" + name + "'}";
        }
    }

    private static class GenreRowMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Genre(rs.getInt("id"), rs.getString("name"));
        }
    }
}