package library.ui;

import library.model.Book;
import library.service.AuthorService;
import library.service.BookService;
import library.service.GenreService;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

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
                System.out.printf("Book{id=%d, %s='%s', %s='%s', %s='%s', %s=%s}%n",
                        book.getId(),
                        titleLabel, book.getTitle(),
                        authorLabel, book.getAuthorName(),
                        descLabel, book.getDescription(),
                        genresLabel, book.getGenres());
            }
        }
    }

    public void createBook() {
        // Select author
        int authorId = selectAuthor();
        if (authorId == -1) return;

        // Book details
        String title = inputHandler.getInput("app.prompt.title", new Object[]{""});
        String description = inputHandler.getInput("app.prompt.desc", new Object[]{""});

        // Select genres
        List<String> genreNames = selectGenres();
        if (genreNames == null) return;

        Book book = new Book(0, title, authorId, null, description, genreNames);
        bookService.createBook(book);
        System.out.println(messageSource.getMessage("app.success.create", null, locale));
    }

    public void editBook() {
        int id;
        try {
            id = inputHandler.getValidId("app.prompt.id.edit");
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
        int authorId = updateAuthor(currentBook);
        if (authorId == -1) return;

        // Book details
        String title = updateTitle(currentBook);
        String description = updateDescription(currentBook);

        // Select genres
        List<String> genreNames = updateGenres(currentBook);
        if (genreNames == null) return;

        Book updatedBook = new Book(id, title, authorId, null, description, genreNames);
        bookService.updateBook(id, updatedBook);
        System.out.println(messageSource.getMessage("app.success.update", null, locale));
    }

    public void deleteBook() {
        int id;
        try {
            id = inputHandler.getValidId("app.prompt.id.delete");
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

    private int selectAuthor() {
        System.out.println(messageSource.getMessage("app.author.list", null, locale));
        List<AuthorService.Author> authors = authorService.getAllAuthors();
        if (authors.isEmpty()) {
            System.out.println(messageSource.getMessage("app.author.emptylist", null, locale));
            return -1;
        }

        for (AuthorService.Author author : authors) {
            System.out.println(author.getId() + ": " + author.getName());
        }

        try {
            int authorId = inputHandler.getValidId("app.prompt.author.id");
            if (!authorService.authorExists(authorId)) {
                System.out.println(messageSource.getMessage("app.error.author.notfound", new Object[]{authorId}, locale));
                return -1;
            }
            return authorId;
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    private List<String> selectGenres() {
        System.out.println(messageSource.getMessage("app.genre.list", null, locale));
        List<GenreService.Genre> genres = genreService.getAllGenres();
        for (GenreService.Genre genre : genres) {
            System.out.println(genre.getId() + ": " + genre.getName());
        }

        String genreInput = inputHandler.getInput("app.prompt.genres", null);
        List<String> genreNames = new ArrayList<>();
        if (!genreInput.isEmpty()) {
            for (String genreIdStr : genreInput.split(",")) {
                try {
                    int genreId = Integer.parseInt(genreIdStr.trim());
                    GenreService.Genre genre = genreService.getGenreById(genreId);
                    genreNames.add(genre.getName());
                } catch (IllegalArgumentException e) {
                    System.out.println(messageSource.getMessage("app.error.genre.invalid", new Object[]{genreIdStr}, locale));
                    return null;
                }
            }
        }
        return genreNames;
    }

    private void displayCurrentBook(Book book) {
        String titleLabel = messageSource.getMessage("book.field.title", null, locale);
        String authorLabel = messageSource.getMessage("book.field.author", null, locale);
        String descLabel = messageSource.getMessage("book.field.description", null, locale);
        String genresLabel = messageSource.getMessage("book.field.genres", null, locale);

        System.out.println(messageSource.getMessage("app.edit.current", new Object[]{
                String.format("id=%d, %s='%s', %s='%s', %s='%s', %s=%s",
                        book.getId(),
                        titleLabel, book.getTitle(),
                        authorLabel, book.getAuthorName(),
                        descLabel, book.getDescription(),
                        genresLabel, book.getGenres())
        }, locale));
        System.out.println(messageSource.getMessage("app.edit.instruction", null, locale));
    }

    private int updateAuthor(Book currentBook) {
        System.out.println(messageSource.getMessage("app.author.list", null, locale));
        List<AuthorService.Author> authors = authorService.getAllAuthors();
        for (AuthorService.Author author : authors) {
            System.out.println(author.getId() + ": " + author.getName());
        }

        String authorInput = inputHandler.getInput("app.prompt.author.id", new Object[]{currentBook.getAuthorName()});
        if (authorInput.isEmpty()) {
            return currentBook.getAuthorId();
        }

        try {
            int authorId = Integer.parseInt(authorInput);
            if (!authorService.authorExists(authorId)) {
                System.out.println(messageSource.getMessage("app.error.author.notfound", new Object[]{authorId}, locale));
                return -1;
            }
            return authorId;
        } catch (NumberFormatException e) {
            System.out.println(messageSource.getMessage("app.error.id.invalid", null, locale));
            return -1;
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

    private List<String> updateGenres(Book currentBook) {
        System.out.println(messageSource.getMessage("app.genre.list", null, locale));
        List<GenreService.Genre> genres = genreService.getAllGenres();
        for (GenreService.Genre genre : genres) {
            System.out.println(genre.getId() + ": " + genre.getName());
        }

        String genreInput = inputHandler.getInput("app.prompt.genres", null);
        List<String> genreNames = new ArrayList<>(currentBook.getGenres());

        if (!genreInput.isEmpty()) {
            genreNames.clear();
            for (String genreIdStr : genreInput.split(",")) {
                try {
                    int genreId = Integer.parseInt(genreIdStr.trim());
                    GenreService.Genre genre = genreService.getGenreById(genreId);
                    genreNames.add(genre.getName());
                } catch (IllegalArgumentException e) {
                    System.out.println(messageSource.getMessage("app.error.genre.invalid", new Object[]{genreIdStr}, locale));
                    return null;
                }
            }
        }
        return genreNames;
    }
}