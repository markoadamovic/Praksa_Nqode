package com.example.Library.repository;

import com.example.Library.model.entity.BookRental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRentalRepository extends JpaRepository<BookRental, Long> {

}
