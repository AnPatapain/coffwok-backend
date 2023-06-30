package com.anpatapain.coffwok.chat.controller;

import com.anpatapain.coffwok.chat.dto.MessageDTO;
import com.anpatapain.coffwok.chat.model.ChatRoom;
import com.anpatapain.coffwok.chat.service.ChatService;
import com.anpatapain.coffwok.notification.service.NotificationService;
import com.anpatapain.coffwok.chat.service.WebSocketService;
import com.anpatapain.coffwok.common.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;


@Controller
public class MessageController {
    private WebSocketService webSocketService;
    private ChatService chatService;

    private NotificationService notificationService;


    private Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    public MessageController(WebSocketService webSocketService,
                             ChatService chatService,
                             NotificationService notificationService) {
        this.webSocketService = webSocketService;
        this.chatService = chatService;
        this.notificationService = notificationService;
    }

    @MessageMapping("/chat/{chat_room_id}")
    public void getMessage(@DestinationVariable("chat_room_id") String chat_room_id, MessageDTO messageDTO) throws InterruptedException {
        Thread.sleep(100);
        String topic = "/chatroom/" + chat_room_id;
        logger.info(topic + " " + messageDTO.getText());

        try {
            webSocketService.sendMessageToChatRoom(chat_room_id, topic, messageDTO);
            ChatRoom chatRoom = chatService.getOneByChatRoomId(chat_room_id);

            String to_notify_user_id = chatRoom.getProfile1().getUserId().equals(messageDTO.getSenderId()) ?
                    chatRoom.getProfile2().getUserId() : chatRoom.getProfile1().getUserId();

            notificationService.notifyMessageToUser(to_notify_user_id, chat_room_id, messageDTO);
        }catch (ResourceNotFoundException e) {
            throw e;
        }
    }
}
