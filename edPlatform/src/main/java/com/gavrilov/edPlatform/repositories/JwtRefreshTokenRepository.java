package com.gavrilov.edPlatform.repositories;

import com.gavrilov.edPlatform.models.JwtRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtRefreshTokenRepository extends JpaRepository<JwtRefreshToken, Long> {
    JwtRefreshToken findByLogin(String login);

    void deleteByLogin(String login);
}