package com.gavrilov.edPlatform.model;

import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@ToString
@Table(name = "test_question")
public class TestQuestion {

    @Id
    @GeneratedValue
    private Long id;

    private String text;

    @OneToMany(mappedBy = "testQuestion")
    @Cascade({CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REMOVE})
    @ToString.Exclude
    private List<QuestionStandardAnswer> questionStandardAnswers = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "course_test_id", referencedColumnName = "id")
    @ToString.Exclude
    private CourseTest courseTest;

    public void setId(Long id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setQuestionStandardAnswers(List<QuestionStandardAnswer> questionStandardAnswers) {
        this.questionStandardAnswers = questionStandardAnswers;
    }

    public void setCourseTest(CourseTest courseTest) {
        this.courseTest = courseTest;
    }

    public List<QuestionStandardAnswer> getQuestionStandardAnswers() {
        return questionStandardAnswers;
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public CourseTest getCourseTest() {
        return courseTest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TestQuestion question = (TestQuestion) o;
        return id != null && Objects.equals(id, question.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
