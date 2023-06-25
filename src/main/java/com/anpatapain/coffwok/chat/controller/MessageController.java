package com.anpatapain.coffwok.chat.controller;

import com.anpatapain.coffwok.chat.dto.MessageDTO;
import com.anpatapain.coffwok.chat.service.ChatService;
import com.anpatapain.coffwok.chat.service.WebSocketService;
import com.anpatapain.coffwok.common.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


@Controller
public class MessageController {
    private SimpMessagingTemplate messagingTemplate;
    private WebSocketService webSocketService;

    private Logger logger = LoggerFactory.getLogger(MessageController.class);

    private ChatService chatService;

    @Autowired
    public MessageController(SimpMessagingTemplate messagingTemplate,
                             WebSocketService webSocketService,
                             ChatService chatService) {
        this.messagingTemplate = messagingTemplate;
        this.webSocketService = webSocketService;
        this.chatService = chatService;
    }

    @MessageMapping("/chat/{chat_room_id}")
    public void getMessage(@DestinationVariable("chat_room_id") String chat_room_id, MessageDTO messageDTO) throws InterruptedException {
        Thread.sleep(100);
        String topic = "/chatroom/" + chat_room_id;
        logger.info(topic + " " + messageDTO.getText());

        try {
            webSocketService.sendMessageToChatRoom(chat_room_id, topic, messageDTO);
        }catch (ResourceNotFoundException e) {
            throw e;
        }
    }
}
