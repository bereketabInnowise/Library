package library.ui;

import library.model.Author;
import library.model.Book;
import library.model.Genre;
import library.service.AuthorService;
import library.service.BookService;
import library.service.GenreService;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.Collectors;

public class BookManager {
    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final MessageSource messageSource;
    private final Scanner scanner;
    private final Locale locale;
    private final InputHandler inputHandler;

    public BookManager(BookService bookService, AuthorService authorService,
                       GenreService genreService, MessageSource messageSource,
                       Scanner scanner, Locale locale, InputHandler inputHandler) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.genreService = genreService;
        this.messageSource = messageSource;
        this.scanner = scanner;
        this.locale = locale;
        this.inputHandler = inputHandler;
    }

    public void displayBooks() {
        System.out.println("\n" + messageSource.getMessage("app.book.list", null, locale));
        List<Book> books = bookService.getAllBooks();
        if (books.isEmpty()) {
            System.out.println(messageSource.getMessage("app.book.none", null, locale));
        } else {
            String titleLabel = messageSource.getMessage("book.field.title", null, locale);
            String authorLabel = messageSource.getMessage("book.field.author", null, locale);
            String descLabel = messageSource.getMessage("book.field.description", null, locale);
            String genresLabel = messageSource.getMessage("book.field.genres", null, locale);
            for (Book book : books) {
                List<String> genreNames = book.getGenres().stream()
                        .map(Genre::getName)
                        .collect(Collectors.toList());
                System.out.printf("Book{id=%d, %s='%s', %s='%s', %s='%s', %s=%s}%n",
                        book.getId(),
                        titleLabel, book.getTitle(),
                        authorLabel, book.getAuthor() != null ? book.getAuthor().getName() : "Unknown",
                        descLabel, book.getDescription(),
                        genresLabel, genreNames);
            }
        }
    }

    public void createBook() {
        // Select author
        Author author = selectAuthor();
        if (author == null) return;

        // Book details
        String title = inputHandler.getInput("app.prompt.title", new Object[]{""});
        String description = inputHandler.getInput("app.prompt.desc", new Object[]{""});

        // Select genres
        List<Genre> genres = selectGenres();
        if (genres == null) return;

        Book book = new Book(title, author, description, genres);
        bookService.createBook(book);
        System.out.println(messageSource.getMessage("app.success.create", null, locale));
    }

    public void editBook() {
        Long id;
        try {
            id = (long) inputHandler.getValidId("app.prompt.id.edit");
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            return;
        }

        if (!bookService.bookExists(id)) {
            System.out.println(messageSource.getMessage("app.error.id.notfound", new Object[]{id}, locale));
            return;
        }

        Book currentBook = bookService.getBookById(id);
        displayCurrentBook(currentBook);

        // Select author
        Author author = updateAuthor(currentBook);
        if (author == null) return;

        // Book details
        String title = updateTitle(currentBook);
        String description = updateDescription(currentBook);

        // Select genres
        List<Genre> genres = updateGenres(currentBook);
        if (genres == null) return;

        Book updatedBook = new Book(title, author, description, genres);
        updatedBook.setId(id);
        bookService.updateBook(id, updatedBook);
        System.out.println(messageSource.getMessage("app.success.update", null, locale));
    }

    public void deleteBook() {
        Long id;
        try {
            id = (long) inputHandler.getValidId("app.prompt.id.delete");
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            return;
        }

        try {
            bookService.deleteBook(id);
            System.out.println(messageSource.getMessage("app.success.delete", null, locale));
        } catch (IllegalArgumentException e) {
            System.out.println(messageSource.getMessage("app.error.id.notfound", new Object[]{id}, locale));
        }
    }

    private Author selectAuthor() {
        System.out.println(messageSource.getMessage("app.author.list", null, locale));
        List<Author> authors = authorService.getAllAuthors();
        if (authors.isEmpty()) {
            System.out.println(messageSource.getMessage("app.author.emptylist", null, locale));
            return null;
        }

        for (Author author : authors) {
            System.out.println(author.getId() + ": " + author.getName());
        }

        try {
            Long authorId = (long) inputHandler.getValidId("app.prompt.author.id");
            Author author = authorService.getAuthorById(authorId);
            return author;
        } catch (IllegalArgumentException e) {
            System.out.println(messageSource.getMessage("app.error.author.notfound", new Object[]{e.getMessage()}, locale));
            return null;
        }
    }

    private List<Genre> selectGenres() {
        System.out.println(messageSource.getMessage("app.genre.list", null, locale));
        List<Genre> genres = genreService.getAllGenres();
        for (Genre genre : genres) {
            System.out.println(genre.getId() + ": " + genre.getName());
        }

        String genreInput = inputHandler.getInput("app.prompt.genres", null);
        List<Genre> selectedGenres = new ArrayList<>();
        if (!genreInput.isEmpty()) {
            for (String genreIdStr : genreInput.split(",")) {
                try {
                    Long genreId = Long.parseLong(genreIdStr.trim());
                    Genre genre = genreService.getGenreById(genreId);
                    selectedGenres.add(genre);
                } catch (IllegalArgumentException e) {
                    System.out.println(messageSource.getMessage("app.error.genre.invalid", new Object[]{genreIdStr}, locale));
                    return null;
                }
            }
        }
        return selectedGenres;
    }

    private void displayCurrentBook(Book book) {
        String titleLabel = messageSource.getMessage("book.field.title", null, locale);
        String authorLabel = messageSource.getMessage("book.field.author", null, locale);
        String descLabel = messageSource.getMessage("book.field.description", null, locale);
        String genresLabel = messageSource.getMessage("book.field.genres", null, locale);

        List<String> genreNames = book.getGenres().stream()
                .map(Genre::getName)
                .collect(Collectors.toList());

        System.out.println(messageSource.getMessage("app.edit.current", new Object[]{
                String.format("id=%d, %s='%s', %s='%s', %s='%s', %s=%s",
                        book.getId(),
                        titleLabel, book.getTitle(),
                        authorLabel, book.getAuthor() != null ? book.getAuthor().getName() : "Unknown",
                        descLabel, book.getDescription(),
                        genresLabel, genreNames)
        }, locale));
        System.out.println(messageSource.getMessage("app.edit.instruction", null, locale));
    }

    private Author updateAuthor(Book currentBook) {
        System.out.println(messageSource.getMessage("app.author.list", null, locale));
        List<Author> authors = authorService.getAllAuthors();
        for (Author author : authors) {
            System.out.println(author.getId() + ": " + author.getName());
        }

        String authorInput = inputHandler.getInput("app.prompt.author.id", new Object[]{currentBook.getAuthor() != null ? currentBook.getAuthor().getName() : ""});
        if (authorInput.isEmpty()) {
            return currentBook.getAuthor();
        }

        try {
            Long authorId = Long.parseLong(authorInput);
            Author author = authorService.getAuthorById(authorId);
            return author;
        } catch (IllegalArgumentException e) {
            System.out.println(messageSource.getMessage("app.error.author.notfound", new Object[]{authorInput}, locale));
            return null;
        }
    }

    private String updateTitle(Book currentBook) {
        String title = inputHandler.getInput("app.prompt.title", new Object[]{currentBook.getTitle()});
        return title.isEmpty() ? currentBook.getTitle() : title;
    }

    private String updateDescription(Book currentBook) {
        String description = inputHandler.getInput("app.prompt.desc", new Object[]{currentBook.getDescription()});
        return description.isEmpty() ? currentBook.getDescription() : description;
    }

    private List<Genre> updateGenres(Book currentBook) {
        System.out.println(messageSource.getMessage("app.genre.list", null, locale));
        List<Genre> genres = genreService.getAllGenres();
        for (Genre genre : genres) {
            System.out.println(genre.getId() + ": " + genre.getName());
        }

        String genreInput = inputHandler.getInput("app.prompt.genres", null);
        List<Genre> selectedGenres = new ArrayList<>(currentBook.getGenres());

        if (!genreInput.isEmpty()) {
            selectedGenres.clear();
            for (String genreIdStr : genreInput.split(",")) {
                try {
                    Long genreId = Long.parseLong(genreIdStr.trim());
                    Genre genre = genreService.getGenreById(genreId);
                    selectedGenres.add(genre);
                } catch (IllegalArgumentException e) {
                    System.out.println(messageSource.getMessage("app.error.genre.invalid", new Object[]{genreIdStr}, locale));
                    return null;
                }
            }
        }
        return selectedGenres;
    }
}