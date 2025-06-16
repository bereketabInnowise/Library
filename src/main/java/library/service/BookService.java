package library.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import library.model.Book;
import library.model.Genre;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private final SessionFactory sessionFactory;

    public BookService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void createBook(Book book) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(book);
            session.getTransaction().commit();
        }
    }

    public void updateBook(Long id, Book updatedBook) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Book existingBook = session.get(Book.class, id);
            if (existingBook == null) {
                throw new IllegalArgumentException("Book with ID " + id + " not found");
            }
            existingBook.setTitle(updatedBook.getTitle());
            existingBook.setAuthor(updatedBook.getAuthor());
            existingBook.setDescription(updatedBook.getDescription());
            existingBook.getGenres().clear();
            existingBook.getGenres().addAll(updatedBook.getGenres());
            session.merge(existingBook);
            session.getTransaction().commit();
        }
    }

    public void deleteBook(Long id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Book book = session.get(Book.class, id);
            if (book == null) {
                throw new IllegalArgumentException("Book with ID " + id + " not found");
            }
            session.remove(book);
            session.getTransaction().commit();
        }
    }

    public List<Book> getAllBooks() {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Book> cq = cb.createQuery(Book.class);
            Root<Book> root = cq.from(Book.class);
            root.fetch("author", JoinType.LEFT);
            root.fetch("genres", JoinType.LEFT);
            cq.select(root);
            return session.createQuery(cq).setCacheable(true).getResultList();
        }
    }

    public Book getBookById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Book book = session.get(Book.class, id);
            if (book == null) {
                throw new IllegalArgumentException("Book with ID " + id + " not found");
            }
            // Initialize lazy collections
            book.getGenres().size();
            return book;
        }
    }

    public boolean bookExists(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Book.class, id) != null;
        }
    }
}