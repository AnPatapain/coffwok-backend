package com.anpatapain.coffwok.chat.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chatRooms")
@Getter @Setter @NoArgsConstructor
public class ChatRoom {
    @Id
    private String id;
    private String userId1;
    private String userId2;
    private Message[] messages;

    public ChatRoom(String userId1, String userId2) {
        this.userId1 = userId1;
        this.userId2 = userId2;
    }
}
