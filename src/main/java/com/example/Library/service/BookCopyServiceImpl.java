package com.example.Library.service;

import com.example.Library.exception.BadRequestException;
import com.example.Library.exception.NotFoundException;
import com.example.Library.model.dto.BookCopyDto;
import com.example.Library.model.entity.Book;
import com.example.Library.model.entity.BookCopy;
import com.example.Library.model.mapper.BookCopyMapper;
import com.example.Library.model.mapper.BookMapper;
import com.example.Library.model.mapper.UserMapper;
import com.example.Library.repository.BookCopyRepository;
import com.example.Library.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookCopyServiceImpl implements BookCopyService {

    private final BookCopyRepository bookCopyRepository;

    private final BookRepository bookRepository;

    private final BookService bookService;

    public BookCopyServiceImpl(BookCopyRepository bookCopyRepository,
                               BookRepository bookRepository,
                               BookService bookService) {
        this.bookRepository = bookRepository;
        this.bookCopyRepository = bookCopyRepository;
        this.bookService = bookService;
    }

    @Override
    public BookCopyDto createBookCopy(Long bookId, String identification) {
        if(bookCopyWithIdentificationExists(identification)){
            throw new BadRequestException("BookCopy with identificator " + identification + " exists");
        }
        BookCopy bookCopy = new BookCopy();
        bookCopy.setBook(bookService.findBookModel(bookId));
        bookCopy.setIdentification(identification);

        return BookCopyMapper.toDto(bookCopyRepository.save(bookCopy));
    }

    @Override
    public BookCopyDto getBookCopy(Long id) {
        BookCopy bookCopy = findBookCopyModel(id);
        return BookCopyMapper.toDto(bookCopy);
    }

    @Override
    public List<BookCopyDto> getBookCopies() {
        List<BookCopy> bookCopies = bookCopyRepository.findAll();

        return bookCopies.stream()
                .map(BookCopyMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        BookCopy bookCopy = findBookCopyModel(id);
        bookCopyRepository.delete(bookCopy);
    }

    @Override
    public BookCopyDto updateBookCopy(Long id, BookCopyDto bookCopyDto) {
        BookCopy bookCopy = findBookCopyModel(id);
        Book book = bookService.findBookModel(bookCopyDto.getBookId());
        bookCopy.setBook(book);
        bookCopy.setIdentification(bookCopyDto.getIdentification());

        return BookCopyMapper.toDto(bookCopyRepository.save(bookCopy));
    }

    public boolean bookCopyWithIdentificationExists(String identification) {
        return bookCopyRepository.findByIdentification(identification).isPresent();
    }

    public BookCopy findBookCopyModel(Long id) {
        return bookCopyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("BookCopy with id " +id + " not found"));
    }

}
