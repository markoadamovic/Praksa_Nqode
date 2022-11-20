package com.example.Library.model.dto;


public class BookCopyDto {

    private Long id;

    private Long bookId;

    private String identification;

    public BookCopyDto(Long id, Long bookId, String identification) {
        this.id = id;
        this.bookId = bookId;
        this.identification = identification;
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

}
