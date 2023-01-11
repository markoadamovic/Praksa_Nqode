package com.example.Library.service;

import com.example.Library.exception.NotFoundException;
import com.example.Library.model.dto.BookRentalDto;
import com.example.Library.model.entity.Book;
import com.example.Library.model.entity.BookCopy;
import com.example.Library.model.entity.BookRental;
import com.example.Library.model.entity.User;
import com.example.Library.model.mapper.BookRentalMapper;
import com.example.Library.repository.BookRentalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookRentalServiceImpl implements BookRentalService {

    private final BookRentalRepository bookRentalRepository;

    private final BookCopyService bookCopyService;

    private final UserService userService;

    public BookRentalServiceImpl(BookRentalRepository bookRentalRepository,
                                 BookCopyService bookCopyService,
                                 UserService userService) {
        this.bookRentalRepository = bookRentalRepository;
        this.bookCopyService = bookCopyService;
        this.userService = userService;
    }

    @Override
    public BookRentalDto createBookRental(Long bookId, Long userId) {
        BookCopy bookCopy = bookCopyService.findNotRentedBookCopy(bookId);
        bookCopy.setRented(true);
        User user = userService.findUserModel(userId);
        BookRental bookRental = new BookRental();
        bookRental.setUser(user);
        bookRental.setBookCopy(bookCopy);
        bookRental.setRentStart(LocalDate.now());

        return BookRentalMapper.toDto(bookRentalRepository.save(bookRental));
    }

    public BookRental findBookRental(Long bookRentalId) {
        return bookRentalRepository.findById(bookRentalId)
                .orElseThrow(() -> new NotFoundException(String.format("BookRental with id %s is not found", bookRentalId)));
    }

    @Override
    public void delete(Long bookRentalId) {
        BookRental bookRental = findBookRental(bookRentalId);

        bookRentalRepository.delete(bookRental);
    }

    @Override
    public List<BookRentalDto> getBookRentals() {
        List<BookRental> bookRentals = bookRentalRepository.findAll();
        return bookRentals.stream()
                .map(BookRentalMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookRentalDto endBookRental(Long bookId, Long bookCopyId) {
        BookCopy bookCopy = bookCopyService.findBookCopyByBookId(bookId, bookCopyId);
        bookCopy.setRented(false);
        BookRental bookRental = getBookRentalByBookCopy(bookCopy);
        bookRental.setRentEnd(LocalDate.now());

        return BookRentalMapper.toDto(bookRentalRepository.save(bookRental));
    }

    @Override
    public BookRental getBookRentalByBookCopy(BookCopy bookCopy) {
        return bookRentalRepository.findByBookCopy(bookCopy).orElseThrow(() -> new NotFoundException("No rented books"));
    }

    @Override
    public BookRentalDto getBookRental(Long bookRentalId) {
        return BookRentalMapper.toDto(getBookRentalModelById(bookRentalId));
    }

    @Override
    public BookRentalDto updateRentedBook(Long bookRentalId, BookRentalDto bookRentalDto) {
        BookRental bookRental = getBookRentalModelById(bookRentalId);
        bookRental.setRentEnd(bookRentalDto.getRentEnd());
        bookRental.setRentStart(bookRentalDto.getRentStart());
        bookRental.setBookCopy(bookRental.getBookCopy());
        bookRental.setUser(userService.findUserModel(bookRentalDto.getUserId()));

        return BookRentalMapper.toDto(bookRentalRepository.save(bookRental));
    }

    @Override
    public List<BookRentalDto> getActiveRents() {
        List<BookRental> bookRentals = bookRentalRepository.findActiveRents();
        return bookRentals.stream()
                .map(BookRentalMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookRentalDto> getClosedRents() {
        List<BookRental> bookRentals = bookRentalRepository.findClosedRents();
        return bookRentals.stream()
                .map(BookRentalMapper::toDto)
                .collect(Collectors.toList());
    }


    public BookRental getBookRentalModelById(Long bookRentalId) {
        return bookRentalRepository.findById(bookRentalId)
                .orElseThrow(() -> new NotFoundException(String.format("Rent with id %s is not found", bookRentalId)));
    }

}
