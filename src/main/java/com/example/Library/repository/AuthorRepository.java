package com.example.Library.repository;

import com.example.Library.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query("SELECT a FROM Author a JOIN Book b ON a.id = b.author.id WHERE a.id = :authorId")
    Author isAuthorAssignedToBook(@Param("authorId") Long authorId);
}
