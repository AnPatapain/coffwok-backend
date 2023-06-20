package com.anpatapain.coffwok.chat.service;

import com.anpatapain.coffwok.chat.dto.MessageDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public interface WebSocketService {

    /***?
     *  Only use in chat by api only:
     *  ex: notifyFrontend will be triggered in the below case:
     *  www.coffwok/chatroom1234
     * @param messageDTO
     */
    public void notifyFrontend(MessageDTO messageDTO, String chat_room_id);

}
