package com.gavrilov.edPlatform.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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

    @OneToMany(mappedBy = "testQuestion")
    @Cascade({CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REMOVE})
    @ToString.Exclude
    private List<QuestionStandardAnswer> questionStandardAnswers = new ArrayList<>();

    @ManyToOne
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
