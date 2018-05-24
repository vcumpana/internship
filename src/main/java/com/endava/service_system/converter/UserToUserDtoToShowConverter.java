package com.endava.service_system.converter;

import com.endava.service_system.model.dto.UserDtoToShow;
import com.endava.service_system.model.entities.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDtoToShowConverter implements Converter<User, UserDtoToShow> {
    @Override
    public UserDtoToShow convert(User user) {
        UserDtoToShow userDtoToShow = new UserDtoToShow();
        userDtoToShow.setName(user.getName());
        userDtoToShow.setSurname(user.getSurname());
        userDtoToShow.setEmail(user.getCredential().getEmail());
        return userDtoToShow;
    }
}
