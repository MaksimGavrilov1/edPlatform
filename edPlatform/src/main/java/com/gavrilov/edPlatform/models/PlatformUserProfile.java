package com.gavrilov.edPlatform.models;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class PlatformUserProfile {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String surname;

    private String middleName;

    private String selfDescription;

    @OneToOne(mappedBy = "profile")
    private PlatformUser platformUser;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PlatformUserProfile that = (PlatformUserProfile) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
