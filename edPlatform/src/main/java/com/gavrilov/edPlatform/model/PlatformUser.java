package com.gavrilov.edPlatform.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.gavrilov.edPlatform.model.enumerator.Role;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.*;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Table(name = "platform_user",
        uniqueConstraints = @UniqueConstraint(columnNames = {"username"})
)
public class PlatformUser implements UserDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @NonNull
    @NotBlank(message = "username should be more than 8 symbols")
    private String username;

    @NonNull
    private String password;

    @OneToOne(mappedBy = "platformUser", cascade = CascadeType.ALL)
    private PlatformUserProfile profile;

    @Enumerated(EnumType.STRING)
    private Role role = Role.STUDENT;

//    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER)
//    @ToString.Exclude
//    private Set<Course> ownedCourses;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_courses",
            joinColumns = @JoinColumn(name = "platform_user_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    @ToString.Exclude
    private Set<Course> joinedCourses = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ModeratorRoleRequest> requestsForModerator = new ArrayList<>();


    public PlatformUser() {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<Role>(Collections.singleton(role));
    }

    @Override
    public @NonNull String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
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



    @Override
    public String toString() {
        if (profile != null){
            return String.format("%s %s", profile.getName(), profile.getSurname());
        } else {
            return username;
        }
    }
}
