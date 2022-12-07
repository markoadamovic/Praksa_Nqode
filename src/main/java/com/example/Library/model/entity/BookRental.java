package com.example.Library.model.entity;

import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class BookRental extends Identity {

    @ManyToOne
    @JoinColumn(name = "book_copy_id")
    private BookCopy bookCopy;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "rent_start")
    private LocalDate rentStart;

    @Column(name = "rent_end")
    private LocalDate rentEnd;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BookCopy getBookCopy() {
        return bookCopy;
    }

    public void setBookCopy(BookCopy bookCopy) {
        this.bookCopy = bookCopy;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BookRental(BookCopy bookCopy, User user, LocalDate rentStart, LocalDate rentEnd) {
        this.bookCopy = bookCopy;
        this.user = user;
        this.rentStart = rentStart;
        this.rentEnd = rentEnd;
    }

    public BookRental() {

    }

    public LocalDate getRentStart() {
        return rentStart;
    }

    public void setRentStart(LocalDate rentStart) {
        this.rentStart = rentStart;
    }

    public LocalDate getRentEnd() {
        return rentEnd;
    }

    public void setRentEnd(LocalDate rentEnd) {
        this.rentEnd = rentEnd;
    }

    @PrePersist
    public void setCreatedAt() {
        createdAt = LocalDateTime.now();
        createdBy = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }

}
