package com.gavrilov.edPlatform.controller;

import com.gavrilov.edPlatform.dto.FormTheme;
import com.gavrilov.edPlatform.exception.CourseEditingException;
import com.gavrilov.edPlatform.model.Course;
import com.gavrilov.edPlatform.model.CourseTheme;
import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.service.CourseService;
import com.gavrilov.edPlatform.service.CourseThemeService;
import com.gavrilov.edPlatform.validator.CourseThemeEditValidator;
import com.gavrilov.edPlatform.validator.CourseThemeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/themes")
public class ThemeController {

    private final CourseService courseService;
    private final ConversionService conversionService;
    private final CourseThemeService courseThemeService;
    private final CourseThemeValidator courseThemeValidator;
    private final CourseThemeEditValidator editValidator;

    @GetMapping("/constructor")
    public String themesConstructor(Model model, @AuthenticationPrincipal PlatformUser user) {
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

        if (!courseService.findCourse(formTheme.getCourseId()).getAuthor().equals(user)) {
            throw new CourseEditingException("Вы не можете создать тему для этого курса, потому что вы не являетесь его автором");
        }
        CourseTheme ct = conversionService.convert(formTheme, CourseTheme.class);
        courseThemeValidator.validate(ct, result);

        if (result.hasErrors()) {
            model.addAttribute("courses", courseService.findCoursesByAuthor(user));
            model.addAttribute("formTheme", formTheme);
            model.addAttribute("userProfileName", user.getProfile().getName());
            return "themeConstructor";
        }

        courseThemeService.saveTheme(ct);
        return "redirect:/courses/usersCourses";
    }

    @GetMapping("/edit/{themeId}")
    public String renderEditTheme(Model model,
                                  @AuthenticationPrincipal PlatformUser user,
                                  @PathVariable Long themeId) {
        CourseTheme theme = courseThemeService.findTheme(themeId);
        if (!user.equals(theme.getCourse().getAuthor())) {
            throw new CourseEditingException("Вы не можете редактировать темы этого курса, потому что вы не являетесь автором этого курса");
        }
        model.addAttribute("userProfileName", user.getProfile().getName());
        model.addAttribute("theme", theme);
        return "editTheme";
    }

    @PostMapping("/edit")
    public String editTheme(@ModelAttribute(name = "theme") CourseTheme theme,
                            Model model,
                            @AuthenticationPrincipal PlatformUser user,
                            BindingResult result) {
        if (!user.equals(theme.getCourse().getAuthor())) {
            throw new CourseEditingException("Вы не можете редактировать темы этого курса, потому что вы не являетесь автором этого курса");
        }
        editValidator.validate(theme, result);
        if (result.hasErrors()){
            model.addAttribute("userProfileName", user.getProfile().getName());
            model.addAttribute("theme", theme);
            return "editTheme";
        }
        courseThemeService.saveTheme(theme);
        return String.format("redirect:/courses/owned/%d", theme.getId());
    }

    @GetMapping("/delete/{id}")
    public String deleteTheme(@PathVariable Long id, Model model, @AuthenticationPrincipal PlatformUser user) {

        CourseTheme theme = courseThemeService.findTheme(id);
        Course course = theme.getCourse();
        if (!course.getAuthor().equals(user)) {
            throw new CourseEditingException("Вы не являетесь автором курса, для того чтобы удалять темы этого курса");
        }
        courseThemeService.deleteTheme(id);
        return String.format("redirect:/courses/owned/%d", course.getId());
    }
}
