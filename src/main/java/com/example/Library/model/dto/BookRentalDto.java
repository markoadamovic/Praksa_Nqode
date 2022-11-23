package com.example.Library.model.dto;

import java.time.LocalDate;

public class BookRentalDto {

    private Long id;

    private Long userId;

    private Long bookCopyId;

    private Long bookId;

    private LocalDate rentStart;

    private LocalDate rentEnd;

    public BookRentalDto(Long id, Long bookId, Long userId, Long bookCopy, LocalDate rentStart, LocalDate rentEnd) {
        this.id = id;
        this.userId = userId;
        this.bookCopyId = bookCopy;
        this.bookId = bookId;
        this.rentStart = rentStart;
        this.rentEnd = rentEnd;
    }

    public BookRentalDto(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBookCopy() {
        return bookCopyId;
    }

    public void setBookCopy(Long bookCopyId) {
        this.bookCopyId = bookCopyId;
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

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
}
