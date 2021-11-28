package com.gavrilov.edPlatform.repo;

import com.gavrilov.edPlatform.model.PlatformUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<PlatformUser, Long> {
    Optional<PlatformUser> findByUsername(String login);
}