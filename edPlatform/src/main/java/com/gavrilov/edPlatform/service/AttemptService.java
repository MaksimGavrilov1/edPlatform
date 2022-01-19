package com.gavrilov.edPlatform.service;

import com.gavrilov.edPlatform.model.Attempt;
import com.gavrilov.edPlatform.model.PlatformUser;

import java.util.List;

public interface AttemptService {

    Attempt save(Attempt attempt);

    Attempt findById(Long id);

    List<Attempt> findByUser(PlatformUser user);

}
