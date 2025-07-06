package library.service;

import library.dto.BookCreateDTO;
import library.dto.BookDTO;
import library.model.Author;
import library.model.Book;
import library.model.Genre;
import library.repository.AuthorRepository;
import library.repository.BookRepository;
import library.repository.GenreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private BookService bookService;

    private Book book;
    private Author author;
    private Genre genre;

    @BeforeEach
    void setUp() {
        author = new Author();
        author.setId(1L);
        author.setName("Author Name");

        genre = new Genre();
        genre.setId(1L);
        genre.setName("Fiction");

        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setDescription("Description");
        book.setAuthor(author);
        book.setGenres(List.of(genre));
    }

    @Test
    void getBookById_found() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        Optional<Book> result = bookService.getBookById(1L);
        assertTrue(result.isPresent());
        assertEquals("Test Book", result.get().getTitle());
        verify(bookRepository).findById(1L);
    }

    @Test
    void getBookById_notFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<Book> result = bookService.getBookById(1L);
        assertFalse(result.isPresent());
        verify(bookRepository).findById(1L);
    }

    @Test
    void createBook_valid() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(genreRepository.findAllById(List.of(1L))).thenReturn(List.of(genre));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book result = bookService.createBook("New Book", "New Description", 1L, List.of(1L), null);
        assertNotNull(result);
        assertEquals("Test Book", result.getTitle());
        verify(bookRepository).save(any(Book.class));
    }
}
