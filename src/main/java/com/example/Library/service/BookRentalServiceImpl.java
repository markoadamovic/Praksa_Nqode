package com.example.Library.service;

import com.example.Library.model.dto.BookRentalDto;
import com.example.Library.model.entity.BookCopy;
import com.example.Library.model.entity.BookRental;
import com.example.Library.model.entity.User;
import com.example.Library.model.mapper.BookRentalMapper;
import com.example.Library.repository.BookRentalRepository;
import org.springframework.stereotype.Service;

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
    public BookRentalDto createBookRental(Long bookCopyId, Long userId) {
        BookCopy bookCopy = bookCopyService.findBookCopyModel(bookCopyId);
        User user = userService.findUserModel(userId);
        BookRental bookRental = new BookRental();
        bookRental.setRented(true);
        bookRental.setBookCopy(bookCopy);
        bookRental.setUser(user);

        return BookRentalMapper.toDto(bookRentalRepository.save(bookRental));
    }
}
