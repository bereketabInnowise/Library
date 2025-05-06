package library.ui;

import library.model.Book;
import library.service.AuthorService;
import library.service.BookService;
import library.service.GenreService;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Scanner;

@Component
public class ConsoleInterface {
    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final MessageSource messageSource;
    private final Scanner scanner = new Scanner(System.in);
    private final Locale locale;

    private final MenuDisplay menuDisplay;
    private final BookManager bookManager;
    private final AuthorManager authorManager;
    private final GenreManager genreManager;
    private final InputHandler inputHandler;

    public ConsoleInterface(BookService bookService, AuthorService authorService,
                            GenreService genreService, MessageSource messageSource) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.genreService = genreService;
        this.messageSource = messageSource;
        this.locale = LocaleSelector.selectLocale(messageSource, scanner);

        this.inputHandler = new InputHandler(scanner, messageSource, locale);
        this.menuDisplay = new MenuDisplay(messageSource, locale);
        this.bookManager = new BookManager(bookService, authorService, genreService,
                messageSource, scanner, locale, inputHandler);
        this.authorManager = new AuthorManager(authorService, messageSource, scanner, locale, inputHandler);
        this.genreManager = new GenreManager(genreService, messageSource, scanner, locale, inputHandler);
    }

    public void start() {
        while (true) {
            menuDisplay.displayMainMenu();
            try {
                int choice = inputHandler.getUserChoice();
                switch (choice) {
                    case 1 -> bookManager.displayBooks();
                    case 2 -> bookManager.createBook();
                    case 3 -> bookManager.editBook();
                    case 4 -> bookManager.deleteBook();
                    case 5 -> authorManager.manageAuthors();
                    case 6 -> genreManager.manageGenres();
                    case 0 -> {
                        System.out.println(messageSource.getMessage("app.success.exit", null, locale));
                        return;
                    }
                    default -> System.out.println(messageSource.getMessage("app.error.choice.invalid", null, locale));
                }
            } catch (NumberFormatException e) {
                System.out.println(messageSource.getMessage("app.error.id.invalid", null, locale));
            }
        }
    }
}