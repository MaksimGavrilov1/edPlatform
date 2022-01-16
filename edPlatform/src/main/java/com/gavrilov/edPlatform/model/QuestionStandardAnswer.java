package com.gavrilov.edPlatform.model;

import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@ToString
@Table(name = "question_standard_answer")
public class QuestionStandardAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String text;

    private Boolean isRight;

    @ManyToOne
    @JoinColumn(name = "test_question_id")
    private TestQuestion testQuestion;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        QuestionStandardAnswer that = (QuestionStandardAnswer) o;
        return id != null && Objects.equals(id, that.id);
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setRight(Boolean right) {
        isRight = right;
    }

    public void setTestQuestion(TestQuestion testQuestion) {
        this.testQuestion = testQuestion;
    }

    public String getText() {
        return text;
    }

    public Boolean getRight() {
        return isRight;
    }

    public TestQuestion getTestQuestion() {
        return testQuestion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}