package com.gavrilov.edPlatform.service.tagServiceImpl;

import com.gavrilov.edPlatform.model.Course;
import com.gavrilov.edPlatform.model.Tag;
import com.gavrilov.edPlatform.repo.TagRepository;
import com.gavrilov.edPlatform.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public Tag save(Tag tag) {
        tag.setName(tag.getName().trim().toLowerCase(Locale.ROOT));
        return tagRepository.save(tag);
    }

    @Override
    public List<Tag> findByCourse(Course course) {
        return tagRepository.findByCourse(course);
    }
}
