package com.gavrilov.edPlatform.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.gavrilov.edPlatform.model.enumerator.CourseStatus;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "name")
@Table(name = "course")
public class Course {



    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "platform_user_id", referencedColumnName = "id")
    @ToString.Exclude
    private PlatformUser author;


    private String name;

    @Column(length = 50_000)
    private String description;

    @OneToMany(mappedBy = "course")
    @ToString.Exclude
    private Set<CourseTheme> themes;

    @Enumerated(EnumType.STRING)
    private CourseStatus status = CourseStatus.DRAFT;



    @OneToMany(mappedBy = "course")
    @ToString.Exclude
    private List<Subscription> subscriptions = new ArrayList<>();

    @OneToOne(mappedBy = "course")
    @ToString.Exclude
    private CourseTest test;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tags_courses",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @ToString.Exclude
    private List<Tag> tags;

    private Timestamp activeTime;

    private Boolean isAlwaysOpen;

    public Course() {
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

    public Integer getActiveDays(){
        return Math.toIntExact(activeTime.getTime() / 1000 / 60 / 60 / 24);
    }

    @Override
    public String toString() {
        return name;
    }
}
