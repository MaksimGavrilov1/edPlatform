package com.gavrilov.edPlatform.controller;

import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.model.Tag;
import com.gavrilov.edPlatform.service.TagService;
import com.gavrilov.edPlatform.validator.TagValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tag")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;
    private final TagValidator tagValidator;

    @PreAuthorize("hasAuthority('MODERATOR')")
    @GetMapping("/all")
    public String allTags(Model model, @AuthenticationPrincipal PlatformUser user) {
        model.addAttribute("userProfileName", user.getProfile().getName());
        model.addAttribute("tags", tagService.findAll());
        model.addAttribute("formTag", new Tag());
        return "tag/tags";
    }

    @PreAuthorize("hasAuthority('MODERATOR')")
    @PostMapping("/add")
    public String addTag(@ModelAttribute("formTag") Tag tag,
                         Model model,
                         @AuthenticationPrincipal PlatformUser user,
                         BindingResult result) {
        tagValidator.validate(tag, result);
        if (result.hasErrors()) {
            model.addAttribute("userProfileName", user.getProfile().getName());
            model.addAttribute("tags", tagService.findAll());
            model.addAttribute("formTag", tag);
            return "tag/tags";
        }
        tagService.save(tag);
        return "redirect:/tag/all";
    }


}
