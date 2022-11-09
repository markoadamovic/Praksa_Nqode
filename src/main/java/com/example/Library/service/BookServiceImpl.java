package com.example.Library.service;

import com.example.Library.model.entity.Author;
import com.example.Library.model.dto.BookDto;
import com.example.Library.model.entity.Book;
import com.example.Library.model.mapper.BookMapper;
import com.example.Library.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorService authorService;

    public BookServiceImpl(BookRepository bookRepository,
                           AuthorService authorService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
    }

    @Override
    public BookDto createBook(BookDto bookDto, Long authorId) {
        Book book = BookMapper.toEntity(bookDto);
        book.setAuthor(authorService.getAuthorModel(authorId));
        return BookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> getBooks() {
        List<Book> bookList = bookRepository.findAll();
        List<BookDto> bookDtos = new ArrayList<>();
        for (Book book : bookList) {
            BookDto bookDto = BookMapper.toDto(book);
            bookDtos.add(bookDto);
        }
        return bookDtos;
    }

    @Override
    public void delete(Long bookId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isEmpty()) {
            throw new RuntimeException("Book not found");
        }
        Book book = bookOptional.get();
        bookRepository.delete(book);
    }

    @Override
    public BookDto updateBook(BookDto bookDto, Long bookId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isEmpty()) {
            return null; //TODO
        }

        Book book = bookOptional.get();
        book.setTitle(bookDto.getTitle());
        book.setDescription(bookDto.getDescription());

        Author author = authorService.getAuthorModel(bookDto.getAuthorId());
        book.setAuthor(author);

        return BookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public BookDto getBook(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException());
        return BookMapper.toDto(book);
    }

}
