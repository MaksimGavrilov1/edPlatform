package com.gavrilov.edPlatform.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Table(name = "question_standard_answer")
public class QuestionStandardAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String text;

    private Boolean isRight;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "test_question_id")
    private TestQuestion testQuestion;


}