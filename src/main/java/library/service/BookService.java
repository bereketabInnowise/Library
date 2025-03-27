package library.service;

import library.model.Book;
import library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

/**
 * Service layer for book operations, demonstrating Field-based DI.
 * Uses IoC via Spring to manage dependencies.
 */
@Service
public class BookService {
//    Field dependency injection
    @Autowired
    private BookRepository bookRepository;


    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public void createBook(Book book) {
        if (book.getTitle() == null || book.getTitle().trim().isEmpty() ||
                book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            throw new IllegalArgumentException("Title and author are required");
        }
        List<Book> books = new ArrayList<>(bookRepository.findAll());
        book.setId(bookRepository.getNextId());
        books.add(book);
        bookRepository.saveAll(books);
    }

    public void updateBook(int id, Book updatedBook) {
        List<Book> books = new ArrayList<>(bookRepository.findAll());
        if (books.stream().noneMatch(book -> book.getId() == id)) {
            throw new IllegalArgumentException("Book with ID " + id + " not found");
        }
        if (updatedBook.getTitle() == null || updatedBook.getTitle().trim().isEmpty() ||
                updatedBook.getAuthor() == null || updatedBook.getAuthor().trim().isEmpty()) {
            throw new IllegalArgumentException("Title and author are required");
        }
        updatedBook.setId(id);  // Ensure ID remains the same
        books.replaceAll(book -> book.getId() == id ? updatedBook : book);
        bookRepository.saveAll(books);
    }

    public void deleteBook(int id) {
        List<Book> books = new ArrayList<>(bookRepository.findAll());
        if (books.stream().noneMatch(book -> book.getId() == id)) {
            throw new IllegalArgumentException("Book with ID " + id + " not found");
        }
        books.removeIf(book -> book.getId() == id);
        bookRepository.saveAll(books);
    }
    public boolean bookExists(int id) {
        return bookRepository.findAll().stream().anyMatch(book -> book.getId() == id);
    }
    public Book getBookById(int id) {
        return bookRepository.findAll().stream()
                .filter(book -> book.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Book with ID " + id + " not found"));
    }
}
/** Custom exception for book not found scenarios */
class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(int id) {
        super("Book with ID " + id + " not found");
    }
}