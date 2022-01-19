package com.gavrilov.edPlatform.model;

import com.gavrilov.edPlatform.model.enumerator.AnswerStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table( name = "user_answer")
public class UserAnswer {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "test_question_id", referencedColumnName = "id")
    private TestQuestion question;

    private String text;

    @Enumerated(EnumType.STRING)
    private AnswerStatus status = AnswerStatus.NOT_CHOSEN;

    @ManyToOne
    @JoinColumn(name = "platform_user_id", referencedColumnName = "id")
    private PlatformUser user;

    @ManyToOne
    @JoinColumn(name = "attempt_id", referencedColumnName = "id")
    private Attempt attempt;
}
