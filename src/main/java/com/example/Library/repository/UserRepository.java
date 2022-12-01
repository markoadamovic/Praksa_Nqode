package com.example.Library.repository;

import com.example.Library.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("SELECT count(br) > 0 FROM BookRental br " +
            "WHERE br.user.id = :userId")
    Boolean userHaveRentedBooks(@Param("userId") Long userId);

}
