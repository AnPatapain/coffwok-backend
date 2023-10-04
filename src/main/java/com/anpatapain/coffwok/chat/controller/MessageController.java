package com.anpatapain.coffwok.chat.controller;

import com.anpatapain.coffwok.chat.aop.Notify;
import com.anpatapain.coffwok.chat.config.WebSocketConfig;
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
    private final WebSocketService webSocketService;

    @Autowired
    public MessageController(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @MessageMapping("/chat/{chat_room_id}")
    @Notify
    public void getMessage(@DestinationVariable("chat_room_id") String chat_room_id, MessageDTO messageDTO) throws InterruptedException {
        Thread.sleep(100);
        String topic = WebSocketConfig.chatTopic + "/" + chat_room_id;
        webSocketService.sendMessageToChatRoom(chat_room_id, topic, messageDTO);
    }
}
