package com.example.Library.model.dto;

import com.example.Library.model.Book;
import com.example.Library.model.Writer;
import com.example.Library.repository.WriterRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class BookDto {

    private Long id;
    private String title;
    private String description;

    private Long writerId;

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

    public Long getWriterId() {
        return this.writerId;
    }

    public void setWriter(Long writer) {
        this.writerId = writer;
    }

    public BookDto() {
    }

    public BookDto(Long id, String title, String description, Long writerId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.writerId = writerId;
    }

}
