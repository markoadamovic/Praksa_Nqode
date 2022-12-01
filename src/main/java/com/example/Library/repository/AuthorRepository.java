package com.example.Library.repository;

import com.example.Library.model.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository <Author, Long> {

    @Query("SELECT count(b) > 0 FROM Book b " +
            "WHERE b.author.id = :authorId")
    Boolean isAuthorAssignedToBook(@Param("authorId") Long authorId);
}
