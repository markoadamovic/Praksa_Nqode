package com.example.Library.model.dto;

public class BookDto {

    private Long id;
    private String title;
    private String description;
    private Long authorId;

    public BookDto(Long id, String title, String description, Long authorId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.authorId = authorId;
    }

    public BookDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getAuthorId() {
        return this.authorId;
    }

    public void setAuthor(Long authorId) {
        this.authorId = authorId;
    }

}
