package com.gavrilov.edPlatform.service;

import com.gavrilov.edPlatform.model.Course;
import com.gavrilov.edPlatform.model.Tag;

import java.util.List;

public interface TagService {

    Tag save(Tag tag);

    List<Tag> findAll();

    Tag findByName(String name);

}
