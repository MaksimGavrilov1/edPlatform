package com.gavrilov.edPlatform.model;

import com.gavrilov.edPlatform.model.enumerator.CourseSubscriptionStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Setter
@Getter
@ToString
@Table(name = "subscription")
public class Subscription {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private PlatformUser user;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    private Timestamp dateOfSubscription;

    private Timestamp courseEndDate;

    @Enumerated
    private CourseSubscriptionStatus status;

    public String getFormattedBeginDate(){
        String timeVal = dateOfSubscription.toString();
        String [] splitTime = timeVal.split("\\.");
        int timeValWithoutNanosIndex = 0;
        return splitTime[timeValWithoutNanosIndex];
    }

    public String getFormattedEndDate(){
        String timeVal = courseEndDate.toString();
        String [] splitTime = timeVal.split("\\.");
        int timeValWithoutNanosIndex = 0;
        return splitTime[timeValWithoutNanosIndex];
    }

    public CourseSubscriptionStatus getStatus() {
        return status;
    }
}
