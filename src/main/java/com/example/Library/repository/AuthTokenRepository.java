package com.example.Library.repository;

import com.example.Library.model.auth.AuthToken;
import com.example.Library.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthTokenRepository extends JpaRepository<AuthToken, Long> {

    @Query("SELECT at FROM AuthToken at WHERE at.user =:user")
    Optional<AuthToken> findByUser(@Param("user") User user);

    @Query("SELECT count(at) > 0 FROM AuthToken at " +
            "WHERE at.user = :user")
    Boolean userHasAccessToken(@Param("user") User user);


}
