package library.controller;

import library.dto.AuthorDTO;
import library.mapper.LibraryMapper;
import library.model.Author;
import library.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/authors")
public class AuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public ResponseEntity<List<AuthorDTO>> getAllAuthors(Pageable pageable) {
        Page<Author> authors = authorService.getAllAuthors(pageable);
        List<AuthorDTO> dtos = authors.getContent().stream()
                .map(LibraryMapper::toAuthorDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable Long id) {
        return authorService.getAuthorById(id)
                .map(author -> ResponseEntity.ok(LibraryMapper.toAuthorDTO(author)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AuthorDTO> createAuthor(@RequestBody AuthorDTO dto) {
        Author author = authorService.createAuthor(dto.getName());
        return new ResponseEntity<>(LibraryMapper.toAuthorDTO(author), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorDTO> updateAuthor(@PathVariable Long id, @RequestBody AuthorDTO dto) {
        Author author = authorService.updateAuthor(id, dto.getName());
        return ResponseEntity.ok(LibraryMapper.toAuthorDTO(author));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }
}