package library.ui;

import library.model.Genre;
import library.service.GenreService;
import org.springframework.context.MessageSource;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class GenreManager {
    private final GenreService genreService;
    private final MessageSource messageSource;
    private final Scanner scanner;
    private final Locale locale;
    private final InputHandler inputHandler;
    private final MenuDisplay menuDisplay;

    public GenreManager(GenreService genreService, MessageSource messageSource,
                        Scanner scanner, Locale locale, InputHandler inputHandler) {
        this.genreService = genreService;
        this.messageSource = messageSource;
        this.scanner = scanner;
        this.locale = locale;
        this.inputHandler = inputHandler;
        this.menuDisplay = new MenuDisplay(messageSource, locale);
    }

    public void manageGenres() {
        menuDisplay.displayGenreMenu();
        try {
            int choice = inputHandler.getUserChoice();
            switch (choice) {
                case 1 -> displayGenres();
                case 2 -> createGenre();
                case 3 -> editGenre();
                case 4 -> deleteGenre();
                case 0 -> {
                    return;
                }
                default -> System.out.print(messageSource.getMessage("app.error.choice.invalid", null, locale));
            }
        } catch (NumberFormatException e) {
            System.out.print(messageSource.getMessage("app.error.choice.invalid", null, locale));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void displayGenres() {
        List<Genre> genres = genreService.getAllGenres();
        if (genres.isEmpty()) {
            System.out.print(messageSource.getMessage("app.genre.emptylist", null, locale));
        } else {
            genres.forEach(System.out::println);
        }
    }

    private void createGenre() {
        String name = inputHandler.getInput("app.gen-prompt.name", null);
        genreService.createGenre(name);
        System.out.print(messageSource.getMessage("app.gen-success.create", null, locale));
    }

    private void editGenre() {
        try {
            long id = inputHandler.getValidId("app.gen-prompt.id");
            String name = inputHandler.getInput("app.gen-prompt.name", null);
            genreService.updateGenre(id, name);
            System.out.print(messageSource.getMessage("app.gen-success.update", null, locale));
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteGenre() {
        try {
            long id = inputHandler.getValidId("app.gen-prompt.id");
            genreService.deleteGenre(id);
            System.out.print(messageSource.getMessage("app.gen-success.delete", null, locale));
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}