package library.dto;

import java.util.List;

public class BookCreateDTO {
    private String title;
    private String description;
    private Long authorId;
    private List<Long> genreIds;
    private String imageId;

    // Getters and setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }
    public List<Long> getGenreIds() { return genreIds; }
    public void setGenreIds(List<Long> genreIds) { this.genreIds = genreIds; }
    public String getImageId(){ return imageId;}
    public void setImageId(String imageId){this.imageId = imageId;}
}