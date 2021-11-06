package com.gavrilov.edPlatform.repositories;

import com.gavrilov.edPlatform.models.PlatformUserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformUserProfileRepository extends JpaRepository<PlatformUserProfile, Long> {
}