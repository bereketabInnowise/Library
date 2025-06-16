package library.ui;

import org.springframework.context.MessageSource;

import java.util.Locale;

public class MenuDisplay {
    private final MessageSource messageSource;
    private final Locale locale;

    public MenuDisplay(MessageSource messageSource, Locale locale) {
        this.messageSource = messageSource;
        this.locale = locale;
    }

    public void displayMainMenu() {
        System.out.println("\n" + messageSource.getMessage("app.welcome", null, locale));
        System.out.println(messageSource.getMessage("app.menu.display", null, locale));
        System.out.println(messageSource.getMessage("app.menu.create", null, locale));
        System.out.println(messageSource.getMessage("app.menu.edit", null, locale));
        System.out.println(messageSource.getMessage("app.menu.delete", null, locale));
        System.out.println(messageSource.getMessage("app.menu.authors", null, locale));
        System.out.println(messageSource.getMessage("app.menu.genres", null, locale));
        System.out.println(messageSource.getMessage("app.menu.exit", null, locale));
        System.out.print(messageSource.getMessage("app.prompt.choice", null, locale));
    }

    public void displayAuthorMenu() {
        System.out.println(messageSource.getMessage("app.auth-menu.gate", null, locale));
        System.out.println(messageSource.getMessage("app.auth-menu.display", null, locale));
        System.out.println(messageSource.getMessage("app.auth-menu.create", null, locale));
        System.out.println(messageSource.getMessage("app.auth-menu.edit", null, locale));
        System.out.println(messageSource.getMessage("app.auth-menu.delete", null, locale));
        System.out.println(messageSource.getMessage("app.auth-menu.back", null, locale));
        System.out.print(messageSource.getMessage("app.auth-menu.prompt", null, locale));
    }

    public void displayGenreMenu() {
        System.out.println(messageSource.getMessage("app.gen-menu.gate", null, locale));
        System.out.println(messageSource.getMessage("app.gen-menu.display", null, locale));
        System.out.println(messageSource.getMessage("app.gen-menu.create", null, locale));
        System.out.println(messageSource.getMessage("app.gen-menu.edit", null, locale));
        System.out.println(messageSource.getMessage("app.gen-menu.delete", null, locale));
        System.out.println(messageSource.getMessage("app.gen-menu.back", null, locale));
        System.out.print(messageSource.getMessage("app.gen-menu.prompt", null, locale));
    }
}