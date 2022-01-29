package com.gavrilov.edPlatform.model;

import com.gavrilov.edPlatform.model.enumerator.RequestStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Table(name = "course_confirmation")
@Entity
@Getter
@Setter
public class CourseConfirmationRequest {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "platform_user_id")
    private PlatformUser user;

    private RequestStatus status;

    private Timestamp submitDate;

    private Timestamp answerDate;

    private String reason;

    public String getFormattedSubmitDate(){
        String timeVal = submitDate.toString();
        String [] splitTime = timeVal.split("\\.");
        int timeValWithoutNanosIndex = 0;
        return splitTime[timeValWithoutNanosIndex];
    }
    public String getFormattedAnswerDate(){
        if (answerDate == null){
            return "";
        }
        String timeVal = answerDate.toString();
        String [] splitTime = timeVal.split("\\.");
        int timeValWithoutNanosIndex = 0;
        return splitTime[timeValWithoutNanosIndex];
    }
}
