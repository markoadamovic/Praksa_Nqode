package com.example.Library.repository;

import com.example.Library.model.entity.Book;
import com.example.Library.model.entity.BookCopy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookCopyRepository extends JpaRepository<BookCopy, Long> {

    Optional<BookCopy> findByIdentification(String identification);

    List<BookCopy> findByBook(Book book);

    @Query("SELECT bc FROM BookCopy bc where bc.book.id = :bookId and bc.id= :bookCopyId")
    BookCopy findBookCopyByBookId(@Param("bookId") Long bookId,
                                  @Param("bookCopyId") Long bookCopyId);

    @Query("SELECT count(br) > 0 FROM BookRental br " +
            "WHERE br.bookCopy.id = :bookCopyId")
    Boolean isBookCopyRented(@Param("bookCopyId") Long bookCopyId);

}
