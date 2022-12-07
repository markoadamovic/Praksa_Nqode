package com.example.Library.repository;

import com.example.Library.model.auth.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthTokenRepository extends JpaRepository<AuthToken, Long> {

    Optional<AuthToken> findByUserId(@Param("userId") Long userId);

    @Query("SELECT count(at) > 0 FROM AuthToken at " +
            "WHERE at.user.id = :userId")
    Boolean userHasAccessToken(@Param("userId") Long userId);

    Optional<AuthToken> findByRefreshToken(String token);

}
