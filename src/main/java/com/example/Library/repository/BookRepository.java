package com.example.Library.repository;

import com.example.Library.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository <Book, Long> {

    @Query("SELECT count(bc) > 0 FROM BookCopy bc " +
            "WHERE bc.book.id = :bookId")
    Boolean bookHasCopies(@Param("bookId") Long bookId);
}
