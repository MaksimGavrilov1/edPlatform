package com.gavrilov.edPlatform.repositories;

import com.gavrilov.edPlatform.models.PlatformUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<PlatformUser, Long> {
    Optional<PlatformUser> findByLogin(String login);
}