package com.gavrilov.edPlatform.service.moderatorRoleRequestServiceImpl;

import com.gavrilov.edPlatform.model.ModeratorRoleRequest;
import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.model.enumerator.RequestStatus;
import com.gavrilov.edPlatform.repo.ModeratorRoleRequestRepository;
import com.gavrilov.edPlatform.service.ModeratorRoleRequestService;
import com.gavrilov.edPlatform.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ModeratorRoleRequestServiceImpl implements ModeratorRoleRequestService {

    private final ModeratorRoleRequestRepository roleRequestRepository;
    private final UserService userService;

    @Override
    public ModeratorRoleRequest save(ModeratorRoleRequest request) {
        return roleRequestRepository.save(request);
    }

    @Override
    public List<ModeratorRoleRequest> findByStatus(RequestStatus status) {
        return roleRequestRepository.findByStatus(status).orElseGet(Collections::emptyList);
    }

    @Override
    public List<ModeratorRoleRequest> findByUser(PlatformUser user) {
        return roleRequestRepository.findByUser(user).orElse(null);
    }

    @Override
    public void approveUser(PlatformUser user) {
        ModeratorRoleRequest activeReq = roleRequestRepository.findByStatusAndUser(RequestStatus.PENDING, user);
        activeReq.setStatus(RequestStatus.APPROVED);
        roleRequestRepository.save(activeReq);

    }

    @Override
    public void denyUser(PlatformUser user, String reason) {
        ModeratorRoleRequest activeReq = roleRequestRepository.findByStatusAndUser(RequestStatus.PENDING, user);
        activeReq.setStatus(RequestStatus.DECLINED);
        activeReq.setReason(reason);
        roleRequestRepository.save(activeReq);
    }

    @Override
    public Boolean isUserHaveAnyActiveRequest(PlatformUser user) {
        ModeratorRoleRequest activeReq = roleRequestRepository.findByStatusAndUser(RequestStatus.PENDING, user);
        return activeReq != null;

    }

    @Override
    public Boolean isUserHaveApproved(PlatformUser user) {
        ModeratorRoleRequest approvedReq = roleRequestRepository.findByStatusAndUser(RequestStatus.APPROVED, user);
        return approvedReq != null;
    }
}
