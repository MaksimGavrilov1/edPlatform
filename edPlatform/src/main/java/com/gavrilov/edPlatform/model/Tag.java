package com.gavrilov.edPlatform.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tag")
@Getter
@Setter
@ToString
public class Tag {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "tags", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<Course> courses = new HashSet<>();
}
