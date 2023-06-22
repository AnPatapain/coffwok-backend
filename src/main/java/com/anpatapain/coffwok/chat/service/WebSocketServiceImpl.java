package com.anpatapain.coffwok.chat.service;

import com.anpatapain.coffwok.chat.dto.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class WebSocketServiceImpl implements WebSocketService{
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketServiceImpl(SimpMessagingTemplate messagingTemplate){
        this.messagingTemplate= messagingTemplate;
    }
    @Override
    public void notifyFrontend(String topic, MessageDTO messageDTO){
        messagingTemplate.convertAndSend(topic,
                new MessageDTO(
                        messageDTO.getMessageType(),
                        messageDTO.getLocalDateTime(),
                        messageDTO.getText(),
                        messageDTO.getSenderId()
                )
        );
    }


}
