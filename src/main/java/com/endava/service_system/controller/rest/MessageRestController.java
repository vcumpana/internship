package com.endava.service_system.controller.rest;

import com.endava.service_system.dto.MessageDto;
import com.endava.service_system.model.Message;
import com.endava.service_system.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class MessageRestController {

    private final MessageService messageService;

    @GetMapping(value = "/admin/messages", params = { "page", "size" })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity markAsRead(@PathVariable("id") long id){
        messageService.setRead(id,true);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping(value = "/admin/markAsUnread/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity markAsUnread(@PathVariable("id") long id){
        messageService.setRead(id,false);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/contact")
    public ModelAndView getContactPage(){
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("contactPage");
        modelAndView.addObject("message",new MessageDto());
        return modelAndView;
    }

    @PostMapping("/contact")
    public ModelAndView saveMessage(@Valid @ModelAttribute("message") MessageDto message, BindingResult bindingResult){
        String messageText=null;
        if(!bindingResult.hasErrors()){
            Message realMessage=new Message();
            realMessage.setFromEmail(message.getFromEmail());
            realMessage.setText(message.getText());
            messageService.save(realMessage);
            messageText="Message was sent";
        }
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("contactPage");
        if(messageText!=null)
        modelAndView.addObject("messageText",messageText);
        return modelAndView;
    }

}
