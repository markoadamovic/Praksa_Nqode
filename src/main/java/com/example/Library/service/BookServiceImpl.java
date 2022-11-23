package com.example.Library.service;

import com.example.Library.exception.BadRequestException;
import com.example.Library.exception.NotFoundException;
import com.example.Library.model.entity.Author;
import com.example.Library.model.dto.BookDto;
import com.example.Library.model.entity.Book;
import com.example.Library.model.mapper.BookMapper;
import com.example.Library.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final AuthorService authorService;

    public BookServiceImpl(BookRepository bookRepository, AuthorService authorService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
    }

    @Override
    public BookDto createBook(BookDto bookDto, Long authorId) {
        Book book = BookMapper.toEntity(bookDto);
        book.setAuthor(authorService.findAuthorModel(authorId));

        return BookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> getBooks() {
        List<Book> bookList = bookRepository.findAll();

        return bookList.stream()
                .map(BookMapper::toDto)
                .collect(Collectors.toList());
    }

    public Book findBookModel(Long bookId){
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException(String.format("Book with id %s is not found", bookId)));
    }

    @Override
    public BookDto getBook(Long bookId) {
        Book book = findBookModel(bookId);

        return BookMapper.toDto(book);
    }

    @Override
    public void delete(Long bookId) {
        Book book = findBookModel(bookId);
        if(bookHasCopies(bookId)){
            throw new BadRequestException(String.format("Book with id %s has bookCopies assigned to it", bookId));
        }
        bookRepository.delete(book);
    }

    public boolean bookHasCopies(Long bookId) {
        return bookRepository.bookHasCopies(bookId);
    }

    @Override
    public BookDto updateBook(BookDto bookDto, Long bookId) {
        Book book = findBookModel(bookId);
        book.setTitle(bookDto.getTitle());
        book.setDescription(bookDto.getDescription());

        Author author = authorService.findAuthorModel(bookDto.getAuthorId());
        book.setAuthor(author);

        return BookMapper.toDto(bookRepository.save(book));
    }

}
