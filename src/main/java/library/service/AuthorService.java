package library.service;

import library.model.Author;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {
    private final SessionFactory sessionFactory;

    public AuthorService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void createAuthor(String name) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(new Author(name));
            session.getTransaction().commit();
        }
    }

    public void updateAuthor(Long id, String name) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Author author = session.get(Author.class, id);
            if (author == null) {
                throw new IllegalArgumentException("Author with ID " + id + " not found");
            }
            author.setName(name);
            session.merge(author);
            session.getTransaction().commit();
        }
    }

    public void deleteAuthor(Long id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Author author = session.get(Author.class, id);
            if (author == null) {
                throw new IllegalArgumentException("Author with ID " + id + " not found");
            }
            session.remove(author);
            session.getTransaction().commit();
        }
    }

    public List<Author> getAllAuthors() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Author", Author.class).setCacheable(true).getResultList();
        }
    }

    public Author getAuthorById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Author author = session.get(Author.class, id);
            if (author == null) {
                throw new IllegalArgumentException("Author with ID " + id + " not found");
            }
            return author;
        }
    }

    public boolean authorExists(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Author.class, id) != null;
        }
    }
}