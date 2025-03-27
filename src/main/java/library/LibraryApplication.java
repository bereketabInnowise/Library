package library;

import library.config.AppConfig;
import library.repository.BookRepository;
import library.ui.ConsoleInterface;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

public class LibraryApplication {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);
        // Set CSV path as an environment property
        ConfigurableEnvironment env = context.getEnvironment();
        String csvPath = env.getProperty("csvPath", "src/main/resources/books.csv");
        context.getBean(BookRepository.class).setCsvPath(csvPath);

        ConsoleInterface consoleInterface = context.getBean(ConsoleInterface.class);
        consoleInterface.start();

        context.close();
    }
}