package com.gavrilov.edPlatform.service;

import com.gavrilov.edPlatform.model.ModeratorRoleRequest;
import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.model.enumerator.RequestStatus;

import java.util.List;

public interface ModeratorRoleRequestService {

    ModeratorRoleRequest save(ModeratorRoleRequest request);

    List<ModeratorRoleRequest> findByStatus (RequestStatus status);

    List<ModeratorRoleRequest> findByUser (PlatformUser user);

    void approveUser (PlatformUser user);

    void denyUser (PlatformUser user, String reason);

    Boolean isUserHaveAnyActiveRequest(PlatformUser user);

    Boolean isUserHaveApproved(PlatformUser user);
}
