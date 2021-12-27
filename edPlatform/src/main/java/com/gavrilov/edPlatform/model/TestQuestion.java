package com.gavrilov.edPlatform.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Table(name = "test_question")
public class TestQuestion {

    @Id
    @GeneratedValue
    private Long id;

    private String text;

    private Integer rightAnswerAmount;

    @OneToMany(mappedBy = "testQuestion", cascade = CascadeType.MERGE)
    @ToString.Exclude
    private List<QuestionStandardAnswer> questionStandardAnswers = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "course_test_id", referencedColumnName = "id")
    @ToString.Exclude
    private CourseTest courseTest;




//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        TestQuestion question = (TestQuestion) o;
//        return id.equals(question.id);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id);
//    }
}
