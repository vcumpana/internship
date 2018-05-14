package com.endava.service_system.controller.rest;

import com.endava.service_system.model.*;
import com.endava.service_system.service.CompanyService;
import com.endava.service_system.service.CredentialService;
import com.endava.service_system.service.EmailService;
import com.endava.service_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class EmailRest {
    private static Logger LOGGER = LogManager.getLogger(EmailRest.class);
    private EmailService emailService;
    private CredentialService credentialService;
    private UserService userService;
    private CompanyService companyService;
    private MailSender mailSender;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    @Autowired
    public void setCredentialService(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }

    @Autowired
    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    @GetMapping("/email/test")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getEmailForm() {
        return "emailForm";
    }

    @PostMapping("/email/test")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String sendEmail(@RequestParam("to") String to, @RequestParam("subject") String subject, @RequestParam("message") String message) {
        LOGGER.log(Level.DEBUG, "to:" + to);
        LOGGER.log(Level.DEBUG, "subject:" + subject);
        LOGGER.log(Level.DEBUG, "message:" + message);
        emailService.sendEmail(to, subject, message);
        return "emailForm";
    }

    @PostMapping(value = "/admin/resetpassword", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity adminResetPassword(@RequestBody Map<String, Object> data) {
        String username = (String) data.get("username");
        LOGGER.log(Level.DEBUG, username);
        try {
            resetPasswordToUser(credentialService.getByUsername(username).get());
            return new ResponseEntity(HttpStatus.OK);
        } catch (Throwable e) {
            e.printStackTrace();
            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/forgotPassword")
    public ModelAndView getForgotPassword() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("forgotPassword");
        return modelAndView;
    }

    @PostMapping(value = "/resetPassword")
    public ResponseEntity resetPassword(@RequestBody Map<String, Object> responseBody) {
        LOGGER.log(Level.DEBUG, responseBody);
        String email = (String) responseBody.get("email");
        Optional<Credential> credentialOptional = credentialService.getByEmail(email);
        if (credentialOptional.isPresent()) {
            resetPasswordToUser(credentialOptional.get());
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    private void resetPasswordToUser(Credential user) {
        String token = getToken();
        credentialService.updatePassword(user.getUsername(), passwordEncoder.encode(token));
        String subject = "Password Reset in Payment System";
        emailService.sendEmail(user.getEmail(), subject, "We have reseted your password for user " + user.getUsername() + ". Your new password is " + token);
    }


    private String getToken() {
        return UUID.randomUUID().toString();
    }
}
