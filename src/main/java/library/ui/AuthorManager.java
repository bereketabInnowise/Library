package library.ui;

import library.model.Author;
import library.service.AuthorService;
import org.springframework.context.MessageSource;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class AuthorManager {
    private final AuthorService authorService;
    private final MessageSource messageSource;
    private final Scanner scanner;
    private final Locale locale;
    private final InputHandler inputHandler;
    private final MenuDisplay menuDisplay;

    public AuthorManager(AuthorService authorService, MessageSource messageSource,
                         Scanner scanner, Locale locale, InputHandler inputHandler) {
        this.authorService = authorService;
        this.messageSource = messageSource;
        this.scanner = scanner;
        this.locale = locale;
        this.inputHandler = inputHandler;
        this.menuDisplay = new MenuDisplay(messageSource, locale);
    }

    public void manageAuthors() {
        menuDisplay.displayAuthorMenu();
        try {
            int choice = inputHandler.getUserChoice();
            switch (choice) {
                case 1 -> displayAuthors();
                case 2 -> createAuthor();
                case 3 -> editAuthor();
                case 4 -> deleteAuthor();
                case 0 -> {
                    return;
                }
                default -> System.out.println(messageSource.getMessage("app.error.choice.invalid", null, locale));
            }
        } catch (NumberFormatException e) {
            System.out.println(messageSource.getMessage("app.error.choice.invalid", null, locale));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void displayAuthors() {
        List<Author> authors = authorService.getAllAuthors();
        if (authors.isEmpty()) {
            System.out.println(messageSource.getMessage("app.error.author.notfound", null, locale));
        } else {
            authors.forEach(System.out::println);
        }
    }

    private void createAuthor() {
        String name = inputHandler.getInput("app.auth-prompt.name", null);
        authorService.createAuthor(name);
        System.out.println(messageSource.getMessage("app.auth-success.create", null, locale));
    }

    private void editAuthor() {
        try {
            long id = inputHandler.getValidId("app.auth-prompt.id");
            String name = inputHandler.getInput("app.auth-prompt.name", null);
            authorService.updateAuthor(id, name);
            System.out.println(messageSource.getMessage("app.auth-success.update", null, locale));
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteAuthor() {
        try {
            long id = inputHandler.getValidId("app.auth-prompt.id");
            authorService.deleteAuthor(id);
            System.out.println(messageSource.getMessage("app.auth-success.delete", null, locale));
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}