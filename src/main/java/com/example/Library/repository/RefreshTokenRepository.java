package com.example.Library.repository;

import com.example.Library.model.auth.RefreshToken;
import com.example.Library.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByRefreshToken(String token);

    void deleteByUser(User user);

    RefreshToken findByUser(User user);
}
