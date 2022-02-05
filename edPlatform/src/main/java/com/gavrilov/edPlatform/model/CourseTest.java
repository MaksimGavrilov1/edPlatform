package com.gavrilov.edPlatform.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
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

    @OneToMany(mappedBy = "courseTest")
    @Cascade({CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REMOVE})
    @ToString.Exclude
    private List<TestQuestion> testQuestions;

    private Integer amountOfAttempts;

    private Integer minThreshold;


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
