package library.service;

import library.model.Genre;
import library.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class GenreService {

    private final GenreRepository genreRepository;

    @Autowired
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public Page<Genre> getAllGenres(Pageable pageable) {
        return genreRepository.findAll(pageable);
    }

    public Optional<Genre> getGenreById(Long id) {
        return genreRepository.findById(id);
    }

    @Transactional
    public Genre createGenre(String name) {
        Genre genre = new Genre();
        genre.setName(name);
        return genreRepository.save(genre);
    }

    @Transactional
    public Genre updateGenre(Long id, String name) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Genre not found"));
        genre.setName(name);
        return genreRepository.save(genre);
    }

    @Transactional
    public void deleteGenre(Long id) {
        if (!genreRepository.existsById(id)) {
            throw new IllegalArgumentException("Genre not found");
        }
        genreRepository.deleteById(id);
    }
}