package com.endava.service_system.converter;

import com.endava.service_system.model.dto.UserAdminDTO;
import com.endava.service_system.model.entities.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserAdminDTOConverter implements Converter<User, UserAdminDTO> {
    @Override
    public UserAdminDTO convert(User source) {
        UserAdminDTO adminDTO=new UserAdminDTO();
        if(source.getCredential()!=null&&source.getCredential().getBankAccount()!=null)
        adminDTO.setBankAccount(source.getCredential().getBankAccount().getCountNumber());
        adminDTO.setEmail(source.getCredential().getEmail());
        adminDTO.setName(source.getName());
        adminDTO.setStatus(source.getCredential().getStatus());
        adminDTO.setSurname(source.getSurname());
        adminDTO.setUsername(source.getCredential().getUsername());
        return adminDTO;
    }
}
