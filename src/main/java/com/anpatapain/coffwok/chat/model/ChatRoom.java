package com.anpatapain.coffwok.chat.model;

import com.anpatapain.coffwok.profile.model.Profile;
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
    private Profile profile1;
    private Profile profile2;
    private List<Message> messages = new ArrayList<>();

    public void addMessage(Message message) {
        messages.add(message);
    }

    public ChatRoom(Profile profile1, Profile profile2) {
        this.profile1 = profile1;
        this.profile2 = profile2;
    }
}
