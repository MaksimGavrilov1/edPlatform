package com.gavrilov.edPlatform.service.moderatorRoleRequestServiceImpl;

import com.gavrilov.edPlatform.model.ModeratorRoleRequest;
import com.gavrilov.edPlatform.repo.ModeratorRoleRequestRepository;
import com.gavrilov.edPlatform.service.ModeratorRoleRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModeratorRoleRequestServiceImpl implements ModeratorRoleRequestService {

    private final ModeratorRoleRequestRepository roleRequestRepository;

    @Override
    public ModeratorRoleRequest save(ModeratorRoleRequest request) {
        return roleRequestRepository.save(request);
    }
}
