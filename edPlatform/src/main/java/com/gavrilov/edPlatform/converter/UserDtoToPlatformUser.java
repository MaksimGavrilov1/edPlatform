package com.gavrilov.edPlatform.converter;

import com.gavrilov.edPlatform.dto.UserDto;
import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.model.PlatformUserProfile;
import org.springframework.core.convert.converter.Converter;

public class UserDtoToPlatformUser implements Converter<UserDto, PlatformUser> {
    @Override
    public PlatformUser convert(UserDto source) {

        PlatformUser user = new PlatformUser();
        PlatformUserProfile profile = new PlatformUserProfile();
        user.setProfile(profile);
        user.setUsername(source.getUsername());
        user.setPassword(source.getPassword());
        profile.setPlatformUser(user);
        profile.setName(source.getName());
        profile.setSurname(source.getSurname());
        profile.setMiddleName(source.getMiddleName());
        profile.setSelfDescription(source.getSelfDescription());
        return user;
    }
}
