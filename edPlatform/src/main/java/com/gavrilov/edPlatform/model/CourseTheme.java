package com.gavrilov.edPlatform.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "course_theme")
public class CourseTheme {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private Course course;

    @Column(length = 1_000)
    private String name;

    @Column(length = 50_000)
    private String lectureMaterial;

    @OneToOne(mappedBy = "theme")
    private ThemeTest test;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseTheme that = (CourseTheme) o;
        return course.equals(that.course) && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
