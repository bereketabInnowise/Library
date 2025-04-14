package library.ui;

import library.model.Book;
import library.service.BookService;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Scanner;
import org.springframework.context.MessageSource;
import java.util.Locale;

/**
 * Console UI demonstrating Constructor-based DI with Spring IoC.
 */
@Component
public class ConsoleInterface {
    private final BookService bookService;
    private final Scanner scanner = new Scanner(System.in);
    private final Locale locale;
    private final MessageSource messageSource;

    public ConsoleInterface(BookService bookService, MessageSource messageSource) {

        this.bookService = bookService;
        this.messageSource = messageSource;
        this.locale = selectLocale();
    }
    private Locale selectLocale(){
        System.out.print(messageSource.getMessage("app.prompt.locale", null, Locale.ENGLISH));
        String input = scanner.nextLine().trim().toLowerCase();
        return switch (input){
            case "pl" -> Locale.forLanguageTag("pl");
            case "en" -> Locale.ENGLISH;
            default -> {
                System.out.println(messageSource.getMessage("app.error.locale.invalid", null, Locale.ENGLISH));
                yield Locale.ENGLISH;
            }
        };
    }
    public void start() {
        while (true) {
            displayMenu();
            try {
                int choice = getUserChoice();
                switch (choice) {
                    case 1 -> displayBooks();
                    case 2 -> createBook();
                    case 3 -> editBook();
                    case 4 -> deleteBook();
                    case 0 -> {
                        System.out.println(messageSource.getMessage("app.success.exit", null, locale));
                        return;
                    }
                    default -> System.out.println(messageSource.getMessage("app.error.choice.invalid", null, locale));
                }
            } catch (NumberFormatException e) {
                System.out.println(messageSource.getMessage("app.error.choice.invalid", null, locale));
            }
        }
    }

    private void displayMenu() {
        System.out.println("\n" + messageSource.getMessage("app.welcome", null, locale));
        System.out.println(messageSource.getMessage("app.menu.display", null, locale));
        System.out.println(messageSource.getMessage("app.menu.create", null, locale));
        System.out.println(messageSource.getMessage("app.menu.edit", null, locale));
        System.out.println(messageSource.getMessage("app.menu.delete", null, locale));
        System.out.println(messageSource.getMessage("app.menu.exit", null, locale));
        System.out.print(messageSource.getMessage("app.prompt.choice", null, locale));
    }

    private int getUserChoice() {
        return Integer.parseInt(scanner.nextLine().trim());
    }

    private void displayBooks() {
        System.out.println("\n" + messageSource.getMessage("app.book.list", null, locale));
        List<Book> books = bookService.getAllBooks();
        if (books.isEmpty()) {
            System.out.println(messageSource.getMessage("app.book.none", null, locale));
        } else {
            String titleLabel = messageSource.getMessage("book.field.title", null, locale);
            String authorLabel = messageSource.getMessage("book.field.author", null, locale);
            String descLabel = messageSource.getMessage("book.field.description", null, locale);
            for (Book book : books) {
                System.out.printf("{id=%d, %s='%s', %s='%s', %s='%s'}%n",
                        book.getId(),
                        titleLabel, book.getTitle(),
                        authorLabel, book.getAuthor(),
                        descLabel, book.getDescription());
            }
        }
    }

    private void createBook() {
        System.out.print(messageSource.getMessage("app.prompt.title", new Object[]{""}, locale));
        String title = scanner.nextLine().trim();
        System.out.print(messageSource.getMessage("app.prompt.author", new Object[]{""}, locale));
        String author = scanner.nextLine().trim();
        System.out.print(messageSource.getMessage("app.prompt.desc", new Object[]{""}, locale));
        String description = scanner.nextLine().trim();

        Book book = new Book(0, title, author, description);
        bookService.createBook(book);
        System.out.println(messageSource.getMessage("app.success.create", null, locale));
    }

    private void editBook() {
        System.out.print(messageSource.getMessage("app.prompt.id.edit", null, locale));
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println(messageSource.getMessage("app.error.id.invalid", null, locale));
            return;
        }

        if (!bookService.bookExists(id)) {
            System.out.println(messageSource.getMessage("app.error.id.notfound", new Object[]{id}, locale));
            return;
        }
        // Get the current book
        Book currentBook = bookService.getBookById(id);
        String titleLabel = messageSource.getMessage("book.field.title", null, locale);
        String authorLabel = messageSource.getMessage("book.field.author", null, locale);
        String descLabel = messageSource.getMessage("book.field.description", null, locale);
        System.out.println(messageSource.getMessage("app.edit.current", new Object[]{
                String.format("id=%d, %s='%s', %s='%s', %s='%s'",
                        currentBook.getId(),
                        titleLabel, currentBook.getTitle(),
                        authorLabel, currentBook.getAuthor(),
                        descLabel, currentBook.getDescription())
        }, locale));
        System.out.println(messageSource.getMessage("app.edit.instruction", null, locale));

        // Title
        System.out.print(messageSource.getMessage("app.prompt.title", new Object[]{currentBook.getTitle()}, locale));
        String title = scanner.nextLine().trim();
        title = title.isEmpty() ? currentBook.getTitle() : title;

        // Author
        System.out.print(messageSource.getMessage("app.prompt.author", new Object[]{currentBook.getAuthor()}, locale));
        String author = scanner.nextLine().trim();
        author = author.isEmpty() ? currentBook.getAuthor() : author;

        // Description
        System.out.print(messageSource.getMessage("app.prompt.desc", new Object[]{currentBook.getDescription()}, locale));
        String description = scanner.nextLine().trim();
        description = description.isEmpty() ? currentBook.getDescription() : description;

        Book updatedBook = new Book(id, title, author, description);
        bookService.updateBook(id, updatedBook);
        System.out.println(messageSource.getMessage("app.success.update", null, locale));
    }

    private void deleteBook() {
        System.out.print(messageSource.getMessage("app.prompt.id.delete", null, locale));
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine().trim());
        }catch (NumberFormatException e){
            System.out.println(messageSource.getMessage("app.error.id.invalid", null, locale));
            return;
        }
        bookService.deleteBook(id);
        System.out.println(messageSource.getMessage("app.success.delete", null, locale));
    }
}