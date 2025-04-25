package library.ui;

import library.model.Book;
import library.service.BookService;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Scanner;

/**
 * Console UI demonstrating Constructor-based DI with Spring IoC.
 */
@Component
public class ConsoleInterface {
    private final BookService bookService;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleInterface(BookService bookService) {
        this.bookService = bookService;
    }

    public void start() {
        // Run the main CLI loop for book management
        while (true) {
            displayMenu();
            try {
                int choice = getUserChoice();
                if (handleUserChoice(choice)) {
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }
    }

    private boolean handleUserChoice(int choice) {
        // Handle user menu selection
        switch (choice) {
            case 1 -> displayBooks();
            case 2 -> createBook();
            case 3 -> editBook();
            case 4 -> deleteBook();
            case 0 -> {
                System.out.println("Saving and exiting...");
                return true;
            }
            default -> System.out.println("Invalid choice. Please enter a number between 0 and 4.");
        }
        return false;
    }

    private void displayMenu() {
        System.out.println("\nLibrary Management System");
        System.out.println("1. Display book list");
        System.out.println("2. Create new book");
        System.out.println("3. Edit book");
        System.out.println("4. Delete book");
        System.out.println("0. Save and exit");
        System.out.print("Enter your choice: ");
    }

    private int getUserChoice() {
        return Integer.parseInt(scanner.nextLine().trim());
    }

    private void displayBooks() {
        System.out.println("\nBook List:");
        List<Book> books = bookService.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("No books found.");
        } else {
            books.forEach(System.out::println);
        }
    }

    private void createBook() {
        System.out.print("Enter title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Enter author: ");
        String author = scanner.nextLine().trim();
        System.out.print("Enter description: ");
        String description = scanner.nextLine().trim();

        Book book = new Book(0, title, author, description);
        bookService.createBook(book);
        System.out.println("Book created successfully!");
    }

    private void editBook() {
        System.out.print("Enter book ID to edit: ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid number for ID.");
            return;
        }

        if (!bookService.bookExists(id)) {
            System.out.println("Error: Book with ID " + id + " not found");
            return;
        }
        // Get the current book
        Book currentBook = bookService.getBookById(id);
        System.out.println("Current book: " + currentBook);
        System.out.println("Press Enter to keep current value.");

        // Title
        System.out.print("Enter new title [" + currentBook.getTitle() + "]: ");
        String title = scanner.nextLine().trim();
        title = title.isEmpty() ? currentBook.getTitle() : title;

        // Author
        System.out.print("Enter new author [" + currentBook.getAuthor() + "]: ");
        String author = scanner.nextLine().trim();
        author = author.isEmpty() ? currentBook.getAuthor() : author;

        // Description
        System.out.print("Enter new description [" + currentBook.getDescription() + "]: ");
        String description = scanner.nextLine().trim();
        description = description.isEmpty() ? currentBook.getDescription() : description;

        Book updatedBook = new Book(id, title, author, description);
        bookService.updateBook(id, updatedBook);
        System.out.println("Book updated successfully!");
    }

    private void deleteBook() {
        System.out.print("Enter book ID to delete: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        bookService.deleteBook(id);
        System.out.println("Book deleted successfully!");
    }
}