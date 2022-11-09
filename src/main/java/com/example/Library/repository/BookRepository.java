package com.example.Library.repository;

import com.example.Library.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BookRepository extends JpaRepository <Book, Long> {

}
