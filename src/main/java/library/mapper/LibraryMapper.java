package library.mapper;

import library.dto.AuthorDTO;
import library.dto.BookDTO;
import library.dto.GenreDTO;
import library.model.Author;
import library.model.Book;
import library.model.Genre;

import java.util.stream.Collectors;

public class LibraryMapper {

    public static BookDTO toBookDTO(Book book) {
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setDescription(book.getDescription());
        dto.setAuthor(toAuthorDTO(book.getAuthor()));
        dto.setGenres(book.getGenres().stream().map(LibraryMapper::toGenreDTO).collect(Collectors.toList()));
        dto.setImageId(book.getImageId());
        return dto;
    }

    public static AuthorDTO toAuthorDTO(Author author) {
        AuthorDTO dto = new AuthorDTO();
        dto.setId(author.getId());
        dto.setName(author.getName());
        return dto;
    }

    public static GenreDTO toGenreDTO(Genre genre) {
        GenreDTO dto = new GenreDTO();
        dto.setId(genre.getId());
        dto.setName(genre.getName());
        return dto;
    }
}