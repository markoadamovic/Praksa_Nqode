package com.example.Library.model.entity;

import javax.persistence.*;

@Entity
public class BookCopy {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "identification", unique = true, nullable = false)
    private String identification;

    @Column(name = "isRented")
    private boolean isRented;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    public BookCopy() {
    }

    public BookCopy(Long id, Book book, String identification, boolean isRented) {
        this.id = id;
        this.book = book;
        this.identification = identification;
        this.isRented = isRented;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public boolean isRented() {
        return isRented;
    }

    public void setRented(boolean rented) {
        isRented = rented;
    }
}
