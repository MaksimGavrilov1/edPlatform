package com.gavrilov.edPlatform.repositories;

import com.gavrilov.edPlatform.models.PlatformUserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlatformUserProfileRepository extends JpaRepository<PlatformUserProfile, Long> {
    @Override
    Optional<PlatformUserProfile> findById(Long aLong);
}