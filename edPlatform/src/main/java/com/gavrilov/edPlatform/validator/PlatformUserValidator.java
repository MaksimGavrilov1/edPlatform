package com.gavrilov.edPlatform.validator;


import com.gavrilov.edPlatform.constant.PlatformValidationUtilities;
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
        user.setUsername(user.getUsername().trim());
        user.setPassword(user.getPassword().trim());
        // Check the fields of platformUser.
        org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty.platformUser.username", PlatformValidationUtilities.NOT_EMPTY_USERNAME);
        org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty.platformUser.password", PlatformValidationUtilities.NOT_EMPTY_USERNAME_PASSWORD);

        //validate if fields are not empty
        if (!errors.hasFieldErrors("username")){
            if (userService.findByUsername(user.getUsername()) != null){
                errors.rejectValue("username","Duplicate.platformUser.username", PlatformValidationUtilities.DUPLICATE_USERNAME);


            } else if (user.getUsername().length() < PlatformValidationUtilities.MIN_USERNAME_SIZE){
                errors.rejectValue("username", "Size.platformUser.username", PlatformValidationUtilities.LENGTH_USERNAME);
            } else if (user.getPassword().length() < PlatformValidationUtilities.MIN_PASSWORD_SIZE){
                errors.rejectValue("password", "Size.platformUser.username", PlatformValidationUtilities.LENGTH_PASSWORD);
            }

        }
    }
}
