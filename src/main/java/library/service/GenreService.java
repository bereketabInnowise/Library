package library.service;

import library.model.Genre;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService {
    private final SessionFactory sessionFactory;

    public GenreService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void createGenre(String name) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Genre genre = new Genre(name);
            session.persist(genre);
            session.getTransaction().commit();
        }
    }

    public void updateGenre(Long id, String name) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Genre genre = session.get(Genre.class, id);
            if (genre == null) {
                throw new IllegalArgumentException("Genre with ID " + id + " not found");
            }
            genre.setName(name);
            session.merge(genre);
            session.getTransaction().commit();
        }
    }

    public void deleteGenre(Long id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Genre genre = session.get(Genre.class, id);
            if (genre == null) {
                throw new IllegalArgumentException("Genre with ID " + id + " not found");
            }
            session.remove(genre);
            session.getTransaction().commit();
        }
    }

    public List<Genre> getAllGenres() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Genre", Genre.class).setCacheable(true).getResultList();
        }
    }

    public Genre getGenreById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Genre genre = session.get(Genre.class, id);
            if (genre == null) {
                throw new IllegalArgumentException("Genre with ID " + id + " not found");
            }
            return genre;
        }
    }

    public boolean genreExists(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Genre.class, id) != null;
        }
    }
}