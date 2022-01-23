package com.gavrilov.edPlatform.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

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

    private Boolean pass;

    public Long getId() {
        return id;
    }

    public CourseTest getCourseTest() {
        return courseTest;
    }

    public PlatformUser getUser() {
        return user;
    }

    public Timestamp getTime() {

        return time;
    }

    public String getFormattedTime(){
        String timeVal = time.toString();
        String [] splitTime = timeVal.split("\\.");
        int timeValWithoutNanosIndex = 0;
        return splitTime[timeValWithoutNanosIndex];
    }

    public Integer getMark() {
        return mark;
    }

    public Boolean getPass() {
        return pass;
    }
}
