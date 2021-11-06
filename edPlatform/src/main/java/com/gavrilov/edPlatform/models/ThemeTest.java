package com.gavrilov.edPlatform.models;

import com.gavrilov.edPlatform.models.enums.RateMethod;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ThemeTest {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "course_theme_id", referencedColumnName = "id")
    private CourseTheme theme;

    @Enumerated(EnumType.STRING)
    private RateMethod method;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ThemeTest themeTest = (ThemeTest) o;
        return id != null && Objects.equals(id, themeTest.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
