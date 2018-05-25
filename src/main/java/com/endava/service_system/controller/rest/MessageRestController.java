package com.endava.service_system.controller.rest;

import com.endava.service_system.model.dto.MessageDto;
import com.endava.service_system.model.entities.Credential;
import com.endava.service_system.model.entities.Message;
import com.endava.service_system.service.CredentialService;
import com.endava.service_system.service.MessageService;
import com.endava.service_system.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MessageRestController {
    private static final Logger LOGGER=LogManager.getLogger(MessageRestController.class);
    private final MessageService messageService;
    private final AuthUtils authUtils;
    private final CredentialService credentialService;

    @GetMapping(value = "/admin/messages", params = { "page", "size" })
    public Page<Message> getMessages(@RequestParam( "page" ) Integer page, @RequestParam( "size" ) Integer size){
        return messageService.getAll(getPageable(page,size));
    }

    private Pageable getPageable(Integer page,Integer size){
        if(page==null||page<0){
            page=0;
        }
        if(size==null||size<0){
            size=20;
        }

        return PageRequest.of(page,size);
    }

    @PutMapping(value = "/admin/markAsRead/{id}")
    public ResponseEntity markAsRead(@PathVariable("id") long id){
        messageService.setRead(id,true);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping(value = "/admin/markAsUnread/{id}")
    public ResponseEntity markAsUnread(@PathVariable("id") long id){
        messageService.setRead(id,false);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/contact")
    public ModelAndView getContactPage(Authentication authentication){
        String email=null;
        LOGGER.debug(authentication);
        if(authUtils.isLoggedIn(authentication)){
            Optional<Credential> credentialOptional=credentialService.getByUsername(authentication.getName());
            if(credentialOptional.isPresent()){
                email=credentialOptional.get().getEmail();
            }
        }
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("contactPage");

        MessageDto messageDto=new MessageDto();
        if(email!=null){
            messageDto.setFromEmail(email);
        }
        modelAndView.addObject("message",messageDto);
        return modelAndView;
    }

    @PostMapping("/contact")
    public ModelAndView saveMessage(HttpServletRequest request, @Valid @ModelAttribute("message") MessageDto message, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        ModelAndView modelAndView=new ModelAndView();
        String messageText=null;
        if(!bindingResult.hasErrors()){
            Message realMessage=new Message();
            realMessage.setFromEmail(message.getFromEmail());
            realMessage.setText(message.getText());
            messageService.save(realMessage);
            messageText="Message was sent";
            modelAndView.setViewName("contactPage");
        }
        modelAndView.setViewName(getPreviousPageByRequest(request).orElse("/"));
        if(messageText!=null) redirectAttributes.addFlashAttribute("messageText",messageText);
        return modelAndView;
    }

    protected Optional<String> getPreviousPageByRequest(HttpServletRequest request)
    {
        return Optional.ofNullable(request.getHeader("Referer")).map(requestUrl -> "redirect:" + requestUrl);
    }

}
