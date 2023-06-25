package com.anpatapain.coffwok.chat.service;

import com.anpatapain.coffwok.chat.dto.MessageDTO;
import com.anpatapain.coffwok.chat.model.ChatRoom;
import com.anpatapain.coffwok.chat.model.Message;
import com.anpatapain.coffwok.common.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class WebSocketServiceImpl implements WebSocketService{
    private SimpMessagingTemplate messagingTemplate;
    private ChatService chatService;

    @Autowired
    public WebSocketServiceImpl(ChatService chatService, SimpMessagingTemplate messagingTemplate){
        this.messagingTemplate= messagingTemplate;
        this.chatService = chatService;
    }
    @Override
    public void sendMessageToChatRoom(String chat_room_id, String topic, MessageDTO messageDTO){
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
            throw e;
        }
    }


}
