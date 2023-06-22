package com.anpatapain.coffwok.chat.controller;

import com.anpatapain.coffwok.chat.dto.MessageDTO;
import com.anpatapain.coffwok.chat.service.WebSocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.HtmlUtils;

@Controller
public class MessageController {
    private SimpMessagingTemplate messagingTemplate;
    private WebSocketService webSocketService;

    private Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    public MessageController(SimpMessagingTemplate messagingTemplate, WebSocketService webSocketService) {
        this.messagingTemplate = messagingTemplate;
        this.webSocketService = webSocketService;
    }

    @PreAuthorize("hasRole('USER')")
    @MessageMapping("/chat/{chat_room_id}")
    public void getMessage(@DestinationVariable("chat_room_id") String chat_room_id, MessageDTO message) throws InterruptedException {
        Thread.sleep(100);
        String topic = "/chatroom/" + chat_room_id;
//        logger.info(topic + " " + HtmlUtils.htmlEscape(message.getMessageContent()));
        messagingTemplate.convertAndSend(topic,
                new MessageDTO(
                        message.getMessageType(),
                        message.getLocalDateTime(),
                        message.getText(),
                        message.getSenderId()
                )
        );
    }
}
