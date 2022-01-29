package com.gavrilov.edPlatform.repo;

import com.gavrilov.edPlatform.model.ModeratorRoleRequest;
import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.model.enumerator.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModeratorRoleRequestRepository extends JpaRepository< ModeratorRoleRequest,Long> {
   Optional<List<ModeratorRoleRequest>>  findByStatus(RequestStatus status);
   Optional<List<ModeratorRoleRequest>> findByUser(PlatformUser user);

   ModeratorRoleRequest findByStatusAndUser(RequestStatus status, PlatformUser user);
}
