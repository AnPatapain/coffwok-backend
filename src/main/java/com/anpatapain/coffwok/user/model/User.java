package com.anpatapain.coffwok.user.model;

import com.anpatapain.coffwok.notification.model.Notification;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
@Getter @Setter @NoArgsConstructor
public class User {
    @Id
    private String id;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max=40)
    private String password;

    private Role role;

    @NotBlank
    private AuthProvider provider;

    private String providerId;

    private String profileId;

    private String planId;

    private List<String> chatRoomIds = new ArrayList<>();

    private List<Notification> notificationList = new ArrayList<>();

    public void addChatRoomId(String chatRoomId) {
        chatRoomIds.add(chatRoomId);
    }

    public void addNotification(Notification notification) {
        boolean exists = notificationList.stream()
                .anyMatch(n -> n.getChatRoomId().equals(notification.getChatRoomId()));

        if (!exists) {
            notificationList.add(notification);
        }else {
            removeNotification(notification.getChatRoomId());
            notificationList.add(notification);
        }
    }

    public void removeNotification(String chatRoomId) {
        notificationList.removeIf(n -> n.getChatRoomId().equals(chatRoomId));
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
