package com.gavrilov.edPlatform.model;

import com.gavrilov.edPlatform.model.enumerator.RequestStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "moderator_role_request")
@Getter
@Setter
@ToString
public class ModeratorRoleRequest {

    @Id
    @GeneratedValue
    private Long id;

    private String experience;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "platform_user_id", referencedColumnName = "id")
    private PlatformUser user;

    private RequestStatus status = RequestStatus.PENDING;

    private String reason;

    private String approverUsername;
}