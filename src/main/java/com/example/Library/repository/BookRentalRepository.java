package com.example.Library.repository;

import com.example.Library.model.entity.BookCopy;
import com.example.Library.model.entity.BookRental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRentalRepository extends JpaRepository<BookRental, Long> {

    Optional<BookRental> findByBookCopy(BookCopy bookCopy);

    @Query("SELECT br FROM BookRental br WHERE br.rentStart IS NOT NULL and br.rentEnd IS NULL")
    List<BookRental> findActiveRents();

    @Query("SELECT br FROM BookRental br WHERE br.rentEnd IS NOT NULL")
    List<BookRental> findClosedRents();

}
