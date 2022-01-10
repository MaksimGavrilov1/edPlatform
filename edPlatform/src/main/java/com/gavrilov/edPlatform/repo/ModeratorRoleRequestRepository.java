package com.gavrilov.edPlatform.repo;

import com.gavrilov.edPlatform.model.ModeratorRoleRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModeratorRoleRequestRepository extends JpaRepository< ModeratorRoleRequest,Long> {
}
