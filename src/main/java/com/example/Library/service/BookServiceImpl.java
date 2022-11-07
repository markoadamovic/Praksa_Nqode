package com.example.Library.service;

import com.example.Library.model.dto.BookDto;
import com.example.Library.model.Book;
import com.example.Library.model.mapper.BookMapper;
import com.example.Library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    BookRepository bookRepository;
    @Autowired
    WriterService writerService;

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
    public BookDto createBook(BookDto bookDto, Long writerId) {
        Book book = BookMapper.toEntity(bookDto);
        book.setWriter(writerService.getWriterModel(writerId));

        return BookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public BookDto getBook(Long bookId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            return BookMapper.toDto(book);
        } else {
            return null; //toDO Exception
        }
    }

    @Override
    public void delete(Long bookId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            bookRepository.delete(book);
        }
    }
}
