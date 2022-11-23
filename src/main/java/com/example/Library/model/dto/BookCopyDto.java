package com.example.Library.model.dto;


public class BookCopyDto {

    private Long id;

    private Long bookId;

    private String identification;

    private boolean isRented;

    public BookCopyDto(Long id, Long bookId, String identification, boolean isRented) {
        this.id = id;
        this.bookId = bookId;
        this.identification = identification;
        this.isRented = isRented;
    }

    public BookCopyDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
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
