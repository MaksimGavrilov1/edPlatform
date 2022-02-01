package com.gavrilov.edPlatform.validator;

import com.gavrilov.edPlatform.constant.PlatformValidationUtilities;
import com.gavrilov.edPlatform.model.Tag;
import com.gavrilov.edPlatform.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

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

        rejectIfEmptyOrWhitespace(errors, "name", "tag.name.empty");

        if (!errors.hasErrors()) {
            if (tag.getName().trim().length() > PlatformValidationUtilities.MAX_TAG_SIZE) {
                errors.rejectValue("name", "tag.name.length");
            }
            if (tag.getName().trim().contains(" ")) {
                errors.rejectValue("name", "tag.name.forbiddenSymbol");
            }
            if (tagService.findByName(tag.getName().trim()) != null) {
                errors.rejectValue("name", "tag.name.nonUnique");
            }
        }

    }
}
