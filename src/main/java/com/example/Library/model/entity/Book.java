package com.example.Library.model.entity;

import com.example.Library.listener.PersistenceListener;

import javax.persistence.*;
import java.util.List;

@Entity
@EntityListeners(PersistenceListener.class)
public class Book extends Identity {

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @OneToMany(mappedBy = "book")
    private List<BookCopy> bookCopy;

    public Book(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Book() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

}
