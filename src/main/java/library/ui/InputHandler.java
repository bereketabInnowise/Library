package library.ui;

import org.springframework.context.MessageSource;

import java.util.Locale;
import java.util.Scanner;

public class InputHandler {
    private final Scanner scanner;
    private final MessageSource messageSource;
    private final Locale locale;

    public InputHandler(Scanner scanner, MessageSource messageSource, Locale locale) {
        this.scanner = scanner;
        this.messageSource = messageSource;
        this.locale = locale;
    }

    public int getUserChoice() {
        return Integer.parseInt(scanner.nextLine().trim());
    }

    public int getValidId(String promptKey) {
        System.out.print(messageSource.getMessage(promptKey, null, locale));
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            throw new NumberFormatException(messageSource.getMessage("app.error.id.invalid", null, locale));
        }
    }

    public String getInput(String promptKey, Object[] args) {
        System.out.print(messageSource.getMessage(promptKey, args, locale));
        return scanner.nextLine().trim();
    }
}