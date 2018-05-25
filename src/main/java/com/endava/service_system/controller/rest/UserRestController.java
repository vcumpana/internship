package com.endava.service_system.controller.rest;

import com.endava.service_system.model.dto.CredentialDTO;
import com.endava.service_system.model.dto.UserAdminDTO;
import com.endava.service_system.model.dto.UserDtoToShow;
import com.endava.service_system.model.dto.UserPasswordDto;
import com.endava.service_system.model.entities.Credential;
import com.endava.service_system.model.entities.User;
import com.endava.service_system.model.enums.UserStatus;
import com.endava.service_system.service.BankService;
import com.endava.service_system.service.CredentialService;
import com.endava.service_system.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class UserRestController {

    private static final Logger LOGGER=LogManager.getLogger(UserRestController.class);
    private UserService userService;
    private CredentialService credentialService;
    private PasswordEncoder passwordEncoder;
    private ConversionService conversionService;
    private BankService bankService;
    private static final int DEFAULT_USERS_SIZE=10;

    @PutMapping(value = "/user/selfUpdate")
    public void updateUser(@RequestBody UserDtoToShow userDtoToShow) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        userService.updateUserNameAndSurname(username, userDtoToShow);
    }

    @PutMapping(value = "/user/selfUpdatePassword")
    public ResponseEntity updateUserPassword(@RequestBody UserPasswordDto userPasswordDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Credential credential = credentialService.getByUsername(username).get();
        if (passwordEncoder.matches(userPasswordDto.getOldPassword(), credential.getPassword())) {
            String encoded=passwordEncoder.encode(userPasswordDto.getNewPassword());
            credentialService.updatePassword(username, encoded);
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/admin/users")
    public ResponseEntity getAllUsers(@RequestParam(required = false, value = "status") UserStatus status,
                                      @RequestParam(required = false,value = "page") Integer page) {
        if(page==null||page<0){
            page=0;
        }
        Map<String,Object> result= userService.getAll(PageRequest.of(page,DEFAULT_USERS_SIZE),status);
        List<User> users = (List<User>) result.get("users");
        List<UserAdminDTO> usersForAdmin = users.stream()
                .map((user) -> conversionService.convert(user, UserAdminDTO.class))
                .collect(Collectors.toList());
        result.put("users",usersForAdmin);
        LOGGER.debug(result);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @PutMapping("/admin/users/{username}")
    public ResponseEntity changePasswordWithStatus(@PathVariable("username") String username,
                                                   @Validated @RequestBody CredentialDTO credentialDTO,
                                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        try {
            if(credentialDTO.getStatus()==UserStatus.ACCEPTED){
                Optional<Credential> userCredentials=credentialService.getByUsername(username);
                if(!userCredentials.isPresent()){
                    String json = "{\"message\":\"Bank Problem\"}";
                    return new ResponseEntity(json, HttpStatus.INTERNAL_SERVER_ERROR);
                }
                bankService.addBankAccount(userCredentials.get());
            }
            int entitiesUpdated = credentialService.updateStatusAndPassword(username, credentialDTO);
            String json = "{\"count\":\"1\"}";
            return new ResponseEntity(json, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Autowired
    public void setCredentialService(CredentialService credentialService) {
        this.credentialService = credentialService;
    }
    @Autowired
    public void setBankService(BankService bankService) {
        this.bankService = bankService;
    }
}
