package com.endava.service_system.controller.rest;

import com.endava.service_system.dto.CredentialDTO;
import com.endava.service_system.model.*;
import com.endava.service_system.service.*;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
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
    @Qualifier("siteUrl")
    private String sireUrl;
    private TokenService tokenService;

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

    @Autowired
    public void setSireUrl(@Qualifier("siteUrl") String sireUrl) {
        this.sireUrl = sireUrl;
    }

    @Autowired
    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
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

    @GetMapping("/resetpassword/{token}")
    public ModelAndView getResetPasswordView(@PathVariable("token")String token,Model model){
        Optional<Token> tokenOptional=tokenService.getToken(token);
        if(!tokenOptional.isPresent()){
            model.addAttribute("tokenError","Wrong Token");
        }else{
            Token realToken=tokenOptional.get();
            boolean isActual=realToken.getEndDate().isAfter(LocalDateTime.now());
            if(!isActual){
                model.addAttribute("tokenError","Token is expired ");
            }
            if(realToken.isUsed()){
                model.addAttribute("tokenError","Token was used");
            }
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("passwordDto",new PasswordDto());
        modelAndView.setViewName("resetPassword");
        return modelAndView;
    }

    @PostMapping("/resetpassword/{token}")
    public ModelAndView resetPassword(@PathVariable("token")String token,@ModelAttribute("passwordDto") @Valid PasswordDto passwordDto, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        LOGGER.debug(passwordDto);
        ModelAndView modelAndView = new ModelAndView();
        Optional<Token> tokenOptional=tokenService.getToken(token);

        Token realToken=null;
        if(!tokenOptional.isPresent()){
            bindingResult.rejectValue("token","wrong token","Wrong Token");
        }else{
            realToken=tokenOptional.get();
            boolean isActual=realToken.getEndDate().isAfter(LocalDateTime.now());
            if(!isActual){
                bindingResult.rejectValue("token","wrong token","Token is expired ");
            }
            if(realToken.isUsed()){
                bindingResult.rejectValue("token","wrong token","Token was used");
            }
        }
        if(bindingResult.hasErrors()){
            modelAndView.setViewName("resetPassword");
        }else {
            realToken.setUsed(true);
            tokenService.save(realToken);
            CredentialDTO credentialDTO=new CredentialDTO();
            credentialDTO.setPassword(passwordDto.getNewPassword());
            credentialDTO.setConfirmPassword(passwordDto.getRepeatedNewPassword());
            credentialService.updateStatusAndPassword(realToken.getUsername(),credentialDTO);
            redirectAttributes.addFlashAttribute("message","You have set new password");
            modelAndView.setViewName("redirect:/login");
        }
        return modelAndView;
    }

    private void resetPasswordToUser(Credential user) {
        String token = getToken();
        Token realToken=new Token();
        realToken.setUsername(user.getUsername());
        realToken.setToken(token);
        realToken.setUsed(false);
        realToken.setStartDate(LocalDateTime.now());
        realToken.setEndDate(LocalDateTime.now().plusDays(1));
        tokenService.save(realToken);
        String subject = "Password Reset in Payment System";
        emailService.sendEmail(user.getEmail(), subject, "You can reset your password here :  " + sireUrl + "/resetpassword/"+ token);
    }


    private String getToken() {
        return UUID.randomUUID().toString();
    }
}
