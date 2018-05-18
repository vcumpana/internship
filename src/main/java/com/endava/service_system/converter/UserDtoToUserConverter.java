package com.endava.service_system.converter;

import com.endava.service_system.model.dto.UserDto;
import com.endava.service_system.model.entities.Credential;
import com.endava.service_system.model.enums.Role;
import com.endava.service_system.model.entities.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import static com.endava.service_system.model.enums.UserStatus.ACCEPTED;

@Component
public class UserDtoToUserConverter implements Converter<UserDto, User> {

    @Override
    public User convert(UserDto userDto) {
        Credential credential = new Credential();
        credential.setUsername(userDto.getLogin());
        credential.setPassword(userDto.getPassword());
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
