package com.gavrilov.edPlatform.model;

import lombok.Getter;
import lombok.ToString;
import org.springframework.stereotype.Service;

import javax.persistence.*;

@Table(name = "moderator_role_answer")
@Entity
@Getter
@Service
@ToString
public class ModeratorRoleAnswer {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "moderator_role_request_id", referencedColumnName = "id")
    private ModeratorRoleRequest request;

    private String reason;

}
