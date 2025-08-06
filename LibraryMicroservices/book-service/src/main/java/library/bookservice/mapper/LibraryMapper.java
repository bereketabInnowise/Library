package library.bookservice.mapper;

import library.bookservice.dto.AuthorDTO;
import library.bookservice.dto.BookDTO;
import library.bookservice.dto.GenreDTO;
import library.bookservice.model.Author;
import library.bookservice.model.Book;
import library.bookservice.model.Genre;

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