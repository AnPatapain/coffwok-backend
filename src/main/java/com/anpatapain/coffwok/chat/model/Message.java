package com.anpatapain.coffwok.chat.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor
public class Message {
    @Id
    private String id;
    private MessageType type;
    private LocalDateTime timeStamp;
    private String text;
    private String senderId;
    private String receiverId;

    public Message(MessageType type, LocalDateTime timeStamp, String text, String senderId, String receiverId) {
        this.type = type;
        this.timeStamp = timeStamp;
        this.text = text;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }
}
