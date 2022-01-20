package com.gavrilov.edPlatform.validator;


import com.gavrilov.edPlatform.constant.ValidationUtils;
import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class PlatformUserValidator implements Validator {

    private final UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz== PlatformUser.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        PlatformUser user = (PlatformUser) target;

        // Check the fields of platformUser.
        org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty.platformUser.username", ValidationUtils.NOT_EMPTY_USERNAME);
        org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty.platformUser.password", ValidationUtils.NOT_EMPTY_USERNAME_PASSWORD);

        //validate if fields are not empty
        if (!errors.hasFieldErrors("username")){
            if (userService.findByUsername(user.getUsername()) != null){
                errors.rejectValue("username","Duplicate.platformUser.username", ValidationUtils.DUPLICATE_USERNAME);


            } else if (user.getUsername().length() < ValidationUtils.MIN_USERNAME_SIZE){
                errors.rejectValue("username", "Size.platformUser.username", ValidationUtils.LENGTH_USERNAME);
            }

        }
    }
}
