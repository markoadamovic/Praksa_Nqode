package com.example.Library.service;

import com.example.Library.model.dto.BookRentalDto;
import org.springframework.stereotype.Service;

@Service
public interface BookRentalService {

    BookRentalDto createBookRental(Long bookCopyId, Long userId);
}
