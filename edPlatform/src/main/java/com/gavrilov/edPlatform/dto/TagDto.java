package com.gavrilov.edPlatform.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class TagDto {

    private String name;
    private Boolean checked;

    public TagDto() {
    }

    public TagDto(String name) {
        this.name = name;
    }

    public TagDto(String name, Boolean checked) {
        this.name = name;
        this.checked = checked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagDto tagDto = (TagDto) o;
        return name.equals(tagDto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
