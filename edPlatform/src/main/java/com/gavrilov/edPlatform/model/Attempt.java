package com.gavrilov.edPlatform.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "attempt")
public class Attempt {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "course_test_id", referencedColumnName = "id")
    private CourseTest courseTest;

    @ManyToOne
    @JoinColumn(name = "platform_user_id", referencedColumnName = "id")
    private PlatformUser user;

    private Timestamp time;

    private Integer mark;
}
