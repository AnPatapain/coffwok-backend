package com.anpatapain.coffwok.chat.model;

import jakarta.annotation.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor
public class Message {
    @Id
    private String id;
    private MessageType type;
    private LocalDateTime timeStamp;
    private String text;
    private String senderId;
    private String chatRoomId;

    public Message(MessageType type, LocalDateTime timeStamp, String text, String senderId, String chatRoomId) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.timeStamp = timeStamp;
        this.text = text;
        this.senderId = senderId;
        this.chatRoomId = chatRoomId;
    }
}
