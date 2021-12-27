package com.gavrilov.edPlatform.model;

import com.gavrilov.edPlatform.model.enumerator.RateMethod;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "course_test")
public class CourseTest {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToOne
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private Course course;

    @OneToMany(mappedBy = "courseTest", cascade = CascadeType.DETACH)
    private List<TestQuestion> testQuestions;


    @Enumerated(EnumType.STRING)
    private RateMethod method;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CourseTest themeTest = (CourseTest) o;
        return id != null && Objects.equals(id, themeTest.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
