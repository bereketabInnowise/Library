package library.controller;

import library.dto.BookCreateDTO;
import library.dto.BookDTO;
import library.dto.BookUpdateDTO;
import library.mapper.LibraryMapper;
import library.model.Book;
import library.model.Genre;
import library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks(Pageable pageable) {
        Page<Book> books = bookService.getAllBooks(pageable);
        List<BookDTO> dtos = books.getContent().stream()
                .map(LibraryMapper::toBookDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        return bookService.getBookById(id)
                .map(book -> ResponseEntity.ok(LibraryMapper.toBookDTO(book)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BookDTO> createBook(@RequestBody BookCreateDTO dto) {
        Book book = bookService.createBook(dto.getTitle(), dto.getDescription(),
                dto.getAuthorId(), dto.getGenreIds());
        return new ResponseEntity<>(LibraryMapper.toBookDTO(book), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @RequestBody BookUpdateDTO dto) {
        Book book = bookService.updateBook(id, dto.getTitle(), dto.getDescription(),
                dto.getAuthorId(), dto.getGenreIds());
        return ResponseEntity.ok(LibraryMapper.toBookDTO(book));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookDTO> patchBook(@PathVariable Long id, @RequestBody BookUpdateDTO dto) {
        Book book = bookService.getBookById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));
        if (dto.getTitle() != null) {
            book.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            book.setDescription(dto.getDescription());
        }
        if (dto.getAuthorId() != null) {
            book.setAuthor(bookService.getAuthorById(dto.getAuthorId())
                    .orElseThrow(() -> new IllegalArgumentException("Author not found")));
        }
        if (dto.getGenreIds() != null) {
            List<Long> genreIds = dto.getGenreIds();
            book.setGenres(bookService.getGenresByIds(genreIds));
            if (book.getGenres().size() != genreIds.size()) {
                throw new IllegalArgumentException("Some genres not found");
            }
        }
        Book updatedBook = bookService.updateBook(id, book.getTitle(), book.getDescription(),
                book.getAuthor().getId(),
                book.getGenres().stream()
                        .map(Genre::getId)
                        .collect(Collectors.toList()));
        return ResponseEntity.ok(LibraryMapper.toBookDTO(updatedBook));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/authors/{authorId}/books")
    public ResponseEntity<List<BookDTO>> getBooksByAuthor(@PathVariable Long authorId) {
        List<BookDTO> dtos = bookService.getBooksByAuthorId(authorId).stream()
                .map(LibraryMapper::toBookDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}