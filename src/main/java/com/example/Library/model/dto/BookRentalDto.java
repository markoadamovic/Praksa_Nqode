package com.example.Library.model.dto;

public class BookRentalDto {

    private Long id;

    private boolean isRented;

    private Long userId;

    private Long bookCopyId;

    public BookRentalDto(Long id, boolean isRented, Long user, Long bookCopy) {
        this.id = id;
        this.isRented = isRented;
        this.userId = user;
        this.bookCopyId = bookCopy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isRented() {
        return isRented;
    }

    public void setRented(boolean rented) {
        isRented = rented;
    }

    public Long getUser() {
        return userId;
    }

    public void setUser(Long userId) {
        this.userId = userId;
    }

    public Long getBookCopy() {
        return bookCopyId;
    }

    public void setBookCopy(Long bookCopyId) {
        this.bookCopyId = bookCopyId;
    }

}
