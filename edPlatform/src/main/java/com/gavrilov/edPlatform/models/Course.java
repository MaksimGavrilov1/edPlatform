package com.gavrilov.edPlatform.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.gavrilov.edPlatform.models.enums.CourseStatus;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "name")
public class Course {


    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "platform_user_id", referencedColumnName = "id")
    private PlatformUser author;

    @NonNull
    private String name;

    private String description;

    @OneToMany(mappedBy = "course")
    @ToString.Exclude
    private Set<CourseTheme> themes;

    @Enumerated(EnumType.STRING)
    private CourseStatus status;

    @ManyToMany(mappedBy = "joinedCourses")
    @ToString.Exclude
    private Set<PlatformUser> joinedUsers;

    protected Course() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Course course = (Course) o;
        return id != null && Objects.equals(id, course.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
