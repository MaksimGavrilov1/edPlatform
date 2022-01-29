package com.gavrilov.edPlatform.validator;

import com.gavrilov.edPlatform.constant.PlatformValidationUtilities;
import com.gavrilov.edPlatform.model.Tag;
import com.gavrilov.edPlatform.service.TagService;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.engine.groups.ValidationOrder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.validation.Valid;

import java.util.Locale;

import static org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace;

@Component
@RequiredArgsConstructor
public class TagValidator implements Validator {

    private final TagService tagService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == Tag.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        Tag tag = (Tag) target;
        tag.setName(tag.getName().toLowerCase(Locale.ROOT));

        rejectIfEmptyOrWhitespace(errors, "name", PlatformValidationUtilities.NOT_EMPTY_TAG_NAME);

        if (!errors.hasErrors()) {
            if (tag.getName().trim().contains(" ")){
                errors.rejectValue("name", "", PlatformValidationUtilities.FORBIDDEN_SYMBOL_TAG);
            }
            if (tagService.findByName(tag.getName().trim()) != null){
                errors.rejectValue("name", "", PlatformValidationUtilities.DUPLICATE_TAG);
            }
        }

    }
}
