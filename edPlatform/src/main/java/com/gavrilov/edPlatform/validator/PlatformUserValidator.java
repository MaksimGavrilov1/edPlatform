package com.gavrilov.edPlatform.validator;


import com.gavrilov.edPlatform.constant.ValidationConstants;
import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class PlatformUserValidator implements Validator {

    private final int MIN_USERNAME_SIZE = 8;
    private final UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz== PlatformUser.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        PlatformUser user = (PlatformUser) target;

        // Check the fields of platformUser.
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty.platformUser.username", ValidationConstants.NOT_EMPTY_USERNAME);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty.platformUser.password", ValidationConstants.NOT_EMPTY_USERNAME_PASSWORD);

        //validate if fields are not empty
        if (!errors.hasFieldErrors("username")){
            if (userService.findByUsername(user.getUsername()) != null){
                errors.rejectValue("username","Duplicate.platformUser.username", ValidationConstants.DUPLICATE_USERNAME);


            } else if (user.getUsername().length() < MIN_USERNAME_SIZE){
                errors.rejectValue("username", "Size.platformUser.username", ValidationConstants.LENGTH_USERNAME);
            }

        }
    }
}
