package com.endava.service_system.utils;

import com.endava.service_system.dto.UserDto;
import com.endava.service_system.enums.UserStatus;
import com.endava.service_system.model.User;
import org.springframework.core.convert.converter.Converter;

import static com.endava.service_system.enums.UserStatus.WAITING;

public class UserDtoToUserConverter implements Converter<UserDto, User> {
    @Override
    public User convert(UserDto userDto){
        User user = new User();
        user.setLogin(userDto.getLogin());
        user.setPassword(userDto.getPassword());
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setUserStatus(WAITING);
        return user;
    }
}
