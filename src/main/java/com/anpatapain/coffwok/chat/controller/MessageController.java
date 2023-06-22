package com.anpatapain.coffwok.chat.controller;

import com.anpatapain.coffwok.chat.dto.MessageDTO;
import com.anpatapain.coffwok.chat.exception.UnAuthorizedActionException;
import com.anpatapain.coffwok.chat.model.ChatRoom;
import com.anpatapain.coffwok.chat.model.Message;
import com.anpatapain.coffwok.chat.service.ChatService;
import com.anpatapain.coffwok.chat.service.WebSocketService;
import com.anpatapain.coffwok.common.exception.ResourceNotFoundException;
import com.anpatapain.coffwok.user.model.User;
import com.anpatapain.coffwok.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
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

        messagingTemplate.convertAndSend(topic,
                new Message(
                        messageDTO.getMessageType(),
                        messageDTO.getLocalDateTime(),
                        messageDTO.getText(),
                        messageDTO.getSenderId(),
                        chat_room_id
                )
        );

        try {
            ChatRoom chatRoom = chatService.pushMessageIntoChatRoomWithoutUser(messageDTO, chat_room_id);
        }catch (ResourceNotFoundException e) {
            logger.error(e.getMessage());
        }
    }
}
