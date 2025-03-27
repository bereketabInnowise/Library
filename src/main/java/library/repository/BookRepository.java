package library.repository;

import library.model.Book;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//Repository for managing book data in CSV file.
//Demonstrates Setter-based Dependency Injection for csvPath.
@Repository
public class BookRepository {
    private String csvPath; // Here the object is injected via setter.
    private final CsvMapper csvMapper = new CsvMapper();
    private final CsvSchema schema = CsvSchema.builder()
            .addColumn("id")
            .addColumn("title")
            .addColumn("author")
            .addColumn("description")
            .build()
            .withHeader();
    public BookRepository(){}

    public void setCsvPath(String csvPath) {
        this.csvPath = csvPath;
    }
    public List<Book> findAll() {
        try (MappingIterator<Book> iterator = csvMapper.readerFor(Book.class)
                .with(schema)
                .readValues(new File(csvPath))) {
            return iterator.readAll();
        } catch (IOException e) {
            throw new RuntimeException("Error reading books from CSV", e);
        }
    }

    public void saveAll(List<Book> books) {
        try {
            csvMapper.writer(schema).writeValue(new File(csvPath), books);
        } catch (IOException e) {
            throw new RuntimeException("Error writing books to CSV: "+ e.getMessage(), e);
        }
    }

    public int getNextId() {
        List<Book> books = findAll();
        return books.stream()
                .mapToInt(Book::getId)
                .max()
                .orElse(0) + 1;
    }
}