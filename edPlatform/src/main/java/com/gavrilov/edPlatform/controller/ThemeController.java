package com.gavrilov.edPlatform.controller;

import com.gavrilov.edPlatform.dto.FormTheme;
import com.gavrilov.edPlatform.model.CourseTheme;
import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.service.CourseService;
import com.gavrilov.edPlatform.service.CourseThemeService;
import com.gavrilov.edPlatform.validator.CourseThemeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/themes")
public class ThemeController {

    private final CourseService courseService;
    private final ConversionService conversionService;
    private final CourseThemeService courseThemeService;
    private final CourseThemeValidator courseThemeValidator;

    @GetMapping("/constructor")
    public String themesConstructor(Model model, @AuthenticationPrincipal PlatformUser user){
        model.addAttribute("courses", courseService.findCoursesByAuthor(user));
        model.addAttribute("formTheme", new FormTheme());
        model.addAttribute("userProfileName", user.getProfile().getName());
        return "themeConstructor";
    }

    @PostMapping("/addTheme")
    public String addTheme(Model model,
                           @ModelAttribute("formTheme") FormTheme formTheme,
                           BindingResult result,
                           @AuthenticationPrincipal PlatformUser user) {

        CourseTheme ct = conversionService.convert(formTheme, CourseTheme.class);
        courseThemeValidator.validate(ct, result);

        if (result.hasErrors()){
            model.addAttribute("courses", courseService.findCoursesByAuthor(user));
            model.addAttribute("formTheme", formTheme);
            model.addAttribute("userProfileName", user.getProfile().getName());
            return "themeConstructor";
        }

        courseThemeService.saveTheme(ct);
        return "redirect:/courses/usersCourses";
    }
}
