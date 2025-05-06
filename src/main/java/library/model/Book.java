package library.model;

import java.util.ArrayList;
import java.util.List;

public class Book {
    private int id;
    private String title;
    private int authorId;
    private String authorName;
    private String description;
    private List<String> genres;

    public Book() {
        this.genres = new ArrayList<>();
    }

    public Book(int id, String title, int authorId, String authorName, String description, List<String> genres) {
        this.id = id;
        this.title = title;
        this.authorId = authorId;
        this.authorName = authorName;
        this.description = description;
        this.genres = genres != null ? genres : new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    @Override
    public String toString() {
        return "Book{id=" + id + ", title='" + title + "', authorId=" + authorId + ", authorName='" + authorName + "', description='" + description + "', genres=" + genres + "}";
    }
}