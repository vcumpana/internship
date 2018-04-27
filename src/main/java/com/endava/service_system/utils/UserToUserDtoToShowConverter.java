package com.endava.service_system.utils;

import com.endava.service_system.dto.UserDtoToShow;
import com.endava.service_system.model.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDtoToShowConverter implements Converter<User, UserDtoToShow> {
    @Override
    public UserDtoToShow convert(User user) {
        UserDtoToShow userDtoToShow = new UserDtoToShow();
        userDtoToShow.setName(user.getName());
        userDtoToShow.setSurname(user.getSurname());
        userDtoToShow.setEmail(user.getEmail());
        return userDtoToShow;
    }
}
