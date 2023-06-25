package com.anpatapain.coffwok.chat.service;

import com.anpatapain.coffwok.chat.dto.MessageDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public interface WebSocketService {
    public void sendMessageToChatRoom(String chat_room_id, String topic, MessageDTO messageDTO);

}
