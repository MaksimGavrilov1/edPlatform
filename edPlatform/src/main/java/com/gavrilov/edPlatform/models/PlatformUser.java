package com.gavrilov.edPlatform.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.gavrilov.edPlatform.models.enums.Role;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class PlatformUser {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    private String login;

    @NonNull
    @JsonIgnore
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "platform_user_profile_id", referencedColumnName = "id")
    private PlatformUserProfile profile;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "author")
    private Set<Course> ownedCourses;

    @ManyToMany
    @JoinTable(
            name = "users_courses",
            joinColumns = @JoinColumn(name = "platform_user_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Set<Course> joinedCourses;


    protected PlatformUser() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PlatformUser that = (PlatformUser) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
