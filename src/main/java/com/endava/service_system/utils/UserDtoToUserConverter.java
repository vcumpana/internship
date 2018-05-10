package com.endava.service_system.utils;

import com.endava.service_system.dto.UserDto;
import com.endava.service_system.model.Credential;
import com.endava.service_system.model.Role;
import com.endava.service_system.model.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import static com.endava.service_system.enums.UserStatus.ACCEPTED;
import static com.endava.service_system.enums.UserStatus.WAITING;

@Component
public class UserDtoToUserConverter implements Converter<UserDto, User> {

    @Override
    public User convert(UserDto userDto) {
        Credential credential = new Credential();
        credential.setUsername(userDto.getLogin());
        credential.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));
        credential.setStatus(ACCEPTED);
        credential.setRole(Role.ROLE_USER);
        credential.setEmail(userDto.getEmail());
        User user = new User();
        user.setCredential(credential);
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());

        return user;
    }
}
