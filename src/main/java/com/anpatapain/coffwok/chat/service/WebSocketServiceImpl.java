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
    public void notifyFrontend(MessageDTO messageDTO,String chat_room_id){
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("messageType", messageDTO.getMessageType());
        hashMap.put("localDateTime", messageDTO.getLocalDateTime());
        hashMap.put("text", messageDTO.getText());
        hashMap.put("senderId", messageDTO.getSenderId());

        messagingTemplate.convertAndSend("/topic/"+chat_room_id,hashMap);
    }


}
