package library.controller;

import library.dto.BookCreateDTO;
import library.dto.BookDTO;
import library.dto.BookUpdateDTO;
import library.mapper.LibraryMapper;
import library.model.Book;
import library.model.Genre;
import library.service.BookService;
import library.service.GridFsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;
    private final GridFsService gridFsService;

    @Autowired
    public BookController(BookService bookService, GridFsService gridFsService) {
        this.bookService = bookService;
        this.gridFsService = gridFsService;
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
                dto.getAuthorId(), dto.getGenreIds(), dto.getImageId());
        return new ResponseEntity<>(LibraryMapper.toBookDTO(book), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @RequestBody BookUpdateDTO dto) {
        Book book = bookService.updateBook(id, dto.getTitle(), dto.getDescription(),
                dto.getAuthorId(), dto.getGenreIds(), dto.getImageId());
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
        if (dto.getImageId() != null){
            book.setImageId(dto.getImageId());
        }
        Book updatedBook = bookService.updateBook(id, book.getTitle(), book.getDescription(),
                book.getAuthor().getId(),
                book.getGenres().stream()
                        .map(Genre::getId)
                        .collect(Collectors.toList()),book.getImageId());
        return ResponseEntity.ok(LibraryMapper.toBookDTO(updatedBook));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        Book book = bookService.getBookById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));
        if (book.getImageId() != null) {
            gridFsService.deleteFile(book.getImageId());
        }
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
    @PostMapping("/{id}/image")
    public ResponseEntity<BookDTO> uploadBookImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        Book book = bookService.getBookById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));
        // Delete existing image if present
        if (book.getImageId() != null) {
            gridFsService.deleteFile(book.getImageId());
        }
        // Store new image in GridFS
        String imageId = gridFsService.storeFile(file);
        // Update book with new imageId
        Book updatedBook = bookService.updateImageId(id, imageId);
        return ResponseEntity.ok(LibraryMapper.toBookDTO(updatedBook));
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getBookImage(@PathVariable Long id) throws IOException {
        Book book = bookService.getBookById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));
        if (book.getImageId() == null) {
            return ResponseEntity.notFound().build();
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        gridFsService.downloadFile(book.getImageId(), outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        String filename = book.getTitle().replaceAll("[^a-zA-Z0-9.-]", "_") + ".jpg";
        String encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8.toString())
                .replace("+", "%20");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFilename);
        headers.setContentLength(imageBytes.length);
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }
}
