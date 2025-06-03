package library.controller;

import library.dto.GenreDTO;
import library.mapper.LibraryMapper;
import library.model.Genre;
import library.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/genres")
public class GenreController {

    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public ResponseEntity<List<GenreDTO>> getAllGenres(Pageable pageable) {
        Page<Genre> genres = genreService.getAllGenres(pageable);
        List<GenreDTO> dtos = genres.getContent().stream()
                .map(LibraryMapper::toGenreDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreDTO> getGenreById(@PathVariable Long id) {
        return genreService.getGenreById(id)
                .map(genre -> ResponseEntity.ok(LibraryMapper.toGenreDTO(genre)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<GenreDTO> createGenre(@RequestBody GenreDTO dto) {
        Genre genre = genreService.createGenre(dto.getName());
        return new ResponseEntity<>(LibraryMapper.toGenreDTO(genre), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenreDTO> updateGenre(@PathVariable Long id, @RequestBody GenreDTO dto) {
        Genre genre = genreService.updateGenre(id, dto.getName());
        return ResponseEntity.ok(LibraryMapper.toGenreDTO(genre));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        genreService.deleteGenre(id);
        return ResponseEntity.noContent().build();
    }
}