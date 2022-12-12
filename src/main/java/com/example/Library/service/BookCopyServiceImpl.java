package com.example.Library.service;

import com.example.Library.exception.BadRequestException;
import com.example.Library.exception.NotFoundException;
import com.example.Library.model.dto.BookCopyDto;
import com.example.Library.model.entity.Book;
import com.example.Library.model.entity.BookCopy;
import com.example.Library.model.mapper.BookCopyMapper;
import com.example.Library.repository.BookCopyRepository;
import com.example.Library.repository.BookRepository;
import org.springframework.stereotype.Service;

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
    public BookCopyDto createBookCopy(Long bookId, BookCopyDto bookCopyDto) {
        if (bookCopyWithIdentificationExists(bookCopyDto.getIdentification())) {
            throw new BadRequestException(String.format("BookCopy with identificator %s exists",
                    bookCopyDto.getIdentification()));
        }
        BookCopy bookCopy = new BookCopy();
        bookCopy.setRented(bookCopyDto.isRented());
        bookCopy.setBook(bookService.findBookModel(bookId));
        bookCopy.setIdentification(bookCopyDto.getIdentification());

        return BookCopyMapper.toDto(bookCopyRepository.save(bookCopy));
    }

    @Override
    public BookCopyDto getBookCopy(Long bookCopyId) {
        BookCopy bookCopy = findBookCopyModel(bookCopyId);
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
    public void delete(Long bookCopyId) {
        BookCopy bookCopy = findBookCopyModel(bookCopyId);
        if (!bookCopyIsRented(bookCopyId))
            bookCopyRepository.delete(bookCopy);
        else
            throw new BadRequestException("Book copy is rented");
    }

    public boolean bookCopyIsRented(Long bookCopyId) {
        return bookCopyRepository.isBookCopyRented(bookCopyId);
    }

    @Override
    public BookCopyDto updateBookCopy(Long bookCopyId, BookCopyDto bookCopyDto) {
        BookCopy bookCopy = findBookCopyModel(bookCopyId);
        Book book = bookService.findBookModel(bookCopyDto.getBookId());
        bookCopy.setBook(book);
        bookCopy.setIdentification(bookCopyDto.getIdentification());
        bookCopy.setRented(bookCopyDto.isRented());

        return BookCopyMapper.toDto(bookCopyRepository.save(bookCopy));
    }

    public boolean bookCopyWithIdentificationExists(String identification) {
        return bookCopyRepository.findByIdentification(identification).isPresent();
    }

    public BookCopy findBookCopyModel(Long bookCopyId) {
        return bookCopyRepository.findById(bookCopyId)
                .orElseThrow(() -> new NotFoundException(String.format("BookCopy with id %s is not found", bookCopyId)));
    }

    @Override
    public List<BookCopy> findBookCopiesByBook(Long bookId) {
        Book book = bookService.findBookModel(bookId);

        return bookCopyRepository.findByBookId(book.getId());
    }

    @Override
    public BookCopy findBookCopyByBookId(Long bookId, Long bookCopyId) {
        Book book = bookService.findBookModel(bookId);

        return bookCopyRepository.findBookCopyByBookId(bookId, bookCopyId)
                .orElseThrow(() -> new NotFoundException(String.format("BookCopy with id %s is not found", bookCopyId)));
    }

    @Override
    public BookCopy findNotRentedBookCopy(Long bookId) {
        List<BookCopy> bookCopies = findBookCopiesByBook(bookId);
        List<BookCopy> list = bookCopies.stream().filter(x -> !x.isRented()).toList();
        if (list.isEmpty()) {
            throw new NotFoundException(String.format("All books with id %s are rented", bookId));
        }
        return list.get(0);
    }

}
