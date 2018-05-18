package com.endava.service_system.service;

import com.endava.service_system.dao.MessageDao;
import com.endava.service_system.model.entities.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageDao messageDao;
    public Page<Message> getAll(Pageable pageable){
        return messageDao.getAllByOrderByDateDesc(pageable);
    }

    @Transactional
    public void setRead(long id, boolean value) {
        messageDao.markAsRead(id,value);
    }

    public Message save(Message message) {
        message.setDate(LocalDateTime.now());
        return messageDao.save(message);
    }
}
