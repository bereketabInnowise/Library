package library.ui;

import org.springframework.context.MessageSource;

import java.util.Locale;
import java.util.Scanner;

public class LocaleSelector {
    public static Locale selectLocale(MessageSource messageSource, Scanner scanner) {
        System.out.print(messageSource.getMessage("app.prompt.locale", null, Locale.ENGLISH));
        String input = scanner.nextLine().trim().toLowerCase();
        return switch (input) {
            case "pl" -> Locale.forLanguageTag("pl");
            case "en" -> Locale.ENGLISH;
            default -> {
                System.out.println(messageSource.getMessage("app.error.locale.invalid", null, Locale.ENGLISH));
                yield Locale.ENGLISH;
            }
        };
    }
}