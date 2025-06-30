package library.dto;

import java.util.List;

public class BookDTO {
    private Long id;
    private String title;
    private String description;
    private AuthorDTO author;
    private List<GenreDTO> genres;
    private String imageId;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public AuthorDTO getAuthor() { return author; }
    public void setAuthor(AuthorDTO author) { this.author = author; }
    public List<GenreDTO> getGenres() { return genres; }
    public void setGenres(List<GenreDTO> genres) { this.genres = genres; }
    public String getImageId(){ return imageId;}
    public void setImageId(String imageId) {this.imageId = imageId;}
}