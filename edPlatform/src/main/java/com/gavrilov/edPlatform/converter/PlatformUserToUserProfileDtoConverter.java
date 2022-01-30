package com.gavrilov.edPlatform.converter;

import com.gavrilov.edPlatform.dto.UserProfileDto;
import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.model.PlatformUserProfile;
import com.gavrilov.edPlatform.service.AttemptService;
import com.gavrilov.edPlatform.service.CourseService;
import com.gavrilov.edPlatform.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;

@RequiredArgsConstructor
public class PlatformUserToUserProfileDtoConverter implements Converter<PlatformUser, UserProfileDto> {

    private final CourseService courseService;
    private final SubscriptionService subscriptionService;
    private final AttemptService attemptService;

    @Override
    public UserProfileDto convert(PlatformUser source) {
        UserProfileDto dto = new UserProfileDto();
        PlatformUserProfile profile = source.getProfile();
        dto.setName(profile.getName());
        dto.setSurname(profile.getSurname());
        dto.setMiddleName(profile.getMiddleName());
        switch (source.getRole()){
            case ADMIN:
                dto.setRole("Администратор");
                break;
            case STUDENT:
                dto.setRole("Студент");
                break;
            case TEACHER:
                dto.setRole("Преподаватель");
                break;
            case MODERATOR:
                dto.setRole("Модератор");
                break;
        }
        dto.setSelfDescription(profile.getSelfDescription());
        dto.setCreatedCoursesAmount(courseService.countByAuthor(source));
        dto.setSubscriptionsAmount(subscriptionService.countByUser(source));
        long successAmount = attemptService.countUserTestResult(source, true);
        long allAttempts = attemptService.countUserAttempts(source);
        if (allAttempts == 0){
            dto.setTestSuccessRate(0);
            return dto;
        }
        float successRate = (float) successAmount * 100 / allAttempts;
        int result = Math.round(successRate);
        dto.setTestSuccessRate(result);
        return dto;
    }
}
