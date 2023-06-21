package com.anpatapain.coffwok.chat.controller;

import com.anpatapain.coffwok.chat.dto.MessageDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class MessageController {
    @MessageMapping("/{chat_room_id}")
    @SendTo("/topic/{chat_room_id}")
    @PreAuthorize("hasRole('USER')")
    public  MessageDTO sendMessage(MessageDTO messageDTO) throws Exception{
        return messageDTO;
    }
}
