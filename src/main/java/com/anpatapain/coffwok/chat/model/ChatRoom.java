package com.anpatapain.coffwok.chat.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "chatRooms")
@Getter @Setter @NoArgsConstructor
public class ChatRoom {
    @Id
    private String id;
    private String userId1;
    private String userId2;
    private List<Message> messages = new ArrayList<>();

    public void addMessage(Message message) {
        messages.add(message);
    }

    public ChatRoom(String userId1, String userId2) {
        this.userId1 = userId1;
        this.userId2 = userId2;
    }
}
