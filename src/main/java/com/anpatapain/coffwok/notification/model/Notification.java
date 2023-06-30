package com.anpatapain.coffwok.notification.model;


import com.anpatapain.coffwok.profile.model.Profile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Notification {
    private String chatRoomId;
    private String messageText;
}
